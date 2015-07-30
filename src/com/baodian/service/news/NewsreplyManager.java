package com.baodian.service.news;

import com.baodian.model.news.Newsreply;

public interface NewsreplyManager {

//c
	public String saveNewsreply(Newsreply newsreply);
//u
	/**
	 * 更改评论内容
	 */
	public String changeNr(Newsreply newsreply);
//d
	public String remove(Newsreply newsreply);

}
