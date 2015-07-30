package com.baodian.dao.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.baodian.dao.util.impl.UtilDaoImpl;
import com.baodian.model.task.Task;
import com.baodian.model.task.TaskList;
import com.baodian.model.task.Task_Depm;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.TaskPage;

@Repository("taskDao")
public class TaskDao extends UtilDaoImpl {
//r
	/**
	 * 获取任务
	 */
	public List<Task> getTaskSetByPage(TaskPage page) {
		StringBuilder whereSql = new StringBuilder();
		whereSql.append(" from Task task where 1=1");
		//StringBuilder orderSql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		if(StaticMethod.StrSize(page.getTitle()) > 0) {
			whereSql.append(" and (task.title like(?) or task.content like(?))");
			params.add("%" + page.getTitle() + "%");
			params.add("%" + page.getTitle() + "%");
		}
		if(page.getStatus() > 0) {
			whereSql.append(" and task.status=" + page.getStatus());
		}
		return getObjsByPage("select count(task.id)" + whereSql,
				whereSql + " order by task.id desc", page, params);
	}
	/**
	 * 获取任务列表
	 */
	public List<TaskList> getTaskListByPage(TaskPage page) {
		StringBuilder whereSql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		if(page.getTime() > 0) {
			whereSql.append(" from TaskList taskList, Task task, Task_Depm td where 1=1");
			Calendar cal = Calendar.getInstance();
			String now = StaticMethod.DateToString(cal.getTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			//今天凌晨时间
			String today = StaticMethod.DateToString(cal.getTime());
			cal.add(Calendar.DATE, 1);
			//明天凌晨时间
			String tomorrow = StaticMethod.DateToString(cal.getTime());
			switch(page.getTime()) {
			case 2://部门明天任务
				whereSql.append(" and taskList.doingDate>='" + tomorrow + "'");
				break;
			case 3://部门今天任务
				whereSql.append(" and taskList.doingDate<'" + tomorrow +
						"' and taskList.doingDate>='" + today + "'");
				break;
			case 4://部门当前(历史)任务
				whereSql.append(" and taskList.doingDate<'" + now + "'");
				break;
			}
			whereSql.append(" and td.depm.id=" + SecuManagerImpl.currentID(1) +
					" and task.id=td.task.id");
		} else {
			whereSql.append(" from TaskList taskList, Task task where 1=1");
		}
		if(StaticMethod.StrSize(page.getTitle()) > 0) {
			whereSql.append(" and (task.title like(?) or task.content like(?))");
			params.add("%" + page.getTitle() + "%");
			params.add("%" + page.getTitle() + "%");
		}
		if(page.getStatus() > 0) {
			whereSql.append(" and taskList.status=" + page.getStatus());
		}
		whereSql.append(" and taskList.taskRef.id=task.id");
		return getObjsByPage("select count(taskList.id)" + whereSql,
				"select new TaskList(taskList.id, taskList.taskDate, taskList.doingDate," +
					"taskList.doneDate, taskList.status, taskList.user, taskList.department," +
					"task.id, task.title, task.content)" +
				whereSql + " order by taskList.doingDate desc", page, params);
	}
	/**
	 * 根据taskId查找部门
	 */
	@SuppressWarnings("unchecked")
	public List<Task_Depm> getNameByIds(String ids) {
		return super.ht.find("select new Task_Depm(td.task.id, dpm.id, dpm.name)" +
				" from Task_Depm td, Department dpm" +
				" where td.task.id in(" + ids +") and td.depm.id=dpm.id");
	}
	/**
	 * 统计任务列表数量
	 */
	public long countList(int id) {
		return (Long) ht.find("select count(*) " +
				"from TaskList tl where tl.taskRef.id=" + id).get(0);
	}
	/**
	 * 获取需要完成的任务
	 */
	@SuppressWarnings("unchecked")
	public List<TaskList> getNeedDoTask(int dpmId) {
		return ht.find("select new TaskList(tl.id, tl.doingDate)" +
				" from TaskList tl, Task_Depm td" +
				" where td.depm.id=" + dpmId +
				" and tl.status=1 and tl.taskRef=td.task");
	}
	/**
	 * 根据任务列表id查找标题
	 */
	public String getTitle(String tlId) {
		List<String> titles = super.getObjs("select t.title from Task t, TaskList tl" +
				" where tl.id=" + tlId + " and tl.taskRef.id=t.id", 1);
		if(titles.size() != 1) {
			return null;
		}
		return titles.get(0);
	}
	/**
	 * 统计后台的任务数量
	 */
	public long count2DayList(String ttomorrow) {
		return (Long) ht.find("select count(*) " +
				"from TaskList tl where tl.changetime=1 and tl.doingDate>='" + ttomorrow + "'").get(0);
	}
	/**
	 * 获取全部运行的任务
	 */
	@SuppressWarnings("unchecked")
	public List<Task> getTaskSet() {
		return ht.find("select new Task(t.id, t.begin, t.end, t.data, t.type)" +
				"from Task t where t.status=1");
	}
//u
	/**
	 * 更改提醒时间
	 */
	public void updateDoningDate(String taskListId, String doingDate) {
		ht.bulkUpdate("update TaskList tl" +
				" set tl.changetime=2, tl.doingDate='" + doingDate + "' where tl.id=" + taskListId);
	}
	/**
	 * 更新任务状态
	 */
	public void updateStatus(String status, String id, String user, String dpm) {
		ht.bulkUpdate("update TaskList tl" +
				" set tl.status=" + status + ", user=?, department=?, doneDate=?" +
				" where tl.id=" + id + " and tl.status!=" + status, user, dpm, new Date());
		
	}
//d
	/**
	 * 根据task id 删除执行部门
	 */
	public void deleteDT(int taskId) {
		super.ht.bulkUpdate("delete from Task_Depm td where td.task.id=" + taskId);
	}
	/**
	 * 删除今天开始的任务
	 */
	public void deleteTL(int tid, String begin) {
		super.ht.bulkUpdate("delete from TaskList tl" +
				" where tl.taskRef.id=" + tid +
				" and tl.doingDate>='" + begin + "'");
	}
}
