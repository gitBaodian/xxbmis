package com.baodian.dao.inspection.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baodian.dao.base.impl.BaseDAOImpl;
import com.baodian.dao.inspection.InspectionDao;

@Component("inspectionDao")
public class InspectionDaoImpl extends BaseDAOImpl implements InspectionDao{
	
	public static final String Inspection = "com.baodian.model.inspection.Inspection";

	@Override
	public Object findById(String id) {
		return super.findById(Inspection, id);   
	}
	
	@Override
	public List<?> findTable() {
		return super.findTable(Inspection);   
	}

	@Override
	public List<?> findByProperty(String field, Object value) {
		return super.findByProperty(Inspection, field, value);   
	}

	@Override
	public Object findById(int id) {
		return super.findById(Inspection, id); 
	}

	@Override
	public List<?> findByProperty(String field1, Object value1, String field2,
			Object value2) {
		return super.findByProperty(Inspection, field1, value1, field2, value2);
	}

}
