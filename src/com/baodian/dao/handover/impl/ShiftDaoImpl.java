package com.baodian.dao.handover.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.handover.ShiftDao;

@Component("shiftDao")
public class ShiftDaoImpl extends BaseDAOImpl implements ShiftDao{

	public static final String Shift = "com.baodian.model.handover.Shift";

	@Override
	public Object findById(String id) {
		return super.findById(Shift, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Shift);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Shift, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Shift, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Shift, field1, value1, field2, value2);
	}
}
