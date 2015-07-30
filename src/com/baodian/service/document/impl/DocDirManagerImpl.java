package com.baodian.service.document.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.document.DocDirDao;
import com.baodian.model.document.DocDir;
import com.baodian.service.document.DocDirManager;

@Service("docDirManager")
public class DocDirManagerImpl implements DocDirManager {
	private DocDirDao ddao;
	@Resource(name="docDirDao")
	public void setDdao(DocDirDao ddao) {
		this.ddao = ddao;
	}
//c
	public String save(DocDir d) {
		ddao.add(d);
		return "{\"status\":0,\"id\":\"" + d.getId() + "\"}";
	}
//r
	public String findDirs(int id) {
		return ddao.getDirs(id);
	}
//u
	public void changeName(DocDir d) {
		ddao.updateName(d);
	}
	public void changeSort(String[] dds) {
		for(String dir : dds) {
			String[] ids = dir.split("_");
			ddao.updateSort(Integer.parseInt(ids[0]),
					Integer.parseInt(ids[1]), Integer.parseInt(ids[2]));
		}
	}
//d
	public String remove(String id) {
		try {
			if(Integer.parseInt(id) != 0) {
				Long num = ddao.findChildNum(id);
				if(num != 0) {
					return "{\"status\":1,\"mess\":\"此目录下存在 " + num + " 个目录！\"}";
				}
				num = ddao.findDocNum(id);
				if(num != 0) {
					return "{\"status\":1,\"mess\":\"此目录中存在 " + num + " 篇文档！\"}";
				}
				ddao.delete(id);
				return "{\"status\":0,\"mess\":\"删除成功！\"}";
			}
		} catch(NumberFormatException e) {}
		return "{\"status\":1,\"mess\":\"输入有误！\"}";
	}
}
