package com.baodian.service.handover;

import java.text.ParseException;

import org.json.simple.JSONObject;

import com.baodian.util.page.Page;

public interface NoticeManager {
	
	public JSONObject notice();
	
	public String get_ak_zb_list(String userName ,Page page) throws ParseException;
	
	public boolean rm_ak_zb(int id);
	
	public String get_tf_zb_list(String userName ,Page page) throws ParseException;
	
	public boolean rm_tf_zb(int id);

}
