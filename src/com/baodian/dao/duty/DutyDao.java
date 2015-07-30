package com.baodian.dao.duty;

import java.util.List;

import com.baodian.model.duty.Duty_Dept;

public interface DutyDao {

//c
	/**
	 * 添加 值班-部门
	 */
	public void save(Duty_Dept dd);
//r
	/**
	 * 读取全部 值班-部门
	 */
	public List<Duty_Dept> getAll();
//d
	/**
	 * 删除全部 值班-部门
	 */
	public void deleteAll();
}
