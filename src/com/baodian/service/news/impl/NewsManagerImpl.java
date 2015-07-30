package com.baodian.service.news.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.news.NewsbaseDao;
import com.baodian.dao.news.NewsclassDao;
import com.baodian.dao.news.NewsreplyDao;
import com.baodian.model.news.Newsbase;
import com.baodian.model.news.Newsclass;
import com.baodian.model.news.Newsreply;
import com.baodian.service.news.NewsManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.page.ForumPage;
import com.baodian.util.page.NewsPage;

@Service("newsManager")
public class NewsManagerImpl implements NewsManager {
	private NewsclassDao newsclassDao;
	private NewsbaseDao newsbaseDao;
	private NewsreplyDao newsreplyDao;
	@Resource(name="newsclassDao")
	public void setNewsclassDao(NewsclassDao newsclassDao) {
		this.newsclassDao = newsclassDao;
	}
	@Resource(name="newsbaseDao")
	public void setNewsbaseDao(NewsbaseDao newsbaseDao) {
		this.newsbaseDao = newsbaseDao;
	}
	@Resource(name="newsreplyDao")
	public void setNewsreplyDao(NewsreplyDao newsreplyDao) {
		this.newsreplyDao = newsreplyDao;
	}
	
//r
	public List<Object> findNbsOnPage(NewsPage page) {
		List<Object> obj = new ArrayList<Object>();
		List<List<Newsbase>> newsbaseList = new ArrayList<List<Newsbase>>();
		List<Newsclass> newsclasses = newsclassDao.getNcs();
		obj.add(newsclasses);
		//组装查询语句
		StringBuilder sql = new StringBuilder();
		//只查询已审核并且显示的
		sql.append("where nb.status=0 and nb.display=1");
		
		if(page.getNcId() != 0) {//某一类型新闻
			sql.append(" and nb.nclass.id=" + page.getNcId());
		}
		if(page.getTitle()!=null) {
			page.setTitle(page.getTitle().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
			if(!page.getTitle().isEmpty())
				sql.append(" and nb.title like('%" + page.getTitle() + "%')");
		}
		//计算页数
		page.countPage(this.newsbaseDao.countNbsOnSql(sql.toString()));
		//不是首页，就查询置顶新闻
		if(page.getNcId()==0 && page.getPage()==1 && page.getTitle()==null) {
			newsbaseList.add(this.newsbaseDao.getNbsOnSql2(sql.toString() + " and nb.sort=1", 0, 6));
		}
		if(newsbaseList.size() == 0)
			newsbaseList.add(null);
		//查询
		newsbaseList.add(this.newsbaseDao.getNbsOnSql2(sql.toString(),
				(page.getPage()-1)*page.getNum(), page.getNum()));
		obj.add(newsbaseList);
		return obj;
	}
	public Newsbase findNbNrsOnPage(ForumPage page) {
		Newsbase newsbase = null;
		Boolean noRead = true;
		if(page.getPage() < 2) {//当前页小于2
			page.setPage(1);
			noRead = false;
			newsbase = this.newsbaseDao.getNbUById(page.getNbId(), true);
		} else {//只查找到标题
			newsbase = this.newsbaseDao.getNb_atrById(page.getNbId(), true);
		}
		if(newsbase == null) return null;
		page.countPage(newsbase.getReplynum());
		if(noRead && page.getPageNums()==1) {
			newsbase = this.newsbaseDao.getNbUById(page.getNbId(), true);
		}
		//查找回复
		if(newsbase.getReplynum() != 0)
			newsbase.setNewsreplies(this.newsreplyDao.getNrsUByNbId(page.getNbId(),
				(page.getPage()-1)*page.getNum(), page.getNum()));
		return newsbase;
	}
	public Newsreply findNrNbByNrId(int nrId) {
		Newsreply nr = new Newsreply(0);
		//1.判断权限
		int uid = SecuManagerImpl.canAccess("forum_change_no_rd.action");
		if(uid == 0) {
			nr.setContent("请先登录！");
		} else if (uid<0 || uid==newsreplyDao.getNrUid_byNr_id(nrId)) {
			//2.查找评论全部内容
			nr = newsreplyDao.getNrUByNrId(nrId);
			//3.查找新闻部分信息
			if(nr != null) {
				nr.setNbase(newsbaseDao.getNb_atrById(nr.getNbase().getId(), false));
			}
		} else {
			nr.setContent("没有权限更改评论！以下用户才有权限：<br>1.管理员<br>2.评论作者");
		}
		return nr;
	}
//u
	public void changeHit(int nbId) {
		this.newsbaseDao.updateHit(nbId);
	}
}
