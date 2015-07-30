package com.baodian.model.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="f_tasklist")
public class TaskList {
	private int id;
	private Date taskDate;
	private Date doingDate;
	private Date doneDate;
	private int status;
	private int changetime;//标记提醒时间是否更改，防止自动添加任务出错
	private String user;
	private String department;
	private Task taskRef;
	public TaskList() {}
	//TaskDao.getTaskListByPage
	public TaskList(int id, Date taskDate, Date doingDate, Date doneDate,
			int status, String user, String department,
			int tId, String title, String content) {
		this.id = id;
		this.taskDate = taskDate;
		this.doingDate = doingDate;
		this.doneDate = doneDate;
		this.status = status;
		this.user = user;
		this.department = department;
		this.taskRef = new Task(tId, title, content);
	}
	//TaskDao.getNeedDoTask
	public TaskList(int id, Date doingDate) {
		this.id = id;
		this.doingDate = doingDate;
	}
	//TaskManager.run
    public TaskList(int status, int changetime, Date taskDate, Task task) {
		this.status = status;
		this.changetime = changetime;
		this.taskDate = taskDate;
		this.taskRef = task;
	}
	//set get
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable=false)
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	@Column(nullable=false)
	public Date getDoingDate() {
		return doingDate;
	}
	public void setDoingDate(Date doingDate) {
		this.doingDate = doingDate;
	}
	@Column
	public Date getDoneDate() {
		return doneDate;
	}
	public void setDoneDate(Date doneDate) {
		this.doneDate = doneDate;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Column
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="task_id", nullable=false)
	public Task getTaskRef() {
		return taskRef;
	}
	public void setTaskRef(Task taskRef) {
		this.taskRef = taskRef;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getChangetime() {
		return changetime;
	}
	public void setChangetime(int changetime) {
		this.changetime = changetime;
	}
	
}
