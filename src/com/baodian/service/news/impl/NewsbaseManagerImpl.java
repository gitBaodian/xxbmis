package com.baodian.service.news.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.baodian.dao.news.NewsbaseDao;
import com.baodian.dao.news.NewsclassDao;
import com.baodian.model.news.Newsbase;
import com.baodian.model.user.User;
import com.baodian.service.news.NewsbaseManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.StaticMethod;
import com.baodian.util.page.NewsbasePage;

@Service("newsbaseManager")
public class NewsbaseManagerImpl implements NewsbaseManager {
	private NewsbaseDao newsbaseDao;
	@Resource(name="newsbaseDao")
	public void setNewsbaseDao(NewsbaseDao newsbaseDao) {
		this.newsbaseDao = newsbaseDao;
	}
	private NewsclassDao newsclassDao;
	@Resource(name="newsclassDao")
	public void setNewsclassDao(NewsclassDao newsclassDao) {
		this.newsclassDao = newsclassDao;
	}

//c
	public String saveNewsbase(Newsbase newsbase) {
		if(newsbase==null || newsbase.getNclass()==null ||
				newsbase.ckTitle()<1 || newsbase.ckContent()<1) {
			return StaticMethod.inputError;
		}
		int userId = SecuManagerImpl.currentId();
		if(userId == 0) {
			return StaticMethod.loginError;
		}
		newsbase.setAuthor(new User(userId));
		newsbase.setStatus(this.newsclassDao.getNc_stById(newsbase.getNclass().getId()));
		newsbase.setTitle(newsbase.getTitle().replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", ""));
		if(newsbase.getTitle().isEmpty()) {
			newsbase.setTitle("无标题！");
			
		}
		newsbase.setPublishtime(new Date());
		newsbase.setReplyer(new User(userId));
		newsbase.setReplytime(new Date());
		
		this.newsbaseDao.save(newsbase);

		return "{\"status\":\"0\",\"nstatus\":" + newsbase.getStatus() + ",\"id\":" + newsbase.getId() + "}";
		
	}
//r
	public String findNbsByPage(NewsbasePage page) {
		//组装查询语句
		StringBuilder sql = new StringBuilder();
		if(page.getNcId() != 0)//某一类型新闻
			sql.append("  and nb.nclass.id=" + page.getNcId());
		if(page.getStatus() != 2)
			sql.append("  and nb.status=" + page.getStatus());
		if(page.getSort() != 2)
			sql.append("  and nb.sort=" + page.getSort());
		if(page.getReply() != 2)
			sql.append("  and nb.reply=" + page.getReply());
		if(page.getDisplay() != 2)
			sql.append("  and nb.display=" + page.getDisplay());
		if(page.getTitle()!=null && !page.getTitle().isEmpty()) {
			sql.append("  and nb.title like('%" + page.getTitle()
					.replaceAll("'", "").replaceAll("\"", "").replaceAll("\\\\", "") + "%')");
		}
		//查询全部,加上'where',去掉前面的'  and'
		if(sql.length() != 0)
			sql.replace(0, 5, "where");
		//计算页数
		int countNums = this.newsbaseDao.countNbsOnSql(sql.toString());
		if(countNums == 0)
			return "{\"total\":0,\"rows\":[]}";
		page.countPage(countNums);
		List<Newsbase> newsbases = this.newsbaseDao.getNbsOnSql(sql.toString(),
				(page.getPage()-1)*page.getNum(), page.getNum());
		sql.setLength(0);
		sql.append("{\"total\":" + countNums + ",\"rows\":[");
		for(int i=0; i<newsbases.size(); i++) {
			sql.append("{\"id\":" + newsbases.get(i).getId() +
					",\"title\":\"" + newsbases.get(i).getTitle() +
					"\",\"ncid\":" + newsbases.get(i).getNclass().getId() +
					",\"author\":\"" + newsbases.get(i).getAuthor().getName() +
					"\",\"publishtime\":\"" + newsbases.get(i).getPublishtime() +
					"\",\"hit\":" + newsbases.get(i).getHit() +
					",\"display\":" + newsbases.get(i).getDisplay() +
					",\"sort\":" + newsbases.get(i).getSort() +
					",\"reply\":" + newsbases.get(i).getReply() +
					",\"replynum\":" + newsbases.get(i).getReplynum() +
					",\"status\":" + newsbases.get(i).getStatus() +
					",\"reviewer\":\"");
			if(newsbases.get(i).getReviewer() == null) {
				if(newsbases.get(i).getStatus() == 1)
					sql.append("未审核");
				else
					sql.append("直接发布");
			} else {
				sql.append(newsbaseDao.getRevwName(newsbases.get(i).getReviewer().getId()));
			}
			sql.append("\",\"reviewtime\":\""+newsbases.get(i).getReviewtime()+"\"},");
		}
		return sql.substring(0, sql.length()-1) + "]}";
	}
	public Newsbase findNbUById(int id) {
		Newsbase newsbase = this.newsbaseDao.getNbUById(id, false);
		if(newsbase == null) return null;
		if(newsbase.getReviewer() == null) {
			if(newsbase.getStatus() == 1)
				newsbase.setReviewer(new User(0,"未审核"));
			else
				newsbase.setReviewer(new User(0,"直接发布"));
		} else {
			newsbase.getReviewer().setName(newsbaseDao.getRevwName(newsbase.getReviewer().getId()));
		}
		return newsbase;
	}
	@SuppressWarnings("unchecked")
	public JSONArray findNbsForIndex() {
		JSONArray array = new JSONArray();
		List<Newsbase> nbs = this.newsbaseDao.getNbsForIndex();
		for(Newsbase nb : nbs) {
			JSONObject json = new JSONObject();
			json.put("id", nb.getId());
			json.put("rnum", nb.getReplynum());
			json.put("title", nb.getTitle());
			json.put("pbtime", StaticMethod.DateToString(nb.getPublishtime()));
			json.put("rptime", StaticMethod.DateToString(nb.getReplytime()));
			array.add(json);
		}
		return array;
	}
//u
	public String changeNbReview(int[] nbIds) {
		org.springframework.security.core.userdetails.User u = SecuManagerImpl.currentUser();
		if(u == null) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		for(int nbId : nbIds)
			this.newsbaseDao.updateReview(nbId, u.getId()[0]);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return "{\"status\":0,\"mess\":\"审核成功！\"," +
				"\"name\":\""+u.getStr()[0]+"\",\"time\":\""+(format.format(new Date()))+"\"}";
	}
	public String changeNewsbase(Newsbase newsbase) {
		int uid = SecuManagerImpl.canAccess("newsbase_change_no.action");
		if(uid == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
			//判断是否有权限
		} else if (uid < 0) {
			newsbaseDao.update(newsbase, uid);
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			return "{\"status\":0,\"mess\":\"更新成功！\"," +
				"\"name\":\""+SecuManagerImpl.currentName()+"\",\"time\":\""+(format.format(new Date()))+"\"}";
			//是否为作者
		} else if(uid==newsbaseDao.getUIdByNbId(newsbase.getId())) {
			newsbaseDao.update(newsbase, uid);
			return "{\"status\":0,\"mess\":\"更新成功！\"," + "\"name\":1}";
		}
		return "{\"status\":1,\"mess\":\"没有权限！\"}";
	}
	public String changeNbSort(int[] nbIds, String sort) {
		org.springframework.security.core.userdetails.User u = SecuManagerImpl.currentUser();
		if(u == null) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		if(!sort.equals("1"))
			sort = "0";
		for(int nbId : nbIds)
			this.newsbaseDao.updateSort(nbId, u.getId()[0], sort);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return "{\"status\":0,\"mess\":\"置顶操作成功！\"," +
				"\"name\":\""+u.getStr()[0]+"\",\"time\":\""+(format.format(new Date()))+"\"}";
	}
//d
	public void removeNbs(int[] nbIds) {
		for(int nbId : nbIds)
			this.newsbaseDao.delete(nbId);
	}
	
}
