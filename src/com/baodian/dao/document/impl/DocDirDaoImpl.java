package com.baodian.dao.document.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.document.DocDirDao;
import com.baodian.model.document.DocDir;
import com.baodian.util.JSONValue;

@Repository("docDirDao")
public class DocDirDaoImpl implements DocDirDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(DocDir d) {
		ht.save(d);
	}
//r
	@SuppressWarnings("unchecked")
	public String getDirs(int id) {
		List<DocDir> dirs = ht.find("select new DocDir(d.id, d.name, d.sort, d.parent.id)" +
				"from DocDir d where d.owner.id=" + (id==0? "null": id) +
				" order by d.sort");
		if(dirs.size() == 0) {
			return "[]";
		}
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(DocDir d : dirs) {
			json.append("{\"id\":" + d.getId() +
					",\"name\":\"" + JSONValue.escape(d.getName()) +
					"\",\"sort\":" + d.getSort() + 
					",\"open\":true");
			if(d.getParent() == null) {
				json.append("},");
			} else {
				json.append(",\"pId\":" + d.getParent().getId() + "},");
			}
		}
		return json.substring(0, json.length()-1) + ']';
	}
//u
	public void updateName(DocDir d) {
		ht.bulkUpdate("update DocDir d set d.name=? where d.id=?", d.getName(), d.getId());
	}
	public void updateSort(int id, int pid, int sort) {
		StringBuilder sql = new StringBuilder();
		if(pid != -1) {
			if(pid == 0)
				sql.append(",d.parent.id=null");
			else
				sql.append(",d.parent.id=" + pid);
		}
		if(sort != -1)
			sql.append(",d.sort=" + sort);
		if(sql.length() != 0) {
			ht.bulkUpdate("update DocDir d set " + sql.substring(1, sql.length()) +
					" where d.id=" + id);
		}
	}
//d
	public Long findChildNum(String id) {
		return (Long) ht.find("select count(*) from DocDir d where d.parent.id=" + id).get(0);
	}
	public Long findDocNum(String id) {
		return (Long) ht.find("select count(*) from Document d where d.dir.id=" + id).get(0);
	}
	public void delete(String id) {
		ht.bulkUpdate("delete from DocDir d where d.id=" + id);
	}
}
