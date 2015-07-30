package com.baodian.service.handover;

import java.text.ParseException;

public interface ApplicationManager {

	public String userPost() throws ParseException;
	
	public String ask_leave(String username,String begindate,String enddate) throws ParseException;
	
	public String apl_transfer(String applicant,String applicant_time,String object,String t_or_r);
}
