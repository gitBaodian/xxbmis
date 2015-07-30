package com.baodian.dao.news.impl;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.news.NewsreplyDao;
import com.baodian.model.user.User;
import com.baodian.model.news.Newsreply;

@Repository("newsreplyDao")
public class NewsreplyDaoImpl implements NewsreplyDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Newsreply newsreply) {
		ht.save(newsreply);
	}
//r
	@SuppressWarnings("unchecked")
	public List<Newsreply> getNrsUByNbId(final int nbId, final int begin, final int num) {
		return ht.executeFind(
				new HibernateCallback<List<User>>() {
					public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
						List<User> result = s.createQuery(" select new Newsreply(nr.id, nr.reptime, nr.content," +
								"u.id, u.name, u.date, u.himage, d.id, d.name)" +
								"from Newsreply nr, User u, Department d " +
								"where nr.nbase.id=" + nbId +
								" and nr.author.id=u.id and u.dpm.id=d.id" +
								" order by nr.id")
							.setFirstResult(begin)
							.setMaxResults(num)
							.list();
						return result;
					}
				}
			);
	}
	@SuppressWarnings("unchecked")
	public Newsreply getNrUByNrId(int nrId) {
		List<Newsreply> nrs = ht.find("select new Newsreply(nr.id, nr.reptime, nr.content, nr.nbase.id," +
				"u.id, u.name, u.date, u.himage, d.id, d.name)" +
				"from Newsreply nr, User u, Department d " +
				"where nr.id=" + nrId +
				" and nr.author.id=u.id and u.dpm.id=d.id");
		if(nrs.size()!=0)
			return nrs.get(0);
		else
			return null;
	}
	public int getNbUid_byNr_id(int id) {
		try {
			return (Integer) ht.find("select nb.author.id from Newsreply nr, Newsbase nb " +
					"where nr.id=? and nr.nbase.id=nb.id", id).get(0);
		} catch(NullPointerException e) {
			return 0;
		}
	}
	public int getNrUid_byNr_id(int id) {
		try {
			return (Integer) ht.find("select nr.author.id from Newsreply nr " +
					"where nr.id=? ", id).get(0);
		} catch(NullPointerException e) {
			return 0;
		}
	}
//u
	public void update(Newsreply newsreply) {
		ht.bulkUpdate("update Newsreply set content=? " +
				"where id=?", newsreply.getContent(), newsreply.getId());
	}
//d
	public void delete(Newsreply newsreply) {
		ht.bulkUpdate("update Newsreply " +
				"set content='<span style=\"color:#666666;\">内容已删除！</span>' " +
				"where id=?", newsreply.getId());
		//ht.delete(newsreply);
	}
}
