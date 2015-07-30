package com.baodian.dao.record.impl;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.record.CustomerRecordDao;
import com.baodian.dao.record.RecordDao;
import com.baodian.model.record.CustomerRecord;
import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;

@Repository("c_recordDao")
public class CustomerRecordDaoImpl implements CustomerRecordDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public void addCustomerRecoed(CustomerRecord cr) {
		ht.save(cr);
	}

	public CustomerRecord findCRById(int cr_id) {
		CustomerRecord cr =  ht.get(CustomerRecord.class, cr_id);
		return cr;
	}

	public void updateCustomerRecord(CustomerRecord cr) {
		ht.update(cr);
	}

	public void delCustomerRecord(int cr_id) {
		ht.bulkUpdate("delete CustomerRecord cr where cr.id=?",cr_id);
	}
	
	
}
