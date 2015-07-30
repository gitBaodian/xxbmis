package com.baodian.action.document;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.document.DocDir;
import com.baodian.service.document.DocDirManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("docdir")
@Scope("prototype")//必须注解为多态
public class DocDirAction extends ActionSupport {
	@Resource(name="docDirManager")
	private DocDirManager dm;
	private String json;
	private DocDir docdir;
	private String[] dds;
//c
	public String add_js() {
		if(docdir==null || docdir.ckName()<1) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = dm.save(docdir);
		}
		return "json";
	}
//r
	/**
	 * 公共目录
	 */
	public String list() {
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) {
			json = "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		} else {
			json = dm.findDirs(0);
		}
		return SUCCESS;
	}
	/**
	 * 个人目录，jps页面及service和list基本相同
	 */
	public String owner() {
		return SUCCESS;
	}
//u
	public String change_js() {
		if(docdir==null || docdir.ckName()<1) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			dm.changeName(docdir);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return "json";
	}
	public String changeSort_js() {
		if(dds==null || dds.length==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			dm.changeSort(dds);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return "json";
	}
//d
	public String remove_js() {
		json = dm.remove(json);
		return "json";
	}
//setget
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public DocDir getDocdir() {
		return docdir;
	}
	public void setDocdir(DocDir docdir) {
		this.docdir = docdir;
	}
	public String[] getDds() {
		return dds;
	}
	public void setDds(String[] dds) {
		this.dds = dds;
	}
	
}
