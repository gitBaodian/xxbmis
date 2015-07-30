package com.baodian.service.document;

import com.baodian.model.document.Document;

public interface DocumentManager {

//c
	public String add(Document doc);
//r
	/**
	 * 获取若干目录中的文档，为避免和目录id重复，文档id使用负数
	 * @param dirId
	 * @return [{id:-1,text,date,dirId}]
	 */
	public String findDocs(String dirIds);
	/**
	 * 查找文档，以及对应的目录
	 * @param flag true查找目录，false只查找文档
	 * @return {dirs:[{id,name,pId}],
	 * 	doc:{status,id,name,dirId,dirName,url,content,date,uId,uName}}
	 */
	public String findDirsDoc(String docId, boolean flag);
	/**
	 * 根据关键字搜索文档
	 * @param owner 所有者id null为公共
	 * @param area 1为标题 2为内容 其他为两者
	 */
	public String findDocsByN(String owner, String area, String keyword);
//u
	/**
	 * 移动文档到新目录
	 * @param json 1A2a3 (目录id A 文档id a)
	 */
	public String changeDir(String json);
	/**
	 * 更新文档内容
	 * @param doc 
	 * @return {status,mess}
	 */
	public String changeDoc(Document doc);
//d
	public String remove(String docId);

}
