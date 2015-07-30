package com.baodian.service.document;

import com.baodian.model.document.DocDir;

public interface DocDirManager {

//c
	public String save(DocDir d);
//r
	/**
	 * 查找个人目录
	 * @param id 用户id，为0时为公共目录
	 * @return [{id,name,sort,pId}]
	 */
	public String findDirs(int id);
//u
	public void changeName(DocDir d);
	/**
	 * 更改顺序
	 * @param dds 'id_pId_sort'
	 */
	public void changeSort(String[] dds);
//d
	public String remove(String id);

}
