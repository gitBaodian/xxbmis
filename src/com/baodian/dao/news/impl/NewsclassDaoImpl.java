package com.baodian.dao.news.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.news.NewsclassDao;
import com.baodian.model.news.Newsclass;

@Repository("newsclassDao")
public class NewsclassDaoImpl implements NewsclassDao {

	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
	
//c
	public void save(Newsclass newsclass) {
		newsclass.setName(newsclass.getName().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		ht.save(newsclass);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Newsclass> getNcs() {
		return ht.find("from Newsclass nc order by nc.position");
	}
	public int getCountByNcId(int ncId) {
		long nums = (Long) ht.find("select count(*) from Newsbase nb where nb.nclass.id=?", ncId).get(0);
		return (int) nums;
	}
	@SuppressWarnings("unchecked")
	public List<Newsclass> getNcsOnDi() {
		return ht.find("select new Newsclass(nc.id, nc.name) from Newsclass nc " +
				"where nc.display=1 order by nc.position");
	}
	public int getNc_stById(int id) {
		try {
			return (Integer) ht.find("select nc.review from Newsclass nc where nc.id=?", id).get(0);
		} catch(NullPointerException e) {
			return 0;
		}
	}
//u
	public void update(Newsclass newsclass) {
		String hql = "update Newsclass set ";
		if(newsclass.getName() != null)
			hql = hql + "name='" + newsclass.getName()
					.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", "") + "',";
		hql = hql + "position=" + newsclass.getPosition() +
				",display=" + newsclass.getDisplay() +
				",review=" + newsclass.getReview() + " where id=?";
		ht.bulkUpdate(hql, newsclass.getId());
	}
//d
	public void delete(Newsclass newsclass) {
		ht.delete(newsclass);
	}
}
