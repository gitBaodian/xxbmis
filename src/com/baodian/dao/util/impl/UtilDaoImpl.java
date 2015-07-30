package com.baodian.dao.util.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.util.UtilDao;
import com.baodian.model.user.User;
import com.baodian.util.page.Page;

@Repository("utilDao")
public abstract class UtilDaoImpl implements UtilDao {

	public HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(Object obj) {
		ht.save(obj);
	}
//r
	@SuppressWarnings("unchecked")
	public boolean chkExit(int id, String obj) {
		List<Integer> objs = ht.find("select obj.id from " + obj + " obj where obj.id=" + id);
		return objs.size()==0? false: true;
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjsByPage(final String countSql, final String selectSql,
			final Page page, final List<String> params) {
		return ht.executeFind(
				new HibernateCallback<List<User>>() {
					public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
						Query query = s.createQuery(countSql);
						for(int i=0; i<params.size(); i++) {
							query.setParameter(i, params.get(i));
						}
						page.countPage(((Long) query.list().get(0)).intValue());
						if(page.getCountNums() == 0) {
							return Collections.emptyList();
						}
						
						query = s.createQuery(selectSql)
							.setFirstResult(page.getFirstNum())
							.setMaxResults(page.getNum());
						for(int i=0; i<params.size(); i++) {
							query.setParameter(i, params.get(i));
						}
						return query.list();
					}
				}
			);
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> getObjs(final String sql, final int num) {
		return ht.executeFind(
			new HibernateCallback<List<User>>() {
				public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
					return s.createQuery(sql).setMaxResults(num).list();
				}
			}
		);
	}
//u
	public int bulkUpdate(final String updateSql, final List<String> params) {
		return ht.executeWithNativeSession(new HibernateCallback<Integer>() {
			public Integer doInHibernate(Session session) throws HibernateException {
				Query queryObject = session.createQuery(updateSql);
				if (params != null) {
					for (int i = 0; i < params.size(); i++) {
						queryObject.setParameter(i, params.get(i));
					}
				}
				return queryObject.executeUpdate();
			}
		});
	}
	public void update(Object obj) {
		ht.update(obj);
	}
//d
	public void delete(String id, String obj) {
		bulkUpdate("delete from " + obj + " obj where obj.id=" + id, null);
	}

}
