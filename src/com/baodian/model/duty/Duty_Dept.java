package com.baodian.model.duty;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.Department;

@Entity
@IdClass(Duty_DeptPK.class)
@Table(name="f_duty_dept")
//值班-部门
public class Duty_Dept {
	private Duty duty;
	private Department dept;
	
	public Duty_Dept() {}
	
	public Duty_Dept(int i, int j) {
		this.duty = new Duty(i);
		this.dept = new Department(j);
	}

	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Duty getDuty() {
		return duty;
	}
	public void setDuty(Duty duty) {
		this.duty = duty;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Department getDept() {
		return dept;
	}
	public void setDept(Department dept) {
		this.dept = dept;
	}
	
}
