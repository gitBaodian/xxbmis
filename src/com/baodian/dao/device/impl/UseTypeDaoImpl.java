package com.baodian.dao.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.device.UseTypeDao;
import com.baodian.model.device.UseType;
import com.baodian.util.JSONValue;

@Repository("useTypeDao")
public class UseTypeDaoImpl implements UseTypeDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(UseType ut) {
		ht.save(ut);
	}
//r
	@SuppressWarnings("unchecked")
	public String getUts() {
		List<UseType> uts = ht.find("from UseType ut order by ut.sort");
		if(uts.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(UseType ut : uts) {
			json.append("{\"id\":" + ut.getId() +
					",\"name\":\"" + JSONValue.escapeHTML(ut.getName()) +
					"\",\"sort\":" + ut.getSort() +
					",\"gd\":" + ut.getGd().getId() +
					",\"dtin\":" + (ut.getDtin()==null? 0: ut.getDtin().getId()) +
					",\"dtout\":" + (ut.getDtout()==null? 0: ut.getDtout().getId()) + "},");
		}
		return json.substring(0, json.length()-1) + ']';
	}
//u
	public void update(UseType ut) {
		ht.update(ut);
		//ht.bulkUpdate("update UseType ut set ut.name=?, ut.sort=" +
			//	ut.getSort() + " where ut.id=" + ut.getId(), ut.getName());
	}
//d
	public void delete(String id) {
		ht.bulkUpdate("delete from UseType ut where ut.id=" + id);
	}

}
