package com.baodian.dao.device.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.device.EquipmentDao;

@Component("equipmentDao")
public class EquipmentDaoImpl extends BaseDAOImpl implements EquipmentDao{

	public static final String Equipment = "com.baodian.model.device.Equipment";

	@Override
	public Object findById(String id) {
		return super.findById(Equipment, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Equipment);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Equipment, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Equipment, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Equipment, field1, value1, field2, value2);
	}
}
