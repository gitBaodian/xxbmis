package com.baodian.dao.util;

import java.util.List;

import com.baodian.util.page.Page;

public interface UtilDao {
//c
	public void add(Object obj);
//r
	/**
	 * 检查此对象的id是否存在
	 * @return true存在	false不存在
	 */
	public boolean chkExit(int id, String obj);
	/**
	 * 根据分页查找对象
	 * @return
	 */
	public <T> List<T> getObjsByPage(String countSql, String selectSql, Page page, List<String> params);
	/**
	 * 按条件获取部分对象
	 * @param sql
	 * @param num
	 * @return
	 */
	public <T> List<T> getObjs(String sql, int num);
//u
	/**
	 * 更新或者删除对象
	 * @param updateSql
	 * @param params 带‘?’的参数
	 * @return the number of instances updated/deleted
	 */
	public int bulkUpdate(String updateSql, List<String> params);
//d
	/**
	 * 更加对象id删除此对象
	 * @param id
	 */
	public void delete(String id, String obj);
}
