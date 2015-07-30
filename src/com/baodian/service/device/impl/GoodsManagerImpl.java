package com.baodian.service.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.device.GoodsDao;
import com.baodian.model.device.Goods;
import com.baodian.service.device.GoodsManager;
import com.baodian.util.JSONValue;

@Service("goodsManager")
public class GoodsManagerImpl implements GoodsManager {
	private GoodsDao gdao;
	@Resource(name="goodsDao")
	public void setGdao(GoodsDao gdao) {
		this.gdao = gdao;
	}
//c
	public String save(Goods goods) {
		if(goods==null || goods.ckName()<1 || goods.ckDetail()<0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		gdao.save(goods);
		return "{\"status\":0,\"id\":" + goods.getId() + ",\"mess\":\"添加成功！\"}";
	}
//r
	public String findGoods() {
		StringBuilder json = new StringBuilder();
		json.append('[');
		List<Goods> gds = gdao.getGoods();
		for(Goods gd : gds) {
			json.append("{\"id\":" + gd.getId() +
					",\"name\":\"" + JSONValue.escape(gd.getName()) +
					"\",\"detail\":\"" + JSONValue.escape(gd.getDetail()) +
					"\",\"sort\":" + gd.getSort() + ",\"open\":true");
			if(gd.getParent() != null)
				json.append(",\"pId\": " + gd.getParent().getId());
			json.append("},");
		}
		if(gds.size() > 0) {
			return json.substring(0, json.length()-1) + ']';
		} else {
			return "[]";
		}
	}
//u
	public String changeND(Goods goods) {
		if(goods==null || goods.getId()==0 ||
				goods.ckName()<1 || goods.ckDetail()<0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		}
		gdao.updateND(goods);
		return "{\"status\":0,\"mess\":\"更新成功！\"}";
	}
	public void changeSort(String[] gds) {
		for(String goods : gds) {
			String[] ids = goods.split("_");
			gdao.updateSort(Integer.parseInt(ids[0]),
					Integer.parseInt(ids[1]), Integer.parseInt(ids[2]));
		}
	}
//d
	public String remove(String gid) {
		int id = 0;
		try {
			id = Integer.parseInt(gid);
		} catch(NumberFormatException e) {}
		if(id == 0) {
			return "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			gdao.delete(id);
			return "{\"status\":0,\"mess\":\"删除成功！\"}";
		}
	}
}
