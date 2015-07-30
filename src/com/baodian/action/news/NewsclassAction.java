package com.baodian.action.news;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.news.Newsclass;
import com.baodian.service.news.NewsclassManager;

@SuppressWarnings("serial")
@Controller("newsclass")
@Scope("prototype")//必须注解为多态
public class NewsclassAction extends ActionSupport {
	private int[] ncIds;
	private String actionStr;
	private String json;
	private Newsclass newsclass;
	@Resource(name="newsclassManager")
	private NewsclassManager newsclassManager;
//c
	public String add_js() {
		if(newsclass != null && newsclass.getName() == null) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			newsclassManager.saveNewsclass(newsclass);
			json = "{\"status\":0,\"mess\":\"添加成功！\"}";
		}
		return "json";
	}
//r
	public String list() {
		return SUCCESS;
	}
	public String list_js() {
		json = newsclassManager.findNcs();
		return "json";
	}
	
//u
	public String change_js() {
		if(newsclass!=null && newsclass.getId()==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			newsclassManager.changeNewsclass(newsclass);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return "json";
	}
//d
	public String remove_js() {
		if(newsclass!=null && newsclass.getId()==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = newsclassManager.removeNewsclass(newsclass);
		}
		return "json";
	}
	
//set get
	public Newsclass getNewsclass() {
		return newsclass;
	}
	public void setNewsclass(Newsclass newsclass) {
		this.newsclass = newsclass;
	}
	public String getActionStr() {
		return actionStr;
	}
	public void setActionStr(String actionStr) {
		this.actionStr = actionStr;
	}
	public int[] getNcIds() {
		return ncIds;
	}
	public void setNcIds(int[] ncIds) {
		this.ncIds = ncIds;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
}
