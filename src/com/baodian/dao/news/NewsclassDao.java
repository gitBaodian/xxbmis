package com.baodian.dao.news;

import java.util.List;

import com.baodian.model.news.Newsclass;

public interface NewsclassDao {
	
//c
	public void save(Newsclass newsclass);
//r
	/**
	 * 按照先后顺序读取新闻类型
	 * @return 完整信息
	 */
	public List<Newsclass> getNcs();
	/**
	 *	返回新闻类型中的新闻数量 
	 */
	public int getCountByNcId(int treeId);
	/**
	 * 显示的新闻类型(id, name, position)
	 * @return
	 */
	public List<Newsclass> getNcsOnDi();
	/**
	 * 根据id查找此新闻类型的审核要求
	 */
	public int getNc_stById(int id);
//u
	/**
	 * 更新新闻类型
	 * @param newsclass
	 */
	public void update(Newsclass newsclass);
//d
	public void delete(Newsclass newsclass);

}
