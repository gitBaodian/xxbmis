package com.baodian.service.page.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.page.PageDao;
import com.baodian.service.page.PageManager;

@Service("pageManager")
public class PageManagerImpl implements PageManager {
	
	private PageDao pageDao;
	@Resource(name="pageDao")
	public void setPageDao(PageDao pageDao) {
		this.pageDao = pageDao;
	}

	public int getRecordCount(String HQL) {              //获取总记录数
		int recordCount = pageDao.getRecordCount(HQL);
		return recordCount;
	}
	
	public List getCurrentPageRecord(int pageSize, int currentPage, String HQL) {    //获取当前页的记录
		List records = pageDao.getCurrentPageRecord(pageSize, currentPage, HQL);
		return records;
	}
	
}
