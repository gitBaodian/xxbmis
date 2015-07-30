package com.baodian.action.news;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsclass;
import com.baodian.service.news.NewsbaseManager;
import com.baodian.service.news.NewsclassManager;
import com.baodian.util.page.NewsbasePage;

@SuppressWarnings("serial")
@Controller("newsbase")
@Scope("prototype")//必须注解为多态
public class NewsbaseAction extends ActionSupport {
	private String actionStr;
	private String json;
	private Newsbase newsbase;
	private Newsclass newsclass;
	private NewsbasePage page;
	private int[] nbIds;
	private List<Newsbase> newsbases;
	@Resource(name="newsbaseManager")
	private NewsbaseManager newsbaseManager;
	@Resource(name="newsclassManager")
	private NewsclassManager newsclassManager;

//c
	public String add_js() {
		json = this.newsbaseManager.saveNewsbase(newsbase);
		return "json";
	}
//r
	public String list() {
		json = newsclassManager.findNc_in();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new NewsbasePage();
		json = this.newsbaseManager.findNbsByPage(page);
		return "json";
	}
	/**
	 * 首页展示的8条新闻
	 */
	public String index_js() {
		json = this.newsbaseManager.findNbsForIndex().toString();
		return "json";
	}
//u
	/**
	 * 批量审核新闻
	 * @return
	 */
	public String review_js() {
		if(nbIds==null && nbIds.length==0)
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		else
			json = newsbaseManager.changeNbReview(nbIds);
		return "json";
	}
	/**
	 * 批量置顶新闻
	 * @return
	 */
	public String sort_js() {
		if(nbIds==null && nbIds.length==0)
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		else
			json = newsbaseManager.changeNbSort(nbIds, json);
		return "json";
	}
	public String change_no() {
		if(newsbase==null || newsbase.getId()==0 ||
				newsbase.ckTitle()==-2 || newsbase.ckContent()==-2) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = newsbaseManager.changeNewsbase(newsbase);
		}
		return "json";
	}
//d
	public String remove_js() {
		if(nbIds == null && nbIds.length == 0)
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		else {
			newsbaseManager.removeNbs(nbIds);
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		}
		return "json";
	}
//set get
	public String getActionStr() {
		return actionStr;
	}
	public void setActionStr(String actionStr) {
		this.actionStr = actionStr;
	}
	public Newsbase getNewsbase() {
		return newsbase;
	}
	public void setNewsbase(Newsbase newsbase) {
		this.newsbase = newsbase;
	}
	public Newsclass getNewsclass() {
		return newsclass;
	}
	public void setNewsclass(Newsclass newsclass) {
		this.newsclass = newsclass;
	}
	public List<Newsbase> getNewsbases() {
		return newsbases;
	}
	public void setNewsbases(List<Newsbase> newsbases) {
		this.newsbases = newsbases;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public NewsbasePage getPage() {
		return page;
	}
	public void setPage(NewsbasePage page) {
		this.page = page;
	}
	public int[] getNbIds() {
		return nbIds;
	}
	public void setNbIds(int[] nbIds) {
		this.nbIds = nbIds;
	}
	
}
