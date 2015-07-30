package com.baodian.dao.handover.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.handover.TransferDao;

@Component("transferDao")
public class TransferDaoImpl extends BaseDAOImpl implements TransferDao{

	public static final String Transfer = "com.baodian.model.handover.Transfer";

	@Override
	public Object findById(String id) {
		return super.findById(Transfer, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Transfer);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Transfer, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Transfer, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Transfer, field1, value1, field2, value2);
	}
}
