package com.baodian.model.task;

import java.io.Serializable;

import com.baodian.model.user.Department;

@SuppressWarnings("serial")
public class Task_DepmPK implements Serializable {
	private Task task;
	private Department depm;
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	public Department getDepm() {
		return depm;
	}
	public void setDepm(Department depm) {
		this.depm = depm;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof Task_DepmPK) {
			Task_DepmPK pk = (Task_DepmPK)o;
			if(this.depm.getId() == pk.depm.getId() && this.task.getId() == pk.task.getId()) {
			  return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.task.hashCode() + this.depm.hashCode();
	}
}
