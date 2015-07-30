package com.baodian.service.page;

import java.util.List;

public interface PageManager {
	
	public int getRecordCount(String HQL);  //获取总记录数

	public List getCurrentPageRecord(int pageSize, int currentPage, String HQL);  //获取当前页记录
}
