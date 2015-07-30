package com.baodian.service.record;

import java.util.Date;
import java.util.LinkedHashMap;

import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;

public interface RecordManager {
	
	public void addWorkingRecord(Working_Record wr);
	
	public Working_Record findWR_by_ID(int id);
	
	public Working_Record findWR_by_UseridAndTime(int u_id, Date time);
	
	public void updateWorkingRecord(Working_Record wr);
	
	public void deleteWorkingRecord(int id);
	
	public String wr_listTable(int num, int page);
	
	public String wr_secrchByCondition(int num, int page, String from_date, String to_date, 
									   String type, String dept_name, String username);
	
	public String wr_searchByKeyword(int num,int page,String keyword);
	
	public void addTroubleRecord(Trouble_Record tr);
	
	public void deleteTroubleRecord(int id);
	
    public Trouble_Record findTR_by_ID(int id);
	
	public void updateTroubleRecord(Trouble_Record tr);

	public String tr_searchByKeyword(int num, int page, String keyword);
	
	public LinkedHashMap count4Analyse(String hql);
	
}
