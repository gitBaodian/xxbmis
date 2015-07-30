package com.baodian.action;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.email.Email;
import com.baodian.service.email.EmailManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.EmailPage;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("email")
@Scope("prototype")//必须注解为多态
public class EmailAction extends ActionSupport {
	private String json;
	private EmailPage page;
	private Email email;
	
	@Resource(name = "emailManager")
	private EmailManager em;
//c
	public String add_js() {
		json = em.saveEmail(email, json);
		return "json";
	}
//r
	public String list_rd() {
		return SUCCESS;
	}
	/**
	 * 读取列表
	 */
	public String list_js() {
		if(page == null) page = new EmailPage();
		json = em.findE_Us(page);
		return "json";
	}
	/**
	 * 自动刷新
	 */
	public String autorf_js() {
		json = em.findNewUrE(json);
		return "json";
	}
	/**
	 * 读取邮件
	 */
	public String read_js() {
		json = em.fcEM(json);
		return "json";
	}
//u
	/**
	 * 更改邮件状态
	 */
	public String changest_js() {
		if(page == null) page = new EmailPage();
		json = em.changeSt(json, page);
		return "json";
	}
	/**
	 * 将所有未读收件改为已读
	 */
	public String changeur_js() {
		if(page == null) page = new EmailPage();
		json = em.changeUr(json, page);
		return "json";
	}
//setget
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public EmailPage getPage() {
		return page;
	}
	public void setPage(EmailPage page) {
		this.page = page;
	}
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	//用户登录信息
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManagerImpl.currentUser();
	}
}
