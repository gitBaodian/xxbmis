package com.baodian.dao.document;

import java.util.List;

import com.baodian.model.document.Document;

public interface DocumentDao {

//c
	public void save(Document doc);
//r
	/**
	 * 获取指定目录中的全部文档
	 */
	public List<Document> getDocs(String dirIds);
	/**
	 * 根据id获取文档
	 */
	public Document getDoc(String docId);
	/**
	 * 根据关键字搜索文档
	 * @param owner 所有者id null为公共
	 * @param area 1为标题 2为内容 其他为两者
	 */
	public List<Document> getDocsByN(String owner, String area, String keyword);
//u
	/**
	 * 移动文档到新目录
	 */
	public void changeDir(int dirId, int docId);
	/**
	 * 只更新不为null的部分，以及date
	 * @param doc id name dir.id url content 
	 */
	public void updateDoc(Document doc);
//d
	public void delete(String docId);

}
