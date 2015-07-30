package com.baodian.action.news;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.baodian.util.page.NewsPage;
import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsclass;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.news.NewsManager;
import com.baodian.service.news.NewsbaseManager;
import com.baodian.service.news.NewsclassManager;

@SuppressWarnings("serial")
@Controller("news")
@Scope("prototype")//必须注解为多态
public class NewsAction extends ActionSupport{
	private NewsPage page;
	private Newsbase newsbase;
	private List<Newsclass> newsclasses;
	private List<List<Newsbase>> newsbaseList;
	@Resource(name="newsManager")
	private NewsManager newsManager;
	@Resource(name="newsbaseManager")
	private NewsbaseManager newsbaseManager;
	@Resource(name="newsclassManager")
	private NewsclassManager newsclassManager;
	@Resource(name="userManager")
	private UserManager userManager;
	
//c
	public String add_rd() {
		newsclasses = newsclassManager.findNcs_in();
		return SUCCESS;
	}
//r
	/**
	 * newsbaseList：第一个为置顶（第一页），第二个所有类型中的分页
	 */
	@SuppressWarnings("unchecked")
	public String list_rd() {
		if(page == null)
			page = new NewsPage();
		List<Object> obj = this.newsManager.findNbsOnPage(page);
		newsclasses = (List<Newsclass>) obj.get(0);
		newsbaseList = (List<List<Newsbase>>) obj.get(1);
		return SUCCESS;
	}
//u
	public String change_rd() {
		if(newsbase==null || newsbase.getId()==0) {
			return "list_rd";
		}
		newsbase = this.newsbaseManager.findNbUById(newsbase.getId());
		//if(newsbase == null) return;
		newsclasses = newsclassManager.findNcs_in();
		return SUCCESS;
	}
//set get
	public List<List<Newsbase>> getNewsbaseList() {
		return newsbaseList;
	}
	public void setNewsbaseList(List<List<Newsbase>> newsbaseList) {
		this.newsbaseList = newsbaseList;
	}
	public Newsbase getNewsbase() {
		return newsbase;
	}
	public void setNewsbase(Newsbase newsbase) {
		this.newsbase = newsbase;
	}
	public List<Newsclass> getNewsclasses() {
		return newsclasses;
	}
	public void setNewsclasses(List<Newsclass> newsclasses) {
		this.newsclasses = newsclasses;
	}
	public NewsPage getPage() {
		return page;
	}
	public void setPage(NewsPage page) {
		this.page = page;
	}
	//用户登录信息
	public int getLoginNums() {
		return userManager.loginUserNums();
		//return SecuManagerImpl.loginUserNums();
	}
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManagerImpl.currentUser();
	}
}
