package com.baodian.service.device.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.device.DepotTypeDao;
import com.baodian.dao.device.GoodsDao;
import com.baodian.dao.device.UseTypeDao;
import com.baodian.model.device.UseType;
import com.baodian.service.device.UseTypeManager;

@Service("useTypeManager")
public class UseTypeManagerImpl implements UseTypeManager {
	private UseTypeDao utdao;
	@Resource(name="useTypeDao")
	public void setUtdao(UseTypeDao utdao) {
		this.utdao = utdao;
	}
	private GoodsDao gdao;
	@Resource(name="goodsDao")
	public void setGdao(GoodsDao gdao) {
		this.gdao = gdao;
	}
	private DepotTypeDao dtdao;
	@Resource(name="depotTypeDao")
	public void setDtdao(DepotTypeDao dtdao) {
		this.dtdao = dtdao;
	}
//c
	public String save(UseType ut) {
		if(ut==null || ut.ckName()<1 || ut.getGd()==null) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		utdao.add(ut);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
//r
	public String findUts() {
		return utdao.getUts();
	}
	public String findGoods() {
		return "{\"gds\":" + gdao.getGds() + ",\"dts\":" + dtdao.getDtsOnStr() + "}";
	}
//u
	public String change(UseType ut) {
		if(ut==null || ut.ckName()<1 || ut.getGd()==null) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		utdao.update(ut);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
//d
	public String remove(String id) {
		try {
			if(Integer.parseInt(id) != 0) {
				utdao.delete(id);
				return "{\"status\":0,\"mess\":\"删除成功！\"}";
			}
		} catch(NumberFormatException e) {}
		return "{\"status\":1,\"mess\":\"输入有误！\"}";
	}

}
