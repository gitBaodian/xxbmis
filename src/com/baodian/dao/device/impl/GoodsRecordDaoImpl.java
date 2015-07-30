package com.baodian.dao.device.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.device.GoodsRecordDao;
import com.baodian.model.device.GoodsRecord;
import com.baodian.model.user.User;
import com.baodian.util.page.GrPage;

@Repository("goodsRecordDao")
public class GoodsRecordDaoImpl implements GoodsRecordDao {
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
//c
	public void add(GoodsRecord gr) {
		ht.save(gr);
	}
//r
	@SuppressWarnings("unchecked")
	public List<GoodsRecord> getGsByPage(final GrPage page) {
		long nums = (Long) ht.find("select count(*) from GoodsRecord gr ").get(0);
		page.countPage((int) nums);
		if(nums == 0) {
			return Collections.emptyList();
		}
		return ht.executeFind(
			new HibernateCallback<List<User>>() {
				public List<User> doInHibernate(Session s) throws HibernateException, SQLException {
					List<User> result = s.createQuery("select new GoodsRecord(gr.id, gr.date, gr.name," +
							"gr.num, gr.dtin.id, gr.dtout.id, gr.gd.id, u.id, u.name)" +
							"from GoodsRecord gr, User u " +
							"where gr.recorder.id=u.id order by gr.date desc")
						.setFirstResult(page.getFirstNum())
						.setMaxResults(page.getNum())
						.list();
					return result;
				}
			}
		);
	}
	@SuppressWarnings("unchecked")
	public List<GoodsRecord> getGs() {
		return ht.find("from GoodsRecord gr order by gr.date desc");
	}
//u
	public void update(GoodsRecord gr) {
		ht.update(gr);
	}
//d
	public void delete(String id) {
		ht.bulkUpdate("delete from GoodsRecord gr where gr.id=" + id);
	}
}
