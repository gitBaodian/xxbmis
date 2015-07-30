package com.baodian.action.device;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.UseType;
import com.baodian.service.device.UseTypeManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("usetype")
@Scope("prototype")//必须注解为多态
public class UseTypeAction extends ActionSupport {
	@Resource(name="useTypeManager")
	private UseTypeManager utm;
	private String json;
	private UseType ut;
//c
	public String add_js() {
		json = utm.save(ut);
		return "json";
	}
//r
	public String list() {
		json = utm.findGoods();
		return SUCCESS;
	}
	public String list_js() {
		json = utm.findUts();
		return "json";
	}
//u
	public String change_js() {
		json = utm.change(ut);
		return "json";
	}
//d
	public String remove_js() {
		json = utm.remove(json);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public UseType getUt() {
		return ut;
	}
	public void setUt(UseType ut) {
		this.ut = ut;
	}
}
