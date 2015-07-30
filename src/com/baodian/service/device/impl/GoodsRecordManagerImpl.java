package com.baodian.service.device.impl;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.device.DepotTypeDao;
import com.baodian.dao.device.GoodsDao;
import com.baodian.dao.device.GoodsRecordDao;
import com.baodian.dao.device.UseTypeDao;
import com.baodian.model.device.GoodsRecord;
import com.baodian.model.user.User;
import com.baodian.service.device.GoodsRecordManager;
import com.baodian.service.role.impl.SecuManagerImpl;
import com.baodian.util.JSONValue;
import com.baodian.util.page.GrPage;

@Service("goodsRecordManager")
public class GoodsRecordManagerImpl implements GoodsRecordManager {
	private GoodsRecordDao grdao;
	@Resource(name="goodsRecordDao")
	public void setGrdao(GoodsRecordDao grdao) {
		this.grdao = grdao;
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
	private UseTypeDao utdao;
	@Resource(name="useTypeDao")
	public void setUtdao(UseTypeDao utdao) {
		this.utdao = utdao;
	}
	private Format ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//c
	public String save(GoodsRecord gr) {
		if(gr.getDate() == null) {
			gr.setDate( new Date());
		}
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		gr.setRecorder(new User(uid));
		grdao.add(gr);
		return "{\"status\":0,\"mess\":\"添加成功！\"}";
	}
//r
	public String findGsByPage(GrPage page) {
		List<GoodsRecord> grs = grdao.getGsByPage(page);
		StringBuilder json = new StringBuilder();
		json.append("{\"total\":" + page.getCountNums() + ",\"rows\":[");
		for(GoodsRecord gr : grs) {
			json.append("{\"id\":" + gr.getId() +
				",\"gr.date\":\"" + ft.format(gr.getDate()) +
				"\",\"gr.name\":\"" + JSONValue.escapeHTML(gr.getName()) +
				"\",\"gr.gd.id\":" + gr.getGd().getId() +
				",\"gr.dtin.id\":" + gr.getDtin().getId() +
				",\"gr.dtout.id\":" + gr.getDtout().getId() +
				",\"gr.num\":" + gr.getNum() +
				",\"recorder\":\"" + gr.getRecorder().getName() + "\"},");
		}
		if(grs.size() != 0)
			return json.substring(0, json.length()-1) + "]}";
		return json + "]}";
	}
	public String findGoods() {
		return "{\"gds\":" + gdao.getGds() + 
				",\"dts\":" + dtdao.getDtsOnStr() + 
				",\"uts\":" + utdao.getUts() + "}";
	}
	public String findGRS() {
		List<GoodsRecord> grs = grdao.getGs();
		//map(goods_id, map(depottype_id, num))
		Map<Integer, HashMap<Integer, Integer>> grcount = new HashMap<Integer, HashMap<Integer, Integer>>();
		int a = 0, b = 0;
		for(GoodsRecord gr : grs) {
			a = gr.getGd().getId();
			if(!grcount.containsKey(a)) {
				grcount.put(a, new HashMap<Integer, Integer>());
			}
			if(gr.getDtout() != null) {//来源，减
				b = gr.getDtout().getId();
				if(!grcount.get(a).containsKey(b)) {
					grcount.get(a).put(b, -gr.getNum());
				} else {
					grcount.get(a).put(b, grcount.get(a).get(b) - gr.getNum());
				}
			}
			if(gr.getDtin() != null) {//目地，加
				b = gr.getDtin().getId();
				if(!grcount.get(a).containsKey(b)) {
					grcount.get(a).put(b, +gr.getNum());
				} else {
					grcount.get(a).put(b, grcount.get(a).get(b) + gr.getNum());
				}
			}
		}
		if(grcount.isEmpty()) {
			return "{\"gds\":" + gdao.getGds() + 
					",\"dts\":" + dtdao.getDtsOnStr() + 
					",\"records\":[]}";
		}
		Iterator<Integer> it = grcount.keySet().iterator();
		StringBuilder json = new StringBuilder();
		json.append('[');
        while(it.hasNext()) {
			int s = (Integer) it.next();
			json.append("{\"id\":" + s + ",\"detail\":[");
			HashMap<Integer, Integer> dts = grcount.get(s);
			Set<Integer> dtids = dts.keySet();
			for(Iterator<Integer> itt=dtids.iterator(); itt.hasNext();) {
				int dtid = (Integer) itt.next();
				json.append("{\"id\":" + dtid +
						",\"num\":" + dts.get(dtid) + "},");
			}
			json.replace(json.length()-1, json.length(), "]},");
        }
        return "{\"gds\":" + gdao.getGds() + 
				",\"dts\":" + dtdao.getDtsOnStr() + 
				",\"records\":" + json.substring(0, json.length()-1) + "]}";
	}
//u
	public String change(GoodsRecord gr) {
		if(gr.getDate() == null) {
			gr.setDate( new Date());
		}
		int uid = SecuManagerImpl.currentId();
		if(uid == 0) {
			return "{\"status\":1,\"mess\":\"请先登录！\",\"login\":false}";
		}
		gr.setRecorder(new User(uid));
		grdao.update(gr);
		return "{\"status\":0,\"mess\":\"更改成功！\"}";
	}
//d
	public String remove(String id) {
		try {
			if(Integer.parseInt(id) != 0) {
				grdao.delete(id);
				return "{\"status\":0,\"mess\":\"删除成功！\"}";
			}
		} catch(NumberFormatException e) {}
		return "{\"status\":1,\"mess\":\"输入有误！\"}";
	}
}
