package com.baodian.dao.base.impl;

import java.util.List;   

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;

import com.baodian.dao.base.BaseDAO;


  
public abstract class BaseDAOImpl implements BaseDAO {   
  
	public HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Resource(name = "hibernateTemplate")
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
    protected BaseDAOImpl() {   
  
    }   
  
    /**
     * 添加对象
     */
    public void save(Object entity) {  
    	hibernateTemplate.save(entity);
    }   
  
    /**
     * 删除对象
     */
    public void delete(Object entity) {   
    	hibernateTemplate.delete(entity);   
    } 
    
    /**
     * 修改对象
     */
    public void attachDirty(Object entity) {
		hibernateTemplate.saveOrUpdate(entity); 
	}
  
    /**
     * 查找整个表
     * @param entityName
     * @return
     */
    public List<?> findTable(String entityName) {   
        String queryString = "from " + entityName;   
        return hibernateTemplate.find(queryString);
    }   
    
    /**
     * 通过主键查找，主键为int类型
     * @param entityName
     * @param id
     * @return
     */
    public Object findById(String entityName, int id) {   
        return hibernateTemplate.get(entityName, id);   
    } 
    
    /**
     * 通过主键查找，主键为string类型
     * @param entityName
     * @param id
     * @return
     */
    public Object findById(String entityName, String id) {   
        return hibernateTemplate.get(entityName, id);   
    }   
  
    /**
     * 通过某一个属性查找，
     * @param entityName
     * @param field
     * @param value
     * @return
     */
    public List<?> findByProperty(String entityName, String field, Object value) {   
        String queryString = "from " + entityName + " as model where model." + field + "= ?";   
        return hibernateTemplate.find(queryString, value);   
    }
    
    /**
     * 通过某两个属性查找，
     * @param entityName
     * @param field1
     * @param value1
     * @param field2
     * @param value2
     * @return
     */
    public List<?> findByProperty(String entityName, String field1, Object value1, String field2, Object value2) {   
        String queryString = "from " + entityName + " as model where model." + field1 + "="+value1 + "and" +"model."+field2+"="+value2;   
        return hibernateTemplate.find(queryString);   
    }
    
    
    /**
     * 使用自定义的hql语句查询
     */
    public List<?> findhql(String hql){
    	return hibernateTemplate.find(hql);
    }
    
    /**
     * 通过自定义的hql语句查询，并删除所查得的数据
     */
    public void delhql(String hql){
    	List<?> list = this.getHibernateTemplate().find(hql);
    	if(list.size()>0){   
    	    this.getHibernateTemplate().deleteAll(list);
    	} 
    }
  
    /**
     * 分页查询
     */
	public Criteria createCriteria(Class<?> class1) {
		SessionFactory sf = hibernateTemplate.getSessionFactory();
		Session session = sf.getCurrentSession();
		return session.createCriteria(class1);
	}
	
	/**
	 * 分页查询
	 */
	public Criteria createCriteria(Class<?> class1,String type) {
		SessionFactory sf = hibernateTemplate.getSessionFactory();
		Session session = sf.getCurrentSession();
		return session.createCriteria(class1,type);
	}
	
	/**
	 * 分页查询
	 */
	public List<?> createQuery(String hql,int first,int num){
		SessionFactory sf = hibernateTemplate.getSessionFactory();
		Session session = sf.getCurrentSession();
		return session.createQuery(hql).setFirstResult(first).setMaxResults(num).list();
	}
    /*   
  
    public List<?> findByExample(Object exampleEntity) {   
        return hibernateTemplate.findByExample(exampleEntity);   
    }   
  
    public Object merge(Object entity) {   
        return hibernateTemplate.merge(entity);   
    }   
  
  
    public void attachClean(Object entity) {   
    	hibernateTemplate.lock(entity, LockMode.NONE);   
    }   */
  
}  
