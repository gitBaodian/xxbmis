package com.baodian.dao.base;

import java.util.List;   

import org.hibernate.Criteria;
  
public interface BaseDAO {   
  
    public void save(Object entity);   
  
    public void delete(Object entity); 
    
    public void attachDirty(Object entity);
  
    public Object findById(int id);
    
    public Object findById(String id);   
  
    public List<?> findTable();   
    
    public void delhql(String hql);
  
    public List<?> findByProperty(String field, Object value); 
    
    public List<?> findByProperty(String field1, Object value1, String field2, Object value2);
    
    public List<?> findhql(String hql);
    
    public Criteria createCriteria(Class<?> class1);
    
    public Criteria createCriteria(Class<?> class1,String type);
    
    public List<?> createQuery(String sql,int first,int num);
  
   // public List<?> findByExample(Object exampleEntity);   
    
   // public List<?> findByProperty(String field, Object minValue, Object maxValue);   
  
   // public List<?> findByLinkProperty(String field, Object value);   
  
   // public Object merge(Object entity);   
  
   // public void attachDirty(Object entity);   
  
   // public void attachClean(Object entity);   
  
}  
