package com.baodian.dao.page.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.page.PageDao;
import com.baodian.model.record.Working_Record;

@Repository("pageDao")
public class PageDaoImpl implements PageDao {

	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}
	
	//通过HQL语句 "select count(*) from table_name" 获取记录总数
	public int getRecordCount(final String HQL) {
		/*List list = ht.executeFind(
				new HibernateCallback() {
					public List doInHibernate(Session s) throws HibernateException, SQLException {
						List result = s.createQuery(HQL).list();
						return result;
					}
				}
			);*/
		List result = ht.find(HQL);
		int recordCount = ((Number) result.iterator().next()).intValue();

		//System.out.println("++++++++recordCount="+recordCount);
		return recordCount;
	}
	
	public List getCurrentPageRecord(int pageSize, int currentPage, final String HQL) {    //获取当前页记录
		int intPage = (currentPage == 0) ? 1:currentPage;  //当前页
        final int number = (pageSize == 0) ? 10:pageSize;       //每页显示条数 
        final int start = (intPage-1)*number;                  //起始记录
        List results = new ArrayList();
        try{
        	results = ht.executeFind(
    				new HibernateCallback() {
    					public List doInHibernate(Session s) throws HibernateException, SQLException {
    						List result = s.createQuery(HQL)
    							.setFirstResult(start)
    							.setMaxResults(number)
    							.list();
    						return result;
    					}
    				}
    			);
        }catch (Exception e) {
        	e.printStackTrace();
        }
        
/*        
		List results = ht.executeFind(
				new HibernateCallback() {
					public List doInHibernate(Session s) throws HibernateException, SQLException {
						List result = s.createQuery(HQL)
							.setFirstResult(start)
							.setMaxResults(number)
							.list();
						return result;
					}
				}
			);
*/		
		return results;
	}
}
