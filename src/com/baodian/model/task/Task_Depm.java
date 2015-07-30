package com.baodian.model.task;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.Department;


@Entity
@IdClass(Task_DepmPK.class)
@Table(name="f_task_depm")
public class Task_Depm {
	private Task task;
	private Department depm;
	public Task_Depm() {}
	public Task_Depm(int taskId, int depmId) {
		this.task = new Task(taskId);
		this.depm = new Department(depmId);
	}
	public Task_Depm(int taskId, int depmId, String dpmName) {
		this.task = new Task(taskId);
		this.depm = new Department(depmId, dpmName);
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Department getDepm() {
		return depm;
	}
	public void setDepm(Department depm) {
		this.depm = depm;
	}
}
