package com.baodian.service.handover;

import java.text.ParseException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public interface HandoverManager {
	
	public JSONObject duty_officer() throws ParseException;
	
	public JSONArray handover();
	
	public String handover_get_remark_shift();
	
	public  String shift(String remark_shift, String addnp) throws ParseException;
	
	public String accept(String remark_accept, String user_name) throws ParseException;
	
}
