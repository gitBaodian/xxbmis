package com.baodian.dao.record;

import com.baodian.model.record.CustomerRecord;

public interface CustomerRecordDao {
	
	public void addCustomerRecoed(CustomerRecord cr);
	
	public CustomerRecord findCRById(int cr_id);
	
	public void updateCustomerRecord(CustomerRecord cr);
	
	public void delCustomerRecord(int cr_id);
}
