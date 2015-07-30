package com.baodian.dao.inspection.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.inspection.Inspection03Dao;

@Component("inspection03Dao")
public class Inspection03DaoImpl extends BaseDAOImpl implements Inspection03Dao{
	
	public static final String Inspection03 = "com.baodian.model.inspection.Inspection03";

	@Override
	public Object findById(String id) {
		return super.findById(Inspection03, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Inspection03);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Inspection03, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Inspection03, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Inspection03, field1, value1, field2, value2);
	}

}
