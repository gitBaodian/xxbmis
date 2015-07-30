package com.baodian.service.duty.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.duty.DutyDao;
import com.baodian.model.duty.Duty_Dept;
import com.baodian.service.duty.DutyManager;
import com.baodian.service.util.StaticDataManager;

@Service("dutyManager")
public class DutyManagerImpl implements DutyManager {
	private DutyDao dutyDao;
	@Resource(name="dutyDao")
	public void setDutyDao(DutyDao dutyDao) {
		this.dutyDao = dutyDao;
	}
	private StaticDataManager sdata;
	@Resource(name="staticData")
	public void setSdata(StaticDataManager sdata) {
		this.sdata = sdata;
	}
//c
//r
	public String findAll() {
		return "{\"duty\":" + sdata.findDuty()+ 
				",\"dept\":" + sdata.findDept() + "}";
	}
//u
	public void changeAll(String json) {
		dutyDao.deleteAll();
		String[] dts = json.split("A");
		int a = 0;
		StaticDataManager.duty_depts.clear();
		for(int i=0; i<dts.length; i++) {
			for(String s : dts[i].split("a")) {
				try {
					a = Integer.parseInt(s);
					//部门a存在并且不重复&&更新值班部门
					if(StaticDataManager.depts.containsKey(a) &&
							StaticDataManager.duty_depts.put(a, i)==null) {
						dutyDao.save(new Duty_Dept(i+1, a));
					}
				} catch(NumberFormatException e) {}
			}
		}
		//更新值班父队列
		sdata.changeDtPa();
	}
//d
}
