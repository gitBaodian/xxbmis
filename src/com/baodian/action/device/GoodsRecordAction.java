package com.baodian.action.device;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.GoodsRecord;
import com.baodian.service.device.GoodsRecordManager;
import com.baodian.util.page.GrPage;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("goodsrecord")
@Scope("prototype")//必须注解为多态
public class GoodsRecordAction extends ActionSupport {
	@Resource(name="goodsRecordManager")
	private GoodsRecordManager grm;
	private String json;
	private GoodsRecord gr;
	private GrPage page;
//c
	public String save_js() {
		if(gr==null || gr.ckName()<1 || gr.getGd()==null) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = grm.save(gr);
		}
		return "json";
	}
//r
	public String list() {
		json = grm.findGoods();
		return SUCCESS;
	}
	public String list_js() {
		if(page == null)
			page = new GrPage();
		json = grm.findGsByPage(page);
		return "json";
	}
	public String count() {
		json = grm.findGRS();
		return SUCCESS;
	}
//u
	public String change_js() {
		if(gr==null || gr.ckName()<1 || gr.getGd()==null) {
			json = "{\"status\":1,\"mess\":\"输入有误！\"}";
		} else {
			json = grm.change(gr);
		}
		return "json";
	}
//d
	public String remove_js() {
		json = grm.remove(json);
		return "json";
	}
//set get
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public GoodsRecord getGr() {
		return gr;
	}
	public void setGr(GoodsRecord gr) {
		this.gr = gr;
	}
	public GrPage getPage() {
		return page;
	}
	public void setPage(GrPage page) {
		this.page = page;
	}
	
}
