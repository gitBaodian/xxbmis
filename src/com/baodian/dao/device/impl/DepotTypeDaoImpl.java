package com.baodian.dao.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.device.DepotTypeDao;
import com.baodian.model.device.DepotType;
import com.baodian.util.JSONValue;

@Repository("depotTypeDao")
public class DepotTypeDaoImpl implements DepotTypeDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(DepotType dt) {
		ht.save(dt);
	}
//r
	@SuppressWarnings("unchecked")
	public List<DepotType> getDts() {
		return ht.find("from DepotType dt order by dt.sort");
	}
	@SuppressWarnings("unchecked")
	public String getDtsOnStr() {
		List<DepotType> dts = ht.find("select new DepotType(dt.id, dt.name)" +
				"from DepotType dt order by dt.sort");
		if(dts.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(DepotType dt : dts) {
			json.append("{\"id\":" + dt.getId() +
				",\"name\":\"" + JSONValue.escapeHTML(dt.getName()) + "\"},");
		}
		return json.substring(0, json.length()-1) + ']';
	}
//u
	public void update(DepotType dt) {
		ht.update(dt);
		//ht.bulkUpdate("update DepotType dt set dt.name=?, dt.sort=" +
			//	dt.getSort() + " where dt.id=" + dt.getId(), dt.getName());
	}
//d
	public void delete(String id) {
		ht.bulkUpdate("delete from DepotType dt where dt.id=" + id);
	}
}
