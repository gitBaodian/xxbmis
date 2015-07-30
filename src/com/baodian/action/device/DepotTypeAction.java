package com.baodian.action.device;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.DepotType;
import com.baodian.service.device.DepotTypeManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("depottype")
@Scope("prototype")//必须注解为多态
public class DepotTypeAction extends ActionSupport {
	@Resource(name="depotTypeManager")
	private DepotTypeManager dtm;
	private String json;
	private DepotType dt;
//c
	public String add_js() {
		json = dtm.save(dt);
		return "json";
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		json = dtm.findDts();
		return "json";
	}
//u
	public String change_js() {
		json = dtm.change(dt);
		return "json";
	}
//d
	public String remove_js() {
		json = dtm.remove(json);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public DepotType getDt() {
		return dt;
	}
	public void setDt(DepotType dt) {
		this.dt = dt;
	}
	
}
