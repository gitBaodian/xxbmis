package com.baodian.service.record;

import java.io.InputStream;
import java.util.List;

import com.baodian.model.record.CustomerRecord;

public interface CustomerRecordManager {
	
	public String listTable(int pageSize, int currentPage);
	
	public String addCustomerRecord(String ticket_id,String type,String title,String detail,
									String solve_approach,String op_username,String time) throws Exception;
	
	public String updateCustomerRecord(int cr_id,String ticket_id,String type,String title,String detail,
									   String solve_approach,String op_username,String time) throws Exception;
	
	public CustomerRecord findCRById(int cr_id);
	
	public String delCustomerRecord(int cr_id);
	
	public String searchByKeyword(int num, int page, String keyword);
	
	public String secrchByCondition(int num, int page, String time, String type, String ticket_id, String state);
	
	public InputStream exportAll2Excel() throws Exception;
	
	public InputStream exportCurrent2Excel(String time, String type, String ticket_id, String state) throws Exception;
}
