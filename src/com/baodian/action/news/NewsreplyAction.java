package com.baodian.action.news;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsreply;
import com.baodian.service.news.NewsreplyManager;

@SuppressWarnings("serial")
@Controller("newsreply")
@Scope("prototype")//必须注解为多态
public class NewsreplyAction extends ActionSupport {
	private String json;
	private Newsreply newsreply;
	private Newsbase newsbase;
	private List<Newsreply> newsreplies;
	@Resource(name="newsreplyManager")
	private NewsreplyManager newsreplyManager;
//c
	/**
	 * 添加回复
	 * @return
	 */
	public String add_js() {
		json = newsreplyManager.saveNewsreply(newsreply);
		return "json";
	}
//r
//u
	public String change_no() {
		if(newsreply==null || newsreply.getId()==0 || newsreply.ckContent()<1)
			json = "{\"status\":1,\"mess\":\"参数有误！\"}";
		else
			json = newsreplyManager.changeNr(newsreply);
		return "json";
	}
//d
	public String remove_no() {
		if(newsreply==null || newsreply.getId()==0)
			json = "{\"status\":1,\"mess\":\"参数有误！\"}";
		else
			json = newsreplyManager.remove(newsreply);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Newsreply getNewsreply() {
		return newsreply;
	}
	public void setNewsreply(Newsreply newsreply) {
		this.newsreply = newsreply;
	}
	public Newsbase getNewsbase() {
		return newsbase;
	}
	public void setNewsbase(Newsbase newsbase) {
		this.newsbase = newsbase;
	}
	public List<Newsreply> getNewsreplies() {
		return newsreplies;
	}
	public void setNewsreplies(List<Newsreply> newsreplies) {
		this.newsreplies = newsreplies;
	}
}
