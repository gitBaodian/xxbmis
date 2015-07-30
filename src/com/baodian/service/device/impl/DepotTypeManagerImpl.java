package com.baodian.service.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.device.DepotTypeDao;
import com.baodian.model.device.DepotType;
import com.baodian.service.device.DepotTypeManager;
import com.baodian.util.JSONValue;

@Service("depotTypeManager")
public class DepotTypeManagerImpl implements DepotTypeManager {
	private DepotTypeDao dtdao;
	@Resource(name="depotTypeDao")
	public void setDtdao(DepotTypeDao dtdao) {
		this.dtdao = dtdao;
	}
//c
	public String save(DepotType dt) {
		if(dt==null || dt.ckName()<1) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		dtdao.add(dt);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
//r
	public String findDts() {
		StringBuilder json = new StringBuilder();
		json.append('[');
		List<DepotType> dts = dtdao.getDts();
		for(DepotType dt : dts) {
			json.append("{\"id\":" + dt.getId() +
					",\"name\":\"" + JSONValue.escapeHTML(dt.getName()) +
					"\",\"sort\":" + dt.getSort() + "},");
		}
		if(dts.size() > 0) {
			return json.substring(0, json.length()-1) + ']';
		} else {
			return "[]";
		}
	}
//u
	public String change(DepotType dt) {
		if(dt==null || dt.getId()==0 || dt.ckName()<1) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		dtdao.update(dt);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
//d
	public String remove(String id) {
		try {
			if(Integer.parseInt(id) != 0) {
				dtdao.delete(id);
				return "{\"status\":0,\"mess\":\"删除成功！\"}";
			}
		} catch(NumberFormatException e) {}
		return "{\"status\":1,\"mess\":\"输入有误！\"}";
	}

}
