package com.baodian.dao.inspection.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.inspection.Inspection02Dao;

@Component("inspection02Dao")
public class Inspection02DaoImpl extends BaseDAOImpl implements Inspection02Dao{
	
	public static final String Inspection02 = "com.baodian.model.inspection.Inspection02";

	@Override
	public Object findById(String id) {
		return super.findById(Inspection02, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Inspection02);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Inspection02, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Inspection02, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Inspection02, field1, value1, field2, value2);
	}

}
