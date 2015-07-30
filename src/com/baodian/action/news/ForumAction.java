package com.baodian.action.news;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsreply;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.service.user.UserManager;
import com.baodian.service.news.NewsManager;
import com.baodian.util.page.ForumPage;

@SuppressWarnings("serial")
@Controller("forum")
@Scope("prototype")//必须注解为多态
public class ForumAction extends ActionSupport{
	private String json;
	private ForumPage page;
	private Newsbase newsbase;
	private Newsreply newsreply;
	@Resource(name="newsManager")
	private NewsManager newsManager;
	@Resource(name="userManager")
	private UserManager userManager;
//r	
	public String list_rd() {
		if(page==null || page.getNbId()==0) {
			json = "news_list_rd.action";
			return "anyaction";
		}
		this.newsbase = newsManager.findNbNrsOnPage(page);
		//未查找到此新闻
		if(newsbase == null) {
			json = "news_list_rd.action";
			return "anyaction";
		}
		if(page.getPage() == 1)
			newsManager.changeHit(newsbase.getId());
		return SUCCESS;
	}
//u
	/**
	 * 打开评论新闻更改页面，错误处理未完成
	 */
	public String change_no_rd() {
		if(newsreply==null || newsreply.getId()==0) {
			json = "输入有误：评论id号为空！";
			return "jhtml";
		}
		this.newsreply = newsManager.findNrNbByNrId(newsreply.getId());
		if(newsreply == null) {
			json = "此评论不存在！";
			return "jhtml";
		} else if(newsreply.getId()==0) {
			json = newsreply.getContent();
			return "jhtml";
		} else {
			return SUCCESS;
		}
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Newsbase getNewsbase() {
		return newsbase;
	}
	public void setNewsbase(Newsbase newsbase) {
		this.newsbase = newsbase;
	}
	public ForumPage getPage() {
		return page;
	}
	public void setPage(ForumPage page) {
		this.page = page;
	}
	public Newsreply getNewsreply() {
		return newsreply;
	}
	public void setNewsreply(Newsreply newsreply) {
		this.newsreply = newsreply;
	}
	//用户登录信息
	public int getLoginNums() {
		return this.userManager.loginUserNums();
	}
	public org.springframework.security.core.userdetails.User getUser() {
		return SecuManagerImpl.currentUser();
	}
}
