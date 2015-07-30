package com.baodian.dao.duty.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.duty.DutyDao;
import com.baodian.model.duty.Duty_Dept;

@Repository("dutyDao")
public class DutyDaoImpl implements DutyDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Duty_Dept dd) {
		ht.save(dd);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Duty_Dept> getAll() {
		return ht.find("from Duty_Dept");
	}
//u
//d
	public void deleteAll() {
		ht.bulkUpdate("delete from Duty_Dept");
	}
}
