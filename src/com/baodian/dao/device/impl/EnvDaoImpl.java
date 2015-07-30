package com.baodian.dao.device.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.device.EnvDao;

@Component("envDao")
public class EnvDaoImpl extends BaseDAOImpl implements EnvDao{

	public static final String Env = "com.baodian.model.device.Env";

	@Override
	public Object findById(String id) {
		return super.findById(Env, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Env);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Env, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Env, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Env, field1, value1, field2, value2);
	}
}
