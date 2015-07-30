package com.baodian.service.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.baodian.model.user.Department;

public interface StaticDataManager {
//static data
	/**
	 * 值班部门表，部门->值数(从0开始)
	 */
	public Map<Integer, Integer> duty_depts = new HashMap<Integer, Integer>();
	/**
	 * 值班父队列
	 */
	public Set<Integer> dtParent = new HashSet<Integer>();
	/**
	 * 部门，使用get(did)返回这个部门Department
	 */
	public Map<Integer, Department> depts = new HashMap<Integer, Department>();
	/**
	 * 部门顺序
	 */
	public List<Integer> dpmIndex = new ArrayList<Integer>();
	/**
	 * 子部门,使用get(did)返回其子部门,无子部门返回为null,get(0)返回根节点
	 */
	public Map<Integer, List<Integer>> dchildren = new HashMap<Integer, List<Integer>>();
//read
	/**
	 * 返回所有值班部门
	 * @return [{dpm:[id1,id2]}*4];
	 */
	public String findDuty();
	/**
	 * 返回所有部门
	 * @return [{id,name,sort,open,pId}]
	 */
	public String findDept();
	/**
	 * 重新读取数据
	 */
	public void reload();
//update
	/**
	 * 更新 值班父队列dtParent
	 */
	public void changeDtPa();
//o
	public void output();
}
