package com.baodian.dao.news;

import java.util.List;

import com.baodian.model.news.Newsbase;

public interface NewsbaseDao {

//c
	public void save(Newsbase newsbase);
//r
	/**
	 * 根据输入的sql统计新闻数
	 * @param sql
	 * @return
	 */
	public int countNbsOnSql(String sql);
	/**
	 * 根据输入的sql分页查找新闻(管理员级别)
	 * @param sql 条件
	 * @param begin
	 * @param num
	 * @return Newsbase除去内容、审核人，加上作者姓名
	 */
	public List<Newsbase> getNbsOnSql(String sql, int begin, int num);
	/**
	 * 分页查找新闻(游客级别)
	 * @return Newsbase除去内容、审核人、审核时间，加上作者姓名、类型名
	 */
	public List<Newsbase> getNbsOnSql2(String sql, int begin, int num);
	/**
	 * 获取审核者姓名
	 * @param id
	 */
	public String getRevwName(int id);
	/**
	 * 根据id查找新闻全部内容包括用户的信息
	 *  1.新闻的标题、正文、发表时间、点击数、是否允许回复、回复数
	 *  2.作者姓名、注册时间、头像、班级名称
	 *  3.新闻类型id 名称
	 * @param isShow true为显示的 false为全部
	 * @return 不存在返回null
	 */
	public Newsbase getNbUById(int id, boolean isShow);
	/**
	 * 根据新闻id 查找此新闻的标题、是否允许回复、回复数、作者姓名、新闻类型id
	 * @param show true为查找显示并且已审核的 false为全部
	 */
	public Newsbase getNb_atrById(int nbId, boolean show);
	/**
	 * 根据新闻id查找此新闻的作者id
	 * @return 不存在返回0
	 */
	public int getUIdByNbId(int id);
	/**
	 * 返回5条新闻
	 */
	public List<Newsbase> getNbsForIndex();
//u
	/**
	 * 为新闻添加审核人
	 * @param nbId
	 * @param uId
	 */
	public void updateReview(int nbId, int uId);
	/**
	 * 置顶或取消新闻
	 * @param nbId
	 * @param uId
	 * @param sort 置顶标记 
	 */
	public void updateSort(int nbId, int uId, String sort);
	/**
	 * 更新新闻标题，类型，
	 * @param newsbase
	 * @param uid 审核人ID
	 */
	public void update(Newsbase newsbase, int uid);
	/**
	 * 点击数加一
	 * @param nbId
	 */
	public void updateHit(int nbId);
	/**
	 * 更新最后回复人和时间
	 * @param newsid
	 * @param userId
	 */
	public void updateReply(int newsId, int userId);
//d
	public void delete(int nbId);

}
