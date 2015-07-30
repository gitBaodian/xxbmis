package com.baodian.dao.inspection.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.inspection.Inspection01Dao;

@Component("inspection01Dao")
public class Inspection01DaoImpl extends BaseDAOImpl implements Inspection01Dao{
	
	public static final String Inspection01 = "com.baodian.model.inspection.Inspection01";

	@Override
	public Object findById(String id) {
		return super.findById(Inspection01, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Inspection01);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Inspection01, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Inspection01, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Inspection01, field1, value1, field2, value2);
	}

}
