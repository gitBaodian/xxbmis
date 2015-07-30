package com.baodian.model.duty;

import java.io.Serializable;

import com.baodian.model.user.Department;

@SuppressWarnings("serial")
public class Duty_DeptPK implements Serializable {
	private Duty duty;
	private Department dept;
	public Duty getDuty() {
		return duty;
	}
	public void setDuty(Duty duty) {
		this.duty = duty;
	}
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	@Override
	public boolean equals(Object o) {//判断逻辑属性和物理属性是否相同
		if(o instanceof Duty_DeptPK) {
			Duty_DeptPK pk = (Duty_DeptPK)o;
			if(this.duty.getId() == pk.duty.getId() && this.dept.getId() == pk.dept.getId()) {
				return true;
			}
		}
		return false;
	}
	@Override
	public int hashCode() {//方便查找(主键)对应的对象
		return this.duty.hashCode() + this.dept.hashCode();
	}
}