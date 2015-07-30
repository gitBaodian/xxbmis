package com.baodian.dao.user.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.user.UserDao;
import com.baodian.model.user.User;
import com.baodian.util.page.UserPage;

@Repository("userDao")
public class UserDaoImpl implements UserDao {

	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(User user) {
		ht.save(user);
	}
//r
	@SuppressWarnings("unchecked")
	public List<User> getU_inadOnPage(final UserPage page) {
		final StringBuilder sql = new StringBuilder();
		if(page.getDpmId() != 0) {
			sql.append(" and u.dpm.id=" + page.getDpmId());
		}
		if(page.getName()!=null && page.getName().length()!=0) {
			sql.append(" and u.name like '" + page.getName().replace("'", "") + "%'");
		}
		if(page.getAccount()!=null && page.getAccount().length()!=0) {
			sql.append(" and u.account like '" + page.getAccount().replace("'", "") + "%'");
		}
		if(sql.length() != 0)
			sql.replace(0, 4, "where");
		long nums = (Long) ht.find("select count(*) from User u " + sql.toString()).get(0);
		page.countPage((int) nums);
		if(nums == 0) {
			return Collections.emptyList();
		}
		if(sql.length() != 0)
			sql.append(" and u.dpm.id=d.id");
		else
			sql.append("where u.dpm.id=d.id");
		return ht.executeFind(
			new HibernateCallback<List<User>>() {
				public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
					List<User> result = s.createQuery("select new User(u.id, u.name, u.account, d.id, d.name)" +
							"from User u, Department d " + sql.toString() + " order by d.id")
						.setFirstResult(page.getFirstNum())
						.setMaxResults(page.getNum())
						.list();
					return result;
				}
			}
		);
	}
	@SuppressWarnings("unchecked")
	public User getU_inadById(int id) {
		List<User> users = ht.find("select new User(u.id, u.name, u.account, d.id, d.name) " +
				"from User u, Department d where u.id=?", id);
		if(users.size() > 0)
			return users.get(0);
		else
			return null;
	}
	@SuppressWarnings("unchecked")
	public String getU_passByAccount(String account) {
		List<String> strs = ht.find("select u.password from User u where u.account=?", account);
		if(strs.size() > 0)
			return strs.get(0);
		else
			return null;
	}
	public int getU_NumbyD_id(int id) {
		return ((Long) ht.find("select count(*) " +
				"from User u where u.dpm.id=?", id).get(0)).intValue();
	}
	public User getUserByU_a(String account) {
		return (User) ht.find("from User u where u.account=?", account).get(0);
	}
	@SuppressWarnings("unchecked")
	public int getU_NumByAc(String account, int uId) {
		List<Integer> uids = ht.find("select u.id " +
				"from User u where u.account=?", account);
		if(uids.size() > 0) {
			if(uId == uids.get(0)) {
				return 0;
			} else {
				return uids.size();
			}
		} else {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	public List<User> getU_ByDids(String dids) {
		return ht.find("select new User(u.id, u.name, u.dpm.id)" +
				"from User u where u.dpm.id in(" + dids + ")");
	}
	@SuppressWarnings("unchecked")
	public List<User> getU_ByDid(int did) {
		return ht.find("select new User(u.id, u.name)" +
				"from User u where u.dpm.id=" + did);
	}
	@SuppressWarnings("unchecked")
	public boolean chkExit(int uid) {
		List<Integer> uids = ht.find("select u.id from User u where u.id=" + uid);
		return uids.size()==0? false: true;
	}
//u
	public void update(User user, int depmId) {
		String sql = "update User u set ";
		if(!user.getName().isEmpty()) {
			sql = sql.concat("u.name='" + user.getName() + "',");
		}
		if(!user.getAccount().isEmpty()) {
			sql = sql.concat("u.account='" + user.getAccount() + "'," +
					"u.password='" + user.getPassword() + "',");
		}
		if(depmId != -1) {
			sql = sql.concat("u.dpm.id=" + depmId + ",");
		}
		if(!sql.equals("update User u set ")) {
			sql = sql.substring(0, sql.length()-1)
					.concat(" where u.id=" + user.getId());
			ht.bulkUpdate(sql);
		}
	}
	public void updatePW(User user) {
		ht.bulkUpdate("update User u set u.password=? where u.account=?", user.getPassword(), user.getAccount());
	}
	public void updateDpm(int uid, int did) {
		ht.bulkUpdate("update User u set u.dpm.id=" + did + " where u.id=" + uid);
	}
//d
	public void delete(User user) {
		ht.delete(user);
	}
	public User getUserByName(String username) {
		return (User) ht.find("from User where name=?",username).get(0);
	}
	@SuppressWarnings("unchecked")
	public List<String> getAllUserName() {
		List<String> usernames = ht.find("select u.name from User u");
		return usernames;
	}

}
