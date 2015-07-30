package com.baodian.dao.document;

import com.baodian.model.document.DocDir;

public interface DocDirDao {

//c
	public void add(DocDir d);
//r
	/**
	 * 查找个人目录
	 * @param id 为0时为公共目录
	 */
	public String getDirs(int id);
//u
	public void updateName(DocDir d);
	/**
	 * 更新顺序
	 * @param pid -1不更新 0移到顶层 其他更新
	 * @param sort -1不更新 其他更新
	 */
	public void updateSort(int id, int pid, int sort);
//d
	/**
	 * 子目录数量
	 */
	public Long findChildNum(String id);
	/**
	 * 文档数量
	 */
	public Long findDocNum(String id);
	/**
	 * 删除文档目录
	 */
	public void delete(String id);
}
