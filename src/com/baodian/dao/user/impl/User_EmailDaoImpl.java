package com.baodian.dao.user.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.user.User_EmailDao;
import com.baodian.model.user.User;
import com.baodian.model.user.User_Email;
import com.baodian.util.page.EmailPage;

@Repository("user_EmailDao")
public class User_EmailDaoImpl implements User_EmailDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(User_Email user_Email) {
		ht.save(user_Email);
	}
//r
	@SuppressWarnings("unchecked")
	public List<User_Email> getE_Us(final EmailPage page) {
		return ht.executeFind(
			new HibernateCallback<List<User_Email>>() {
				public List<User_Email> doInHibernate(Session s) throws HibernateException, SQLException {
					StringBuilder sql = new StringBuilder();
					sql.append("ue.user.id=" + page.getUid());
					switch(page.getEmailst()) {
						case 2: sql.append(" and ue.readst=1 and ue.emailst=1"); break;
						case 3: sql.append(" and ue.emailst=2"); break;
						default: sql.append(" and ue.emailst=1");
					}
					page.countPage(((Long) s.createQuery("select count(*) " +
							"from User_Email ue where " + sql.toString()).list().get(0)).intValue());
					if(page.getCountNums() == 0) return null;
					sql.append(" and ue.email.id=e.id and e.addressor.id=u.id and u.dpm.id=d.id ");
					switch(page.getSortst()) {
						case -1: sql.append("order by ue.date"); break;
						case -2: sql.append("order by e.addressor.id,ue.date desc"); break;
						case 2: sql.append("order by e.addressor.id desc,ue.date desc"); break;
						default: sql.append("order by ue.date desc");
					}
					List<User_Email> result = s.createQuery("select new User_Email(" +
							"ue.email.id, ue.date, ue.readst, ue.recest, ue.emailst," +
							"e.title, e.addressor.id, u.name, d.name)" +
							"from User_Email ue, Email e, User u, Department d " +
							"where " + sql.toString())
							.setFirstResult(page.getFirstNum())
							.setMaxResults(page.getNum())
							.list();
					return result;
				}
			}
		);
	}
	@SuppressWarnings("unchecked")
	public List<User_Email> getE_Us(int uid, String ot) {
		return ht.find("select new User_Email(" +
			"ue.email.id, ue.date, ue.readst, ue.recest, ue.emailst," +
			"e.title, e.addressor.id, u.name, d.name)" +
			"from User_Email ue, Email e, User u, Department d " +
			"where ue.user.id=" + uid +
			" and ue.date>='" + ot + "' and ue.email.id=e.id" +
			" and e.addressor.id=u.id and u.dpm.id=d.id order by ue.date");
	}
	public int getUnReadByUId(int uid) {
		return ((Long) ht.find("select count(*) " +
				"from User_Email ue where ue.user.id=" + uid +
				" and ue.readst=1 and ue.emailst=1").get(0)).intValue();
	}
	public boolean getCDelete(int uid, int eid) {
		return (Long) ht.find("select count(ue.email.id) " +
				"from User_Email ue where ue.email.id=" + eid +
				(uid==0 ? "" : " and ue.user.id!=" + uid) +
				" and ue.emailst!=3").get(0) == 0;
	}
	@SuppressWarnings("unchecked")
	public List<User> get3UsByEId(final int id) {
		return ht.executeFind(
			new HibernateCallback<List<User>>() {
				public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
					return s.createQuery("select new User(u.id, u.name)" +
							"from User_Email ue, User u " +
							"where ue.email.id=" + id + " and ue.user.id=u.id")
							.setMaxResults(3)
							.list();
				}
			}
		);
	}
	@SuppressWarnings("unchecked")
	public List<User_Email> getE_UsByEId(int id) {
		return ht.find("select new User_Email(" +
				"ue.date, ue.readst, ue.recest, ue.emailst, u.id, u.name, d.id, d.name)" +
				"from User_Email ue, User u, Department d " +
				"where ue.email.id=" + id + " and ue.user.id=u.id and u.dpm.id=d.id");
	}
	public int getUnum(int uid) {
		return ((Long) ht.find("select count(*) from User u where u.id=" + uid).get(0)).intValue();
	}
//u
	private String[] str = {"readst", "emailst", "recest"};
	public void updateState(int uid, int eid, int state, int st) {
		ht.bulkUpdate("update User_Email ue set ue." + str[state] + "=" + st +
				" where ue.user.id=" + uid + " and ue.email.id=" + eid);
	}
	public void updateUr(int uid, int emailst) {
		ht.bulkUpdate("update User_Email ue set ue.readst=2"+
				" where ue.user.id=" + uid +
				" and ue.readst=1 and ue.emailst=" + emailst);
	}
//d
	public void delete(int eid) {
		ht.bulkUpdate("delete from User_Email ue where ue.email.id=" + eid);
	}
}
