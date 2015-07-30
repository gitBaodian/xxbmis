package com.baodian.dao.handover.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.handover.SpecialTransferDao;

@Component("specialTransferDao")
public class SpecialTransferDaoImpl  extends BaseDAOImpl implements SpecialTransferDao{

	public static final String SpecialTransfer = "com.baodian.model.handover.SpecialTransfer";

	@Override
	public Object findById(String id) {
		return super.findById(SpecialTransfer, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(SpecialTransfer);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(SpecialTransfer, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(SpecialTransfer, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(SpecialTransfer, field1, value1, field2, value2);
	}
	
}
