package com.baodian.action;

import java.text.ParseException;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.service.handover.ApplicationManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("application")
@Scope("prototype")
public class ApplicationAction extends ActionSupport{
	
	private ApplicationManager applicationManager;
	private String json;
	
	private String username;
	private String posts;
	private String begindate;
	private String enddate;
	
	private String applicant;
	private String applicant_time;
	private String object;
	private String t_or_r;
	
	//获取用户信息
	public String getuserPost() throws ParseException{
		json = applicationManager.userPost();
		return "json";
	}
	
	//请假申请
	public String ask_leave() throws ParseException{
		json = applicationManager.ask_leave(username, begindate, enddate);
		return "json";
	}
	
	//调班申请
	public String apl_transfer(){
		json = applicationManager.apl_transfer(applicant, applicant_time, object,t_or_r);
		return "json";
	}
	
	
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPosts() {
		return posts;
	}
	public void setPosts(String posts) {
		this.posts = posts;
	}
	public String getBegindate() {
		return begindate;
	}
	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}
	public String getEnddate() {
		return enddate;
	}
	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getApplicant_time() {
		return applicant_time;
	}
	public void setApplicant_time(String applicant_time) {
		this.applicant_time = applicant_time;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getT_or_r() {
		return t_or_r;
	}


	public void setT_or_r(String t_or_r) {
		this.t_or_r = t_or_r;
	}


	@Resource(name = "applicationManager")
	public void setApplicationManager(ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

}
