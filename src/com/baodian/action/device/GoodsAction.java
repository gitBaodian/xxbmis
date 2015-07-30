package com.baodian.action.device;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.Goods;
import com.baodian.service.device.GoodsManager;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("goods")
@Scope("prototype")//必须注解为多态
public class GoodsAction extends ActionSupport {
	@Resource(name="goodsManager")
	private GoodsManager gm;
	
	private String json;
	private Goods goods;
	private String[] gds;
	
//c
	public String add_js() {
		json = gm.save(goods);
		return "json";
	}
//r
	public String list() {
		json = gm.findGoods();
		return SUCCESS;
	}
//u
	public String change_js() {
		json = gm.changeND(goods);
		return "json";
	}
	public String changeSort_js() {
		if(gds==null || gds.length==0) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			gm.changeSort(gds);
			json = "{\"status\":0,\"mess\":\"更新成功！\"}";
		}
		return "json";
	}
//d
	public String remove_js() {
		json = gm.remove(json);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public Goods getGoods() {
		return goods;
	}
	public void setGoods(Goods goods) {
		this.goods = goods;
	}
	public String[] getGds() {
		return gds;
	}
	public void setGds(String[] gds) {
		this.gds = gds;
	}
	
}
