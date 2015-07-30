package com.baodian.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.baodian.service.duty.DutyManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("duty")
@Scope("prototype")//必须注解为多态
public class DutyAction extends ActionSupport {
	private String json;
	@Resource(name = "dutyManager")
	private DutyManager dtm;
//c
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		json = dtm.findAll();
		return "json";
	}
//u
	public String change_js() {
		try{
			dtm.changeAll(json);
			json = "{\"status\":0}";
		} catch(DataIntegrityViolationException e){
			e.printStackTrace();
			json = "{\"status\":1,\"mess\":\"所选部门不存在！\"}";
		}
		return "json";
	}
//d
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
