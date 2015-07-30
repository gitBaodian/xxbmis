package com.baodian.service.news;

import java.util.List;

import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsreply;
import com.baodian.util.page.ForumPage;
import com.baodian.util.page.NewsPage;

public interface NewsManager {

//r
	/**
	 * 分页查找节点或某一类型的新闻
	 * @param page 页码包含节点id或类型id
	 * @return tree 节点,newsclasses 新闻类型,newsbaseList 保存结果
	 */
	public List<Object> findNbsOnPage(NewsPage page);
	
	/**
	 * 新闻内容及分页的回复
	 * @param page 分页
	 * @return
	 */
	public Newsbase findNbNrsOnPage(ForumPage page);
	/**
	 * 根据评论id查找评论，及新闻部分信息
	 * @return null->不存在	nr.id=0->没权限
	 */
	public Newsreply findNrNbByNrId(int nrId);
//u
	/**
	 * 新闻点击数加一
	 * @param nbId
	 */
	public void changeHit(int nbId);

}
