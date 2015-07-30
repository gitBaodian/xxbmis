package com.baodian.action;

import java.text.ParseException;

import javax.annotation.Resource;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.service.handover.NoticeManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.Page;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("notice")
@Scope("prototype")
public class NoticeAction extends ActionSupport{
	
	private String json;
	private Page  page;
	private NoticeManager noticeManager;
	
	private int id;
	
	public String ak_zb(){
		return SUCCESS;
	}
	
	public String tf_zb(){
		return SUCCESS;
	}
	
	public String getnotice(){
		json = noticeManager.notice().toString();
		return "json";
	}
	
	public String ak_zb_list() throws ParseException{
		String userName = SecuManagerImpl.currentName();
		if(page == null)
			page = new Page();
		json = noticeManager.get_ak_zb_list(userName,page);
		return "json";
	}
	
	
	public String remove_ak_zb(){
		boolean i = noticeManager.rm_ak_zb(id);
		if(i==true){
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"删除失败！\"}";
		}
		return "json";
	}
	
	
	public String tf_zb_list() throws ParseException{
		String userName = SecuManagerImpl.currentName();
		if(page == null)
			page = new Page();
		json = noticeManager.get_tf_zb_list(userName,page);
		return "json";
	}
	
	
	public String remove_tf_zb(){
		boolean i = noticeManager.rm_tf_zb(id);
		if(i==true){
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"删除失败！\"}";
		}
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
	
	public NoticeManager getNoticeManager() {
		return noticeManager;
	}
	
	@Resource(name = "noticeManager")
	public void setNoticeManager(NoticeManager noticeManager) {
		this.noticeManager = noticeManager;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
}
