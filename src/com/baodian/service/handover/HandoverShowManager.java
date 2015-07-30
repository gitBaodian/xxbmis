package com.baodian.service.handover;

import java.text.ParseException;

import com.baodian.model.handover.Shift;
import com.baodian.util.page.Page;

public interface HandoverShowManager {

	public String handover_get(int id);
	
	public String handover_update(Shift shift);
	
	public String handover_add(Shift shift) throws ParseException;
	
	public String handover_delete(int id);
	
	public String handover_show(Page page) throws ParseException;
}
