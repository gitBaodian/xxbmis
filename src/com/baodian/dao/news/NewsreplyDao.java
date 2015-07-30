package com.baodian.dao.news;

import java.util.List;

import com.baodian.model.news.Newsreply;

public interface NewsreplyDao {

//c
	public void save(Newsreply newsreply);
//r
	/**
	 * 根据新闻Id 分页查找回复时间、内容、作者姓名、注册时间、头像、班级名称
	 */
	public List<Newsreply> getNrsUByNbId(int nbId, int begin, int num);
	/**
	 * 根据评论Id 查找评论时间、内容、作者姓名、注册时间、头像、班级名称
	 */
	public Newsreply getNrUByNrId(int nrId);
	/**
	 * 根据评论id查找，此新闻作者id
	 */
	public int getNbUid_byNr_id(int id);
	/**
	 * 根据评论id查找，此评论作者id
	 */
	public int getNrUid_byNr_id(int id);
//u
	/**
	 * 更新评论内容
	 */
	public void update(Newsreply newsreply);
//d
	/**
	 * 只删除评论内容，不删除评论
	 */
	public void delete(Newsreply newsreply);

}
