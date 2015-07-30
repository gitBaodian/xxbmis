package com.baodian.service.news;

import java.util.List;

import com.baodian.model.news.Newsclass;

public interface NewsclassManager {

//c
	public void saveNewsclass(Newsclass newsclass);
//r
	/**
	 * 按照先后顺序读取新闻类型
	 * @return json 格式
	 *  {
	 *  	"total":12,
	 *  	"rows":[{"id":1,name:"类型1"}],},
	 *  	"footer":[{"name":"数量统计"}]
	 *  }
	 */
	public String findNcs();
	/**
	 * 查找新闻类型
	 * @return json格式保存{"id":"1","name":"test"}
	 */
	public String findNc_in();
	/**
	 * 查找新闻类型的id和name
	 */
	public List<Newsclass> findNcs_in();
//u
	/**
	 * 更新新闻类型
	 * @param newsclass
	 */
	public void changeNewsclass(Newsclass newsclass);
//d
	public String removeNewsclass(Newsclass newsclass);

}
