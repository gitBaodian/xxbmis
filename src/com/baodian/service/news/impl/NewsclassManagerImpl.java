package com.baodian.service.news.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.news.NewsclassDao;
import com.baodian.model.news.Newsclass;
import com.baodian.service.news.NewsclassManager;

@Service("newsclassManager")
public class NewsclassManagerImpl implements NewsclassManager {
	
	private NewsclassDao newsclassDao;
	@Resource(name="newsclassDao")
	public void setNewsclassDao(NewsclassDao newsclassDao) {
		this.newsclassDao = newsclassDao;
	}
//c
	public void saveNewsclass(Newsclass newsclass) {
		this.newsclassDao.save(newsclass);
	}
//r
	public String findNcs() {
		List<Newsclass> newsclasses = newsclassDao.getNcs();
		StringBuilder json = new StringBuilder();
		json.append("{\"total\":" + newsclasses.size() + ",\"rows\":[");
		int nums = 0;
		for(int i=0; i<newsclasses.size(); i++) {
			json.append("{\"newsclass.id\":" + newsclasses.get(i).getId() +
					",\"newsclass.name\":\"" + newsclasses.get(i).getName() +
					"\",\"newsclass.position\":" + newsclasses.get(i).getPosition() +
					",\"newsclass.display\":\"");
			if(newsclasses.get(i).getDisplay()==1) {
				json.append("显示");
			}
			json.append("\",\"newsclass.review\":\"");
			if(newsclasses.get(i).getReview()==1) {
				json.append("需要");
			}
			json.append("\",\"num\":" + newsclasses.get(i).getNum() + "},");
			nums = nums + newsclasses.get(i).getNum();
		}
		if(newsclasses.size() != 0)
			json.deleteCharAt(json.length()-1);
		return json.toString() + "],\"footer\":[{\"newsclass.name\":\"数量统计\"," +
				"\"newsclass.position\":\"-" + newsclasses.size() +
				"-\",\"num\":\"-" + nums + "-\"}]}";
	}
	public String findNc_in() {
		List<Newsclass> newsclasses = this.newsclassDao.getNcsOnDi();
		StringBuilder json = new StringBuilder();
		json.append('[');
		for(int i=0;i<newsclasses.size();i++) {
			json.append("{\"id\":" + newsclasses.get(i).getId() + "," +
					"\"name\":\"" + newsclasses.get(i).getName() + "\"},");
		}
		if(json.length() != 1)
			return json.substring(0, json.length()-1) + ']';
		return json.toString() + ']';
	}
	public List<Newsclass> findNcs_in() {
		return this.newsclassDao.getNcsOnDi();
	}
//u
	public void changeNewsclass(Newsclass newsclass) {
		newsclassDao.update(newsclass);
	}
//d
	public String removeNewsclass(Newsclass newsclass) {
		int nbNums = newsclassDao.getCountByNcId(newsclass.getId());
		if(nbNums != 0) {
			return "{\"status\":1,\"mess\":\"存在 " + nbNums + " 条新闻，不能删除!!!\"}";
		} else {
			this.newsclassDao.delete(newsclass);
			return "{\"status\":0,\"mess\":\"删除成功!!!\"}";
		}
	}
}
