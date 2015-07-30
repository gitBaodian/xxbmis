package com.baodian.model.task;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="f_task")
public class Task {
	private int id;
	private String title;
	private String content;
	private String user;
	private String department;
	private Date date;
	private int status;//1运行 2停止
	
	/**
	 * 任务类型，提醒时间放到begin上面
	 * 1-每天，例每天16:30(type=1 begin='16:30')
	 * 2-每星期，把星期放到data上，例每星期一、二、三 (type=2 data="1-2-3")
	 * 3-日期范围内，例10月1日~10月7日(type=3 begin='10-01 16:30' end="10-07")
	 */
	private int type;
	private Date begin;//开始时间
	private Date end;//结束时间
	private String data;//辅助数据
	
	public Task() {}
	public Task(int taskId) {
		this.id = taskId;
	}
	public Task(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
	//TaskDao.getTaskSet
	public Task(int id, Date begin, Date end, String data, int type) {
		this.id = id;
		this.begin = begin;
		this.end = end;
		this.data = data;
		this.type = type;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(columnDefinition="text", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(nullable=false, updatable=false)
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Column(nullable=false, updatable=false)
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	@Column(nullable=false, updatable=false)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Column(nullable=false)
	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	@Column(nullable=false)
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	@Column(nullable=false)
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
