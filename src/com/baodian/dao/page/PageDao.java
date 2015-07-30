package com.baodian.dao.page;

import java.util.List;

public interface PageDao {
	
	public int getRecordCount(String HQL);  //通过HQL语句 "select count(*) from table_name" 获取记录总数

	public List getCurrentPageRecord(int pageSize, int currentPage, String HQL);  //获取当前页记录

}
