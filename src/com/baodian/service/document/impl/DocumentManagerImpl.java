package com.baodian.service.document.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.document.DocDirDao;
import com.baodian.dao.document.DocumentDao;
import com.baodian.model.document.Document;
import com.baodian.model.user.User;
import com.baodian.service.document.DocumentManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.JSONValue;
import com.baodian.util.StaticMethod;

@Service("documentManager")
public class DocumentManagerImpl implements DocumentManager {
	private DocumentDao ddao;
	@Resource(name="documentDao")
	public void setDdao(DocumentDao ddao) {
		this.ddao = ddao;
	}
	private DocDirDao dirdao;
	@Resource(name="docDirDao")
	public void setDirdao(DocDirDao dirdao) {
		this.dirdao = dirdao;
	}

//c
	public String add(Document doc) {
		if(doc==null || doc.ckName()<1 || doc.ckContent()<0 || doc.ckUrl()<0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		doc.setDate(new Date());
		int uId = SecuManagerImpl.currentId();
		if(uId == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		doc.setOwner(new User(uId));
		ddao.save(doc);
		return "{\"status\":0,\"mess\":\"添加成功！\",\"id\":" + doc.getId() + "}";
	}
//r
	public String findDocs(String dirIds) {
		if(dirIds!=null && !dirIds.isEmpty()) {
			Set<Integer> dirs = new HashSet<Integer>();
			for(String s : dirIds.split("a")) {
				try {
					dirs.add(Integer.parseInt(s));
				} catch(NumberFormatException e) {}
			}
			if(dirs.size() > 0) {
				StringBuilder json = new StringBuilder();
				for(int id : dirs) {
					json.append(id + ",");
				}
				List<Document> docs = ddao.getDocs(json.substring(0, json.length()-1));
				if(docs.size() > 0) {
					json.delete(0, json.length());
					json.append('[');
					for(Document d : docs) {
						json.append("{\"id\":-" + d.getId() +
								",\"text\":\"" + JSONValue.escapeHTML(d.getName()) +
								"\",\"date\":\"" + StaticMethod.DateToString(d.getDate()) + 
								"\",\"dirId\":" + d.getDir().getId() + "},");
					}
					return json.substring(0, json.length()-1) + ']';
				}
			}
		}
		return "[]";
	}
	public String findDirsDoc(String docId, boolean flag) {
		try {
			Integer.parseInt(docId);
		} catch(NumberFormatException e) {
			return "{\"status\":1,\"mess\":\"输入有误！\",\"id\":\"" + docId + "\"}";
		}
		Document doc = ddao.getDoc(docId);
		if(doc == null) {
			return "{\"status\":1,\"mess\":\"文档不存在！\",\"id\":\"" + docId + "\"}";
		}
		User dirOwner = doc.getDir().getOwner();
		if(dirOwner != null) {//个人目录
			if(dirOwner.getId() != SecuManagerImpl.currentId()) {
				doc = null;
				return "{\"status\":1,\"mess\":\"没有权限查看！\",\"id\":\"" + docId + "\"}";
			}
			if(flag) {
				return "{\"dirs\":" + dirdao.getDirs(dirOwner.getId()) +
						",\"doc\":" + docToString(doc) + "}";
			} else {
				return docToString(doc);
			}
		} else {
			if(flag) {
				return "{\"dirs\":" + dirdao.getDirs(0) +
						",\"doc\":" + docToString(doc) + "}";
			} else {
				return docToString(doc);
			}
		}
	}
	private String docToString(Document doc) {
		return "{\"status\":0,\"id\":" + doc.getId() +
				",\"name\":\"" + JSONValue.escapeHTML(doc.getName()) +
				"\",\"date\":\"" + StaticMethod.DateToString(doc.getDate()) +
				"\",\"dirId\":" + doc.getDir().getId() +
				",\"dirName\":\"" + JSONValue.escape(doc.getDir().getName()) +
				"\",\"url\":\"" + JSONValue.escape(doc.getUrl()) +
				"\",\"content\":\"" + JSONValue.escape(doc.getContent()) +
				"\",\"uId\":" + doc.getOwner().getId() +
				",\"uName\":\"" + JSONValue.escapeHTML(doc.getOwner().getName()) + "\"}";
	}
	public String findDocsByN(String owner, String area, String keyword) {
		List<Document> docs = ddao.getDocsByN(owner, area, keyword);
		if(docs.size() > 0) {
			StringBuilder json = new StringBuilder();
			json.append('[');
			for(Document d : docs) {
				json.append("{\"id\":-" + d.getId() +
						",\"text\":\"" + JSONValue.escapeHTML(d.getName()) +
						"\",\"date\":\"" + StaticMethod.DateToString(d.getDate()) + 
						"\",\"dirId\":" + d.getDir().getId() + "},");
			}
			return json.substring(0, json.length()-1) + ']';
		}
		return "[]";
	}
//u
	public String changeDir(String json) {
		if(json!=null && !json.isEmpty()) {
			String[] str = json.split("A");
			if(str.length == 2) {
				int dirId = 0;
				try {
					dirId = Integer.parseInt(str[0]);
				} catch(NumberFormatException e) {}
				if(dirId != 0) {
					Set<Integer> docIds = new HashSet<Integer>();
					for(String s : str[1].split("a")) {
						try {
							docIds.add(Integer.parseInt(s));
						} catch(NumberFormatException e) {}
					}
					if(docIds.size() > 0) {
						for(int docId : docIds) {
							ddao.changeDir(dirId, docId);
						}
						return "{\"status\":0,\"mess\":\"更新成功！\"}";
					}
				}
				
			}
		}
		return "{\"status\":1,\"mess\":\"输入有误！\"}";
	}
	public String changeDoc(Document doc) {
		int length = doc.ckName();
		if(length==-2 || length==0 || doc.ckContent()==-2 || doc.ckUrl()==-2) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		ddao.updateDoc(doc);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
//d
	public String remove(String docId) {
		try {
			Integer.parseInt(docId);
		} catch(NumberFormatException e) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		ddao.delete(docId);
		return "{\"status\":0,\"mess\":\"删除成功！\"}";
	}
}
