package com.baodian.service.news;

import org.json.simple.JSONArray;

import com.baodian.model.news.Newsbase;
import com.baodian.util.page.NewsbasePage;

public interface NewsbaseManager {

//c
	public String saveNewsbase(Newsbase newsbase);
//r
	/**
	 * 按条件(分页,审核...)查找所有新闻
	 */
	public String findNbsByPage(NewsbasePage page);
	/**
	 * 根据id查找新闻全部内容包括用户的信息
	 *  1.新闻的标题、正文、发表时间、点击数、回复数、是否允许回复
	 *  2.作者姓名、注册时间、头像、部门名称
	 *  3.新闻类型id, name
	 *  4.审核人 如果有 id, name ,time
	 */
	public Newsbase findNbUById(int id);
	/**
	 * 首页展示的新闻
	 */
	public JSONArray findNbsForIndex();
//u
	/**
	 * 批量审核新闻
	 */
	public String changeNbReview(int[] nbIds);
	/**
	 * 更新新闻内容，判断是否是作者
	 */
	public String changeNewsbase(Newsbase newsbase);
	/**
	 * 批量置顶或取消新闻
	 * @param sort 0-取消 1-置顶
	 */
	public String changeNbSort(int[] nbIds, String sort);
//d
	/**
	 * 批量删除新闻
	 */
	public void removeNbs(int[] nbIds);
}
