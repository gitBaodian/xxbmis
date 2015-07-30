package com.baodian.service.news.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.news.NewsbaseDao;
import com.baodian.dao.news.NewsreplyDao;
import com.baodian.model.news.Newsreply;
import com.baodian.model.user.User;
import com.baodian.service.news.NewsreplyManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.StaticMethod;

@Service("newsreplyManager")
public class NewsreplyManagerImpl implements NewsreplyManager {
	
	private NewsreplyDao newsreplyDao;
	@Resource(name="newsreplyDao")
	public void setNewsreplyDao(NewsreplyDao newsreplyDao) {
		this.newsreplyDao = newsreplyDao;
	}
	private NewsbaseDao newsbaseDao;
	@Resource(name="newsbaseDao")
	public void setNewsbaseDao(NewsbaseDao newsbaseDao) {
		this.newsbaseDao = newsbaseDao;
	}
//c
	public String saveNewsreply(Newsreply newsreply) {
		if(newsreply==null
				|| newsreply.getNbase().getId() == 0
				|| newsreply.ckContent()<1) {
			return StaticMethod.inputError;
		}
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			return StaticMethod.loginError;
		}
		newsreply.setAuthor(new User(userId));
		this.newsreplyDao.save(newsreply);
		this.newsbaseDao.updateReply(newsreply.getNbase().getId(), userId);
		return StaticMethod.addSucc;
	}
//r
//u
	public String changeNr(Newsreply newsreply) {
		int uid = SecuManagerImpl.canAccess("newsreply_change_no.action");
		if(uid == 0) {
			return StaticMethod.loginError;
		} else if (uid<0 || uid==newsreplyDao.getNrUid_byNr_id(newsreply.getId())) {
			this.newsreplyDao.update(newsreply);
			return StaticMethod.changeSucc;
		}
		return StaticMethod.authError;
	}
//d
	public String remove(Newsreply newsreply) {
		int uid = SecuManagerImpl.canAccess("newsreply_remove_no.action");
		if(uid == 0) {
			return StaticMethod.loginError;
		} else if (uid < 0) {
			this.newsreplyDao.delete(newsreply);
			return StaticMethod.removeSucc;
		}
		//判断是否是新闻作者或者评论作者
		if(uid == newsreplyDao.getNbUid_byNr_id(newsreply.getId()) ||
				uid == newsreplyDao.getNrUid_byNr_id(newsreply.getId())) {
			this.newsreplyDao.delete(newsreply);
			return StaticMethod.removeSucc;
		}
		return StaticMethod.authError;
	}
}
