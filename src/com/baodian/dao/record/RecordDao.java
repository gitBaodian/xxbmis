package com.baodian.dao.record;

import java.util.Date;
import java.util.List;

import com.baodian.model.record.Trouble_Record;
import com.baodian.model.record.Working_Record;

public interface RecordDao {
	
	public void addWorkingRecord(Working_Record wr);
	
	public Working_Record findWR_by_ID(int id);
	
	public Working_Record findWR_by_UseridAndTime(int u_id, Date time);
	
	public void updateWorkingRecord(Working_Record wr);
	
	public void deleteWorkingRecord(int id);
	
	public void addTroubleRecord(Trouble_Record tr);
	
	public void deleteTroubleRecord(int id);
	
    public Trouble_Record findTR_by_ID(int id);
	
	public void updateTroubleRecord(Trouble_Record tr);
	
	public List count4Analyse(String hql);
	
}
