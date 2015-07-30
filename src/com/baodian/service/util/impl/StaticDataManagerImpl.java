package com.baodian.service.util.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.duty.DutyDao;
import com.baodian.dao.user.DepartmentDao;
import com.baodian.model.duty.Duty_Dept;
import com.baodian.model.user.Department;
import com.baodian.service.util.StaticDataManager;

@Service("staticData")
//利用sping保存经常读，但又不经常变化的
public class StaticDataManagerImpl implements StaticDataManager {
	private DutyDao dutyDao;
	@Resource(name="dutyDao")
	public void setDutyDao(DutyDao dutyDao) {
		this.dutyDao = dutyDao;
	}
	private DepartmentDao departmentDao;
	@Resource(name="departmentDao")
	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}
//init
	@PostConstruct
	public void init() {
		int a;
		//部门
		List<Department> dps = departmentDao.getAll();
		for(Department dpm : dps) {
			depts.put(dpm.getId(), dpm);
			dpmIndex.add(dpm.getId());
		}
		//子部门
		Iterator<Department> it = depts.values().iterator();
	    while(it.hasNext()) {
	    	Department dept = it.next();
	    	if(dept.getParent() != null)
		    	a = dept.getParent().getId();
	    	else a = 0;//根节点
	    	if(dchildren.containsKey(a)) {
	    		dchildren.get(a).add(dept.getId());
	    	} else {
	    		List<Integer> ids = new ArrayList<Integer>();
	    		ids.add(dept.getId());
	    		dchildren.put(a, ids);
	    	}
		}
		//值班部门
		List<Duty_Dept> dds = dutyDao.getAll();
		duty_depts.clear();
		for(int i=0; i<dds.size(); i++) {
			a = dds.get(i).getDept().getId();
			duty_depts.put(a, dds.get(i).getDuty().getId()-1);
			//值班父队列
			if(depts.get(a).getParent() != null) {
				dtParent.add(depts.get(a).getParent().getId());
			}
		}
		System.out.println("***初始化StaticData(静态数据)成功***");
	}
//read
	public String findDuty() {
		StringBuilder json = new StringBuilder();
		json.append('[');
		String[] dmpOnDuty =  new String[] {"", "", "", "", "", "", "", ""};
		for(Entry<Integer, Integer> entry : duty_depts.entrySet()) {
			dmpOnDuty[entry.getValue()] = dmpOnDuty[entry.getValue()].concat(entry.getKey() + ",");
		}
		String str;
		for(int i=0; i<dmpOnDuty.length; i++) {
			str = dmpOnDuty[i];
			if(str.isEmpty()) json.append("{\"dpm\":[]},");
			else json.append("{\"dpm\":[" + str.substring(0, str.length()-1)  + "]},");
		}
		if(dmpOnDuty.length > 0) {
			return json.substring(0, json.length()-1) + ']';
		} else {
			return "[]";
		}
	}
	@SuppressWarnings("unchecked")
	public String findDept() {
		if(depts.size() == 0) {
			return "[]";
		}
		JSONArray array = new JSONArray();
		Department dept;
		for(int did : dpmIndex) {
			dept = depts.get(did);
			JSONObject deptJSON = new JSONObject();
			deptJSON.put("id", dept.getId());
			deptJSON.put("name", dept.getName());
			deptJSON.put("sort", dept.getSort());
			deptJSON.put("open", true);
			if(dept.getParent() != null) {
				deptJSON.put("pId", dept.getParent().getId());
			}
			array.add(deptJSON);
		}
		return array.toString();
		
	}
	public void reload() {
		duty_depts.clear();
		dtParent.clear();
		depts.clear();
		dpmIndex.clear();
		dchildren.clear();
		this.init();
	}
//update
	public void changeDtPa() {
		dtParent.clear();
	    for(int dId : duty_depts.keySet()) {
	    	Department dpm = depts.get(dId).getParent();
	    	if(dpm != null) dtParent.add(dpm.getId());
	    }
	}
//output
	public void output() {
		System.out.println("值班部门表 => duty_depts" );
		System.out.println(findDuty());
		System.out.println("值班父队列 => dtParent");
		for(int i : dtParent) {
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println("部门 => depts");
		System.out.println(findDept());
		System.out.println("子部门 => dchildren");
		for(Entry<Integer, List<Integer>> settt : dchildren.entrySet()) {
	    	List<Integer> iiii = settt.getValue();
	    	System.out.print(settt.getKey() + " => ");
	    	for(int i=0;i<iiii.size();i++)
	    		System.out.print(iiii.get(i) + " ");
	    	System.out.println();
		}
	}
}
