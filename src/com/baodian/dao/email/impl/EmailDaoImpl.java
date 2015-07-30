package com.baodian.dao.email.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.email.EmailDao;
import com.baodian.model.email.Email;
import com.baodian.util.page.EmailPage;

@Repository("emailDao")
public class EmailDaoImpl implements EmailDao {

	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Email email) {
		ht.save(email);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Email> getEms(final EmailPage page) {
		return ht.executeFind(
			new HibernateCallback<List<Email>>() {
				public List<Email> doInHibernate(Session s) throws HibernateException, SQLException {
					StringBuilder sql = new StringBuilder();
					sql.append("e.addressor.id=" + page.getUid());
					switch(page.getEmailst()) {
						case -2: sql.append(" and e.emailst=2"); break;
						default: sql.append(" and e.emailst=1");
					}
					page.countPage(((Long) s.createQuery("select count(*) " +
							"from Email e where " + sql.toString()).list().get(0)).intValue());
					if(page.getCountNums() == 0) return null;
					switch(page.getSortst()) {
						case -1: sql.append(" order by e.id"); break;
						default: sql.append(" order by e.id desc");
					}
					List<Email> result = s.createQuery("select new Email(" +
							"e.id, e.title, e.date, e.sendst, e.emailst)" +
							"from Email e where " + sql.toString())
							.setFirstResult(page.getFirstNum())
							.setMaxResults(page.getNum())
							.list();
					return result;
				}
			}
		);
	}
	@SuppressWarnings("unchecked")
	public Email getEm(int id) {
		List<Email> es = ht.find("select new Email(" +
				"e.id, e.title, e.content, e.date, e.sendst, e.emailst," +
				"u.id, u.name, d.id, d.name)from Email e, User u, Department d " +
				"where e.id=? and e.addressor.id=u.id and u.dpm.id=d.id", id);
		if(es.size()==0) return null;
		return es.get(0);
	}
	public boolean getCDelete(int eid) {
		return (Long) ht.find("select count(*) from Email e where e.id=" +
				eid + " and e.emailst!=3").get(0) == 0;
	}
//u
	private String[] str = {"sendst", "emailst"};
	public void updateState(int uid, int eid, int state, int st) {
		ht.bulkUpdate("update Email e set e." + str[state] + "=" + st +
				" where e.id=" + eid + (uid==0 ? "" : " and e.addressor.id=" + uid));
	}
//d
	public void delete(int eid) {
		ht.bulkUpdate("delete from Email e where e.id=" + eid);
	}
}
