package com.baodian.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.task.Task;
import com.baodian.service.task.TaskManager;
import com.baodian.util.page.TaskPage;

@SuppressWarnings("serial")
@Component("task")
@Scope("prototype")//必须注解为多态
public class TaskAction extends CommonAction {
	@Resource(name="taskManager")
	private TaskManager tm;
	private TaskPage page;
	private Task task;

//c
	public String add_js() {
		json = tm.save(task, json);
		return JSON;
	}
//r
	public String set() {
		json = tm.findDepms();
		return SUCCESS;
	}
	public String set_js() {
		if(page == null) {
			page = new TaskPage();
		}
		json = tm.findTaskSetByPage(page);
		return JSON;
	}
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		if(page == null) {
			page = new TaskPage();
		}
		json = tm.findTaskListByPage(page);
		return JSON;
	}
	/**
	 * 获取需要完成的任务
	 */
	public String need_js() {
		json = tm.findNeedDoTask().toString();
		return JSON;
	}
	/**
	 * 指定日期生成任务列表
	 */
	public String create_js() {
		json = tm.createTaskList(json);
		return JSON;
	}
//u
	public String change_js() {
		json = tm.change(task, json);
		return JSON;
	}
	/**
	 * 更改提醒时间
	 */
	public String changeDoingDate_js() {
		json = tm.changeDoingDate(json);
		return JSON;
	}
	/**
	 * 标记任务完成状态
	 */
	public String changeStatus_js() {
		json = tm.changeStatus(json);
		return JSON;
	}
//d
	public String remove_js() {
		json = tm.remove(json);
		return JSON;
	}
	public String removeList_js() {
		json = tm.removeList(json);
		return JSON;
	}
//set get
	public TaskManager getTm() {
		return tm;
	}
	public void setTm(TaskManager tm) {
		this.tm = tm;
	}
	public TaskPage getPage() {
		return page;
	}
	public void setPage(TaskPage page) {
		this.page = page;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	
}
