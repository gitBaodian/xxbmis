package com.baodian.dao.news.impl;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.news.NewsbaseDao;
import com.baodian.model.user.User;
import com.baodian.model.news.Newsbase;

@Repository("newsbaseDao")
public class NewsbaseDaoImpl implements NewsbaseDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void save(Newsbase newsbase) {
		ht.save(newsbase);
	}
//r
	public int countNbsOnSql(String sql) {
		long nums = (Long) ht.find("select count(*) from Newsbase nb " + sql).get(0);
		return (int) nums;
	}
	@SuppressWarnings("unchecked")
	public List<Newsbase> getNbsOnSql(final String sql, final int begin, final int num) {
		return ht.executeFind(
				new HibernateCallback<List<Newsbase>>() {
					public List<Newsbase> doInHibernate(Session s) throws HibernateException, SQLException {
						String str;
						if(sql.isEmpty())
							str = "where ";
						else
							str = sql + "and ";
						List<Newsbase> result = s.createQuery("select new Newsbase(nb.id, nb.title," +
								"nb.publishtime, nb.hit, nb.replynum, nb.status, nb.sort, nb.reply, nb.display," +
								"nb.reviewer.id, nb.reviewtime, nb.nclass.id, u.id, u.name)" +
								"from Newsbase nb, User u " + str +
								"nb.author.id=u.id order by nb.publishtime desc")
								.setFirstResult(begin)
								.setMaxResults(num)
								.list();
						return result;
					}
				}
			);
	}
	@SuppressWarnings("unchecked")
	public List<Newsbase> getNbsOnSql2(final String sql, final int begin, final int num) {
		return ht.executeFind(
				new HibernateCallback<List<User>>() {
					public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
						List<User> result = s.createQuery("select new Newsbase(nb.id, nb.title," +
								"nb.publishtime, nb.hit, nb.replynum, nb.reply," +
								"nc.id, nc.name, nc.position, u.id, u.name, u2.id, u2.name, nb.replytime)" +
								"from Newsbase nb, Newsclass nc, User u, User u2 " + sql +
								" and nb.nclass.id=nc.id and nb.author.id=u.id and nb.replyer.id=u2.id " +
								"order by nb.publishtime desc")
							.setFirstResult(begin)
							.setMaxResults(num)
							.list();
						return result;
					}
				}
			);
	}
	@SuppressWarnings("unchecked")
	public String getRevwName(int id) {
		List<String> unames = ht.find("select u.name from User u where u.id=?", id);
		if(unames.size() == 0) return "不存在";
		else return unames.get(0);
	}
	@SuppressWarnings("unchecked")
	public Newsbase getNbUById(int id, boolean isShow) {
		String sql = "";
		if(isShow)
			sql = "and nb.display=1 and nb.status=0 ";
		List<Newsbase> newsbases =  ht.find("select new Newsbase(nb.id, nb.title, nb.content, nb.publishtime," +
				"nb.hit, nb.replynum, nb.status, nb.sort, nb.reply, nb.display," +
				"nb.reviewer.id, nb.reviewtime, nc.id, nc.name," +
				"u.id, u.name, u.date, u.himage, d.id, d.name) " +
				"from Newsbase nb, Newsclass nc, User u, Department d " +
				"where nb.id=? " + sql +
				"and nb.author.id=u.id and u.dpm.id=d.id and nb.nclass.id=nc.id", id);
		if(newsbases.size() < 1) return null;
		return newsbases.get(0);
	}
	@SuppressWarnings("unchecked")
	public Newsbase getNb_atrById(int nbId, boolean show) {
		String str = "";
		if(show) str = "and nb.display=1 and nb.status=0 ";
		List<Newsbase> newsbases = ht.find("select new Newsbase(nb.id, nb.title, nb.reply, nb.replynum," +
				"u.id, u.name, nc.id, nc.name) " +
				"from Newsbase nb, Newsclass nc, User u " +
				"where nb.id=? " + str +
				"and nb.author.id=u.id and nb.nclass.id=nc.id", nbId);
		if(newsbases.size() < 1) return null;
		return newsbases.get(0);
	}
	public int getUIdByNbId(int id) {
		try {
			return (Integer) ht.find("select nb.author.id from Newsbase nb " +
					"where nb.id=?", id).get(0);
		} catch(NullPointerException e) {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	public List<Newsbase> getNbsForIndex() {
		return ht.executeFind(
				new HibernateCallback<List<User>>() {
					public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
						List<User> result = s.createQuery("select new Newsbase(" +
								"nb.id, nb.title, nb.publishtime, nb.replynum, nb.replytime)" +
								"from Newsbase nb " +
								"where nb.status=0 and nb.display=1 " +
								"order by nb.replytime desc")
							.setMaxResults(5)
							.list();
						return result;
					}
				}
			);
	}
//u
	public void updateReview(int nbId, int uId) {
		ht.bulkUpdate("update Newsbase set status=0, reviewer_id=" + uId + ", reviewtime=? " +
				" where id=" + nbId, new Date());
	}
	public void updateSort(int nbId, int uId, String sort) {
		ht.bulkUpdate("update Newsbase set sort=" + sort + ", status=0, reviewer_id=" + uId + ", reviewtime=? " +
				" where id=" + nbId, new Date());
	}
	@SuppressWarnings("unchecked")
	public void update(Newsbase newsbase, int uid) {
		String hql = "update Newsbase set ";
		if(newsbase.getTitle() != null) {
			hql = hql.concat("title='" + newsbase.getTitle()
					.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", "") + "',");
		}
		if(newsbase.getNclass() != null) {
			hql = hql.concat("newsclass_id=" + newsbase.getNclass().getId() + ",");
			//更新各个新闻类型数量
			List<Integer> ncs = ht.find("select nb.nclass.id from Newsbase nb where nb.id=?", newsbase.getId());
			if(ncs.size() == 0) return;
			else {
				ht.bulkUpdate("update Newsclass set num=num-1 where id=" + ncs.get(0));
				ht.bulkUpdate("update Newsclass set num=num+1 where id=" + newsbase.getNclass().getId());
			}
		}
		//管理员才有权限修改时间和点击数
		if(uid < 0) {
			if(newsbase.getPublishtime() != null) {
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				hql = hql.concat("publishtime='" + format.format(newsbase.getPublishtime()) + "',");
			}
			if(newsbase.getHit() > 0) {
				hql = hql.concat("hit=" + newsbase.getHit() + ",");
			}
		}
		/**
		 * 更新的如果是内容，不能直接添加
		 * sort reply display 如果不更改，应该从浏览器传-1过来，如果不传，将使用默认的0，
		 * 既然一定要传一个参数，那么就把当前的传过来，直接修改，省去判断是否要更改
		 */
		if(newsbase.getContent() != null) {
			if(uid < 0) {
				ht.bulkUpdate(hql.concat("status=0,display=" + newsbase.getDisplay() +
					", sort=" + newsbase.getSort() +
					", reply=" + newsbase.getReply() +
					", reviewer_id=" + uid*-1 +
					", reviewtime=? " +
					", content=? where id=" + newsbase.getId()), new Date(), newsbase.getContent());
			} else {
				ht.bulkUpdate(hql.concat("reply=" + newsbase.getReply() +
					", content=? where id=" + newsbase.getId()), newsbase.getContent());
			}
		} else {
			if(uid < 0) {
				ht.bulkUpdate(hql.concat("status=0,display=" + newsbase.getDisplay() +
					", sort=" + newsbase.getSort() +
					", reply=" + newsbase.getReply() +
					", reviewer_id=" + uid*-1 +
					", reviewtime=? " +
					" where id=" + newsbase.getId()), new Date());
			} else {
				ht.bulkUpdate(hql.concat("reply=" + newsbase.getReply() +
						" where id=" + newsbase.getId()));
			}
		}
	}
	public void updateHit(int nbId) {
		ht.bulkUpdate("update Newsbase nb set nb.hit=nb.hit+1 where nb.id=?", nbId);
	}
	public void updateReply(int newsId, int userId) {
		ht.bulkUpdate("update Newsbase nb set nb.replyer.id=" + userId +
				", nb.replytime=? where nb.id=" + newsId, new Date());
	}
//d
	public void delete(int nbId) {
		ht.bulkUpdate("delete from Newsreply nr where nr.nbase.id=?", nbId);
		ht.bulkUpdate("delete from Newsbase nb where nb.id=?", nbId);
	}

}
