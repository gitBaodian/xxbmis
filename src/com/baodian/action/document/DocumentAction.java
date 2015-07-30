package com.baodian.action.document;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.document.Document;
import com.baodian.service.document.DocDirManager;
import com.baodian.service.document.DocumentManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("document")
@Scope("prototype")//必须注解为多态
public class DocumentAction extends ActionSupport {
	@Resource(name="documentManager")
	private DocumentManager docm;
	@Resource(name="docDirManager")
	private DocDirManager dirm;
	private String json;
	private String docStr;
	private Document doc;
//c
	public String add() {
		docStr = dirm.findDirs(0);
		return SUCCESS;
	}
	public String add_js() {
		json = docm.add(doc);
		return "json";
	}
//r
	public String list() {
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) {
			json = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		} else {
			json = dirm.findDirs(0);
		}
		return SUCCESS;
	}
	public String list_js() {
		json = docm.findDocs(json);
		return "json";
	}
	
	public String read() {
		docStr = docm.findDirsDoc(json, false);
		if(docStr.startsWith("{\"status\":1")) {//有错误返回
			json = docStr;
			return "json";
		}
		return SUCCESS;
	}
	public String search_js() {
		if(json==null || json.length()==0) {
			json = "0";
		}
		if(docStr==null || docStr.length()<2 || docStr.length()>30) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = docm.findDocsByN("null", json, docStr);
		}
		return "json";
	}
//u
	public String changeDir_js() {
		json = docm.changeDir(json);
		return "json";
	}
	public String change() {
		json = docm.findDirsDoc(json, true);
		if(json.startsWith("{\"status\":1")) {//有错误返回
			return "json";
		}
		return SUCCESS;
	}
	public String change_js() {
		if(doc == null) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = docm.changeDoc(doc);
		}
		return "json";
	}
//d
	public String remove_js() {
		json = docm.remove(json);
		return "json";
	}
//setget
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public void setDocStr(String docStr) {
		this.docStr = docStr;
	}
	public String getDocStr() {
		return docStr;
	}
}
