package com.baodian.dao.handover.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.handover.AskLeaveDao;

@Component("askLeaveDao")
public class AskLeaveDaoImpl extends BaseDAOImpl implements AskLeaveDao{
	
	public static final String AskLeave = "com.baodian.model.handover.AskLeave";

	@Override
	public Object findById(String id) {
		return super.findById(AskLeave, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(AskLeave);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(AskLeave, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(AskLeave, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(AskLeave, field1, value1, field2, value2);
	}

}
