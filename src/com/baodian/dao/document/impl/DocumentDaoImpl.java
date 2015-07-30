package com.baodian.dao.document.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.document.DocumentDao;
import com.baodian.model.document.Document;

@Repository("documentDao")
public class DocumentDaoImpl implements DocumentDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Document doc) {
		ht.save(doc);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Document> getDocs(String dirIds) {
		return ht.find("select new Document(d.id, d.name, d.date, d.dir.id)" +
				"from Document d where d.dir.id in(" + dirIds +
				")order by d.name");
	}
	@SuppressWarnings("unchecked")
	public Document getDoc(String docId) {
		List<Document> docs = ht.find("select new Document(d.id, d.name, d.date," +
				"dir.id, dir.name, dir.owner.id, d.url, d.content, u.id, u.name)" +
				"from Document d, DocDir dir, User u " +
				"where d.id=" + docId + " and d.dir.id=dir.id and d.owner.id=u.id");
		if(docs.size() == 0) {
			return null;
		}
		return docs.get(0);
	}
	/**
	 * 暂时不用全文搜索，like在千级数量的时候，性能仍然不减
	 */
	@SuppressWarnings("unchecked")
	public List<Document> getDocsByN(String owner, String area, String keyword) {
		StringBuilder json = new StringBuilder();
		json.append("select new Document(d.id, d.name, d.date, d.dir.id)" +
				"from Document d where d.dir.id in(" +
				"select dir.id from DocDir dir where dir.owner.id=" + owner + ")");
		int count = 0;
		switch(area.charAt(0)) {
			case '1':
				json.append("and d.name like(?)");
				count = 1;
				break;
			case '2':
				json.append("and d.content like(?)");
				count = 1;
				break;
			default:
				json.append("and (d.name like(?) or d.content like(?))");
				count = 2;
		}
		json.append("order by d.name");
		switch(count) {
			case 1:
				return ht.find(json.toString(), '%' + keyword + '%');
			case 2:
				return ht.find(json.toString(), '%' + keyword + '%', '%' + keyword + '%');
			default:
				return Collections.emptyList();
		}
	}
//u
	public void changeDir(int dirId, int docId) {
		ht.bulkUpdate("update Document d set d.dir.id=" + dirId + " where d.id=" + docId);
	}
	public void updateDoc(Document doc) {
		StringBuilder json = new StringBuilder();
		json.append("update Document d set ");
		List<String> params = new ArrayList<String>();
		if(doc.getName() != null) {
			json.append("d.name=?,");
			params.add(doc.getName());
		}
		if(doc.getUrl() != null) {
			json.append("d.url=?,");
			params.add(doc.getUrl());
		}
		if(doc.getContent() != null) {
			json.append("d.content=?,");
			params.add(doc.getContent());
		}
		if(doc.getDir() != null) {
			json.append("d.dir.id=" + doc.getDir().getId() + ",");
		}
		if(json.length() == 22) {//未更新
			return;
		}
		json.append(" d.date=? where d.id=" + doc.getId());
		switch(params.size()) {
			case 0:
				ht.bulkUpdate(json.toString(), new Date());
				break;
			case 1:
				ht.bulkUpdate(json.toString(), params.get(0), new Date());
				break;
			case 2:
				ht.bulkUpdate(json.toString(), params.get(0), params.get(1), new Date());
				break;
			case 3:
				ht.bulkUpdate(json.toString(), params.get(0), params.get(1), params.get(2), new Date());
				break;
		}
	}
//d
	public void delete(String docId) {
		ht.bulkUpdate("delete from Document d where d.id=" + docId);
	}
}
