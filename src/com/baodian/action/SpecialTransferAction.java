package com.baodian.action;

import java.text.ParseException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.handover.SpecialTransfer;
import com.baodian.service.handover.SpecialTransferManager;
import com.baodian.util.page.Page;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("specialtransfer")
@Scope("prototype")
public class SpecialTransferAction extends ActionSupport{

	private String json;
	private Page  page;
	private SpecialTransferManager specialTransferManager;
	
	private String specil_date;
	private String one;
	private String two;
	private String there;
	
	
	public String manager(){
		return SUCCESS;
	}
	
	/**
	 * 获取排班对象
	 * @return
	 */
	public String getuser(){
		json = specialTransferManager.specialTransfer_getuser();
		return "json";
	}
	
	/**
	 * 特殊排班显示
	 * @return
	 * @throws ParseException 
	 */
	public String specialtransfer_show() throws ParseException{
		json = specialTransferManager.specialTransfer_show(page);
		return "json";
	}
	
	/**
	 * 特殊排班添加
	 * @return
	 */
	public String specialtransfer_add(){
		SpecialTransfer st = new SpecialTransfer();
		st.setSpecialDate(specil_date);
		st.setOne(one);
		st.setTwo(two);
		st.setThere(there);
		json = specialTransferManager.specialTransfer_add(st);
		return "json";
	}
	
	/**
	 * 特殊排班添加
	 * @return
	 */
	public String specialtransfer_del(){
		SpecialTransfer st = new SpecialTransfer();
		st.setSpecialDate(specil_date);
		json = specialTransferManager.specialTransfer_del(st);
		return "json";
	}
	
	/**
	 * 特殊排班添加
	 * @return
	 */
	public String specialtransfer_update(){
		SpecialTransfer st = new SpecialTransfer();
		st.setSpecialDate(specil_date);
		st.setOne(one);
		st.setTwo(two);
		st.setThere(there);
		json = specialTransferManager.specialTransfer_update(st);
		return "json";
	}
	
	
	
	
	public String getJson() {
		return json;
	}
	
	public void setJson(String json) {
		this.json = json;
	}
	
	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	public String getSpecil_date() {
		return specil_date;
	}

	public void setSpecil_date(String specil_date) {
		this.specil_date = specil_date;
	}

	public String getOne() {
		return one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	public String getTwo() {
		return two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	public String getThere() {
		return there;
	}

	public void setThere(String there) {
		this.there = there;
	}

	public SpecialTransferManager getSpecialTransferManager() {
		return specialTransferManager;
	}
	
	@Resource(name = "specialTransferManager")
	public void setSpecialTransferManager(
			SpecialTransferManager specialTransferManager) {
		this.specialTransferManager = specialTransferManager;
	}
	
	

}
