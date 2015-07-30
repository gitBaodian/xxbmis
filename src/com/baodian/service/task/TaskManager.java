package com.baodian.service.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.task.TaskDao;
import com.baodian.model.record.Working_Record;
import com.baodian.model.task.Task;
import com.baodian.model.task.TaskList;
import com.baodian.model.task.Task_Depm;
import com.baodian.model.user.Department;
import com.baodian.model.user.User;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.util.StaticDataManager;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.TaskPage;

@Service("taskManager")
public class TaskManager extends TimerTask {
	//改成static定时器才能使用
	public static TaskDao tdao;
	@Resource(name="taskDao")
	public void setTdao(TaskDao tdao) {
		TaskManager.tdao = tdao;
	}
	private StaticDataManager sdata;
	@Resource(name="staticData")
	public void setSdata(StaticDataManager sdata) {
		this.sdata = sdata;
	}
//c
	/**
	 * 添加任务
	 * @param json 执行部门，id中间用'-'隔开
	 */
	public String save(Task task, String json) {
		if(task==null || StaticMethod.StrSize(json)<1) {
			return StaticMethod.inputError;
		}
		org.springframework.security.core.userdetails.User user = SecuManagerImpl.currentUser();
		if(user == null) {
			return StaticMethod.loginError;
		}
		task.setUser(user.getStr()[0]);
		task.setDepartment(StaticDataManager.depts.get(user.getId()[1]).getName());
		task.setDate(new Date());
		tdao.add(task);
		//生成任务
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		List<Task> tasks = new ArrayList<Task>();
		tasks.add(task);
		addTaskList(cal, tasks);//生成今天任务
		cal.add(Calendar.DATE, 1);
		addTaskList(cal, tasks);//生成明天任务
		//添加部门
		Set<Integer> depmIds = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("-")) {
			try {
				id = Integer.parseInt(s);
				if(depmIds.add(id)) {
					tdao.add(new Task_Depm(task.getId(), id));
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.addSucc;
	}
//r
	/**
	 * 返回部门
	 */
	public String findDepms() {
		return "{\"depms\":" + sdata.findDept() + "}";
	}
	/**
	 * 分页查找任务设置
	 */
	@SuppressWarnings("unchecked")
	public String findTaskSetByPage(TaskPage page) {
		List<Task> tasks = tdao.getTaskSetByPage(page);
		if(tasks.size() == 0) {
			return "{\"total\":0,\"rows\":[]}";
		}
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		Set<Integer> pids = new HashSet<Integer>();
		for(Task task : tasks) {
			pids.add(task.getId());
		}
		Map<Integer, List<Department>> DepmMap = new HashMap<Integer, List<Department>>();
		List<Task_Depm> tds = tdao.getNameByIds(StaticMethod.Array2Str(pids, ","));
		for(Task_Depm depm : tds) {
			if(DepmMap.containsKey(depm.getTask().getId())) {
				DepmMap.get(depm.getTask().getId()).add(depm.getDepm());
				
			} else {
				List<Department> depms = new ArrayList<Department>();
				depms.add(depm.getDepm());
				DepmMap.put(depm.getTask().getId(), depms);
			}
		}
		for(Task task : tasks) {
			JSONObject taskJson = new JSONObject();
			taskJson.put("id", task.getId());
			taskJson.put("title", task.getTitle());
			taskJson.put("content", task.getContent());
			taskJson.put("user", task.getUser());
			taskJson.put("department", task.getDepartment());
			taskJson.put("date", StaticMethod.DateToString(task.getDate()));
			taskJson.put("status", task.getStatus());
			taskJson.put("type", task.getType());
			taskJson.put("begin", StaticMethod.DateToString(task.getBegin()));
			taskJson.put("end", StaticMethod.DateToString(task.getEnd()));
			taskJson.put("data", task.getData());
			if(DepmMap.containsKey(task.getId())) {
				List<Department> depms = DepmMap.get(task.getId());
				JSONArray depmIds = new JSONArray();
				JSONArray depmNames = new JSONArray();
				for(Department demp : depms) {
					depmIds.add(demp.getId());
					depmNames.add(demp.getName());
				}
				taskJson.put("depmIds", depmIds);
				taskJson.put("depmNames", depmNames);
			}
			array.add(taskJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 分页查找任务列表
	 */
	@SuppressWarnings("unchecked")
	public String findTaskListByPage(TaskPage page) {
		List<TaskList> taskLists = tdao.getTaskListByPage(page);
		if(taskLists.size() == 0) {
			return "{\"total\":0,\"rows\":[]}";
		}
		JSONObject json = new JSONObject();
		json.put("total", page.getCountNums());
		JSONArray array = new JSONArray();
		Set<Integer> pids = new HashSet<Integer>();
		for(TaskList task : taskLists) {
			pids.add(task.getTaskRef().getId());
		}
		Map<Integer, List<Department>> DepmMap = new HashMap<Integer, List<Department>>();
		List<Task_Depm> tds = tdao.getNameByIds(StaticMethod.Array2Str(pids, ","));
		for(Task_Depm depm : tds) {
			if(DepmMap.containsKey(depm.getTask().getId())) {
				DepmMap.get(depm.getTask().getId()).add(depm.getDepm());
				
			} else {
				List<Department> depms = new ArrayList<Department>();
				depms.add(depm.getDepm());
				DepmMap.put(depm.getTask().getId(), depms);
			}
		}
		for(TaskList task : taskLists) {
			JSONObject taskJson = new JSONObject();
			taskJson.put("id", task.getId());
			taskJson.put("taskDate", StaticMethod.DateToString(task.getTaskDate()));
			taskJson.put("doingDate", StaticMethod.DateToString(task.getDoingDate()));
			if(task.getDoneDate() != null) {
				taskJson.put("doneDate", StaticMethod.DateToString(task.getDoneDate()));
			}
			taskJson.put("status", task.getStatus());
			taskJson.put("user", task.getUser());
			taskJson.put("department", task.getDepartment());
			taskJson.put("tId", task.getTaskRef().getId());
			taskJson.put("title", task.getTaskRef().getTitle());
			taskJson.put("content", task.getTaskRef().getContent());
			if(DepmMap.containsKey(task.getTaskRef().getId())) {
				List<Department> depms = DepmMap.get(task.getTaskRef().getId());
				JSONArray depmIds = new JSONArray();
				JSONArray depmNames = new JSONArray();
				for(Department demp : depms) {
					depmIds.add(demp.getId());
					depmNames.add(demp.getName());
				}
				taskJson.put("depmIds", depmIds);
				taskJson.put("depmNames", depmNames);
			}
			array.add(taskJson);
		}
		json.put("rows", array);
		return json.toString();
	}
	/**
	 * 获取需要完成的任务
	 */
	@SuppressWarnings("unchecked")
	public JSONObject findNeedDoTask() {
		int dpmId = SecuManagerImpl.currentID(1);
		if(dpmId == 0) {
			return StaticMethod.loginError();
		}
		JSONObject json = new JSONObject();
		List<TaskList> tasks = tdao.getNeedDoTask(dpmId);
		JSONArray array = new JSONArray();
		for(TaskList task : tasks) {
			JSONObject taskJson = new JSONObject();
			taskJson.put("id", task.getId());
			taskJson.put("doingDate", StaticMethod.DateToString(task.getDoingDate()));
			array.add(taskJson);
		}
		json.put("task", array);
		json.put("date", StaticMethod.DateToString(new Date()));
		return json;
	}
	/**
	 * 指定日期生成任务列表
	 */
	public String createTaskList(String json) {
		Calendar cal = Calendar.getInstance();
		try {
			String[] strs = json.split("-");
			cal.set(Integer.parseInt(strs[0]),
					Integer.parseInt(strs[1])-1,
					Integer.parseInt(strs[2]), 0 , 0, 0);
		} catch(Exception e) {
			return StaticMethod.inputError;
		}
		//取出定时任务
		List<Task> tasks = tdao.getTaskSet();
		addTaskList(cal, tasks);
		return StaticMethod.jsonMess(0, "生成任务成功！");
	}
//u
	public String change(Task task, String json) {
		if(task==null) {
			return StaticMethod.inputError;
		}
		tdao.update(task);
		//重新生成任务
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		tdao.deleteTL(task.getId(), StaticMethod.DateToString(cal.getTime()));//删除今天开始的任务
		if(task.getStatus() == 1) {//设置为开启
			List<Task> tasks = new ArrayList<Task>();
			tasks.add(task);
			addTaskList(cal, tasks);//重新生成今天任务
			cal.add(Calendar.DATE, 1);
			addTaskList(cal, tasks);//重新生成明天任务
		}
		//删除相关部门
		tdao.deleteDT(task.getId());
		if(json==null || json.isEmpty()) {
			return StaticMethod.inputError;
		}
		Set<Integer> depmIds = new HashSet<Integer>();
		int id = 0;
		for(String s : json.split("-")) {
			try {
				id = Integer.parseInt(s);
				if(depmIds.add(id)) {
					tdao.add(new Task_Depm(task.getId(), id));
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.changeSucc;
	}
	/**
	 * 更改提醒时间
	 * @param json taskListId A Date
	 */
	public String changeDoingDate(String json) {
		if(StaticMethod.StrSize(json) < 15) {
			return StaticMethod.inputError;
		}
		String[] str = json.split("A");
		if(str.length<2 || StaticMethod.Str2Int(str[0])<1) {
			return StaticMethod.inputError;
		}
		str[1] = StaticMethod.formatDate(str[1]);
		if(str[1] == null) {
			return StaticMethod.inputError;
		}
		tdao.updateDoningDate(str[0], str[1]);
		return StaticMethod.changeSucc;
	}
	/**
	 * 标记任务完成状态
	 * @param json 2/1 A id1-id2 (2A1-2-3)
	 */
	public String changeStatus(String json) {
		if(StaticMethod.StrSize(json) < 3) {
			return StaticMethod.inputError;
		}
		String[] str = json.split("A");
		if(str.length<2) {
			return StaticMethod.inputError;
		}
		if(StaticMethod.Str2Int(str[0]) != 1) {
			str[0] = "2";
		}
		org.springframework.security.core.userdetails.User u = SecuManagerImpl.currentUser();
		if(u == null) {
			return StaticMethod.loginError;
		}
		String user = u.getStr()[0];
		String dpm = StaticDataManager.depts.get(u.getId()[1]).getName();
		Set<String> taskIds = new HashSet<String>();
		for(String s : str[1].split("-")) {
			try {
				Integer.parseInt(s);
				if(taskIds.add(s)) {
					tdao.updateStatus(str[0], s, user, dpm);
					if(str[0].equals("2")) {//添加进运行记录
						String title = tdao.getTitle(s);
						if(title != null) {
							Working_Record wr = new Working_Record();
							wr.setType("任务执行");
							wr.setDetail("完成任务：" + title);
							wr.setTime(new Date());
							wr.setUser(new User(u.getId()[0]));
							tdao.add(wr);
						}
					}
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.changeSucc;
	}
//d	
	public String remove(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<Integer> taskIds = new HashSet<Integer>();
		int id = 0;
		int count = 0;
		for(String s : json.split("-")) {
			try {
				id = Integer.parseInt(s);
				if(taskIds.add(id)) {
					if(tdao.countList(id) != 0) {
						count ++;
					} else {
						tdao.deleteDT(id);
						tdao.delete(s, "Task");
					}
				}
			} catch(NumberFormatException e) {}
		}
		if(count !=0 ) {
			return StaticMethod.jsonMess(0, count + "个任务存在完成日志，未删除！");
		}
		return StaticMethod.removeSucc;
	}
	public String removeList(String json) {
		if(StaticMethod.StrSize(json) < 1) {
			return StaticMethod.inputError;
		}
		Set<String> taskIds = new HashSet<String>();
		for(String s : json.split("-")) {
			try {
				Integer.parseInt(s);
				if(taskIds.add(s)) {
					tdao.delete(s, "TaskList");
				}
			} catch(NumberFormatException e) {}
		}
		return StaticMethod.removeSucc;
	}
//o
	@Override
	public void run() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		//今天凌晨时间
		//String today = StaticMethod.DateToString(cal.getTime());
		cal.add(Calendar.DATE, 2);
		//后台凌晨时间
		String ttomorrow = StaticMethod.DateToString(cal.getTime());
		//先查看后台的任务是否已经生成了
		if(tdao.count2DayList(ttomorrow) == 0) {
			//取出定时任务
			List<Task> tasks = tdao.getTaskSet();
			addTaskList(cal, tasks);
		}
	}
	
	/**
	 * 添加任务到列表
	 * @param cal 更新日期
	 * @tasks 任务列表
	 */
	@SuppressWarnings("deprecation")
	private void addTaskList(Calendar cal, List<Task> tasks) {
		for(Task task : tasks) {
			boolean add = false;
			/**
			 * 任务类型，提醒时间放到begin上面
			 * 1-每天，例每天16:30(type=1 begin='16:30')
			 * 2-每星期，把星期放到data上，例每星期一、二、三 (type=2 data="1-2-3")
			 * 3-日期范围内，例10月1日~10月7日(type=3 begin='10-01 16:30' end="10-07")
			 */
			switch(task.getType()) {
				case 1:
					add = true;
					break;
				case 2:
					//从星期日开始为1，星期六为7
					String week = String.valueOf(cal.get(Calendar.DAY_OF_WEEK) - 1);
					if(week.equals("0")) {
						week = "7";
					}
					if(task.getData().indexOf(week) >= 0) {
						add = true;
					}
					break;
				case 3:
					//忽略小时，只比较日期
					if((cal.getTimeInMillis()-task.getBegin().getTime()+86400000) > 0 &&
						(task.getEnd().getTime()-cal.getTimeInMillis()) >= 0 ) {
						add = true;
					}
					break;
			}
			if(add) {
				cal.set(Calendar.HOUR_OF_DAY, task.getBegin().getHours());
				cal.set(Calendar.MINUTE, task.getBegin().getMinutes());
				TaskList tl = new TaskList(1, 1, new Date(), task);
				tl.setDoingDate(cal.getTime());
				tdao.add(tl);
			}
		}
	}
}
