package com.baodian.dao.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.device.GoodsDao;
import com.baodian.model.device.Goods;
import com.baodian.util.JSONValue;

@Repository("goodsDao")
public class GoodsDaoImpl implements GoodsDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Goods g) {
		ht.save(g);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Goods> getGoods() {
		return ht.find("from Goods g order by g.sort");
	}
	@SuppressWarnings("unchecked")
	public String getGds() {
		List<Goods> gds = ht.find("select new Goods(g.id, g.name, g.parent.id) " +
				"from Goods g order by g.sort");
		if(gds.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(Goods gd : gds) {
			json.append("{\"id\":" + gd.getId() +
				",\"name\":\"" + JSONValue.escapeHTML(gd.getName()) + (gd.getParent()!=null?
				"\",\"pId\":" + gd.getParent().getId() + "},": "\"},"));
		}
		return json.substring(0, json.length()-1) + ']';
	}
//u
	public void updateND(Goods goods) {
		ht.bulkUpdate("update Goods g set g.name=?,g.detail=? " +
				"where g.id=" + goods.getId(), goods.getName(), goods.getDetail());
	}
	public void updateSort(int id, int pid, int sort) {
		StringBuilder sql = new StringBuilder();
		if(pid != -1) {
			if(pid == 0)
				sql.append(",g.parent.id=null");
			else
				sql.append(",g.parent.id=" + pid);
		}
		if(sort != -1)
			sql.append(",g.sort=" + sort);
		if(sql.length() != 0) {
			ht.bulkUpdate("update Goods g set " + sql.substring(1, sql.length()) +
					" where g.id=" + id);
		}
	}
//d
	public void delete(int gid) {
		ht.bulkUpdate("delete from Goods g where g.id=" + gid);
	}
}
