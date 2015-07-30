package com.baodian.service.record.impl;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.record.IpDao;
import com.baodian.model.record.IP;
import com.baodian.service.record.IpManager;

@Service("ipManager")
public class IpManagerImpl implements IpManager {

	private IpDao ipDao;
	
	@Resource(name="ipDao")
	public void setIpDao(IpDao ipDao) {
		this.ipDao = ipDao;
	}
	
	public String getAllIp() {
		String json = "[";
		IP Ip;
		List ip_list = ipDao.getAllIp();
		Iterator it = ip_list.iterator();
		while(it.hasNext()){
			Ip = (IP) it.next();
			json = json.concat("{\"id\":\""+Ip.getIp_head()+
					"\",\"text\":\""+Ip.getIp_head()+"\"},");
		}
		if(ip_list != null){   //去掉逗号
			json = json.substring(0, json.length()-1);
		}
		json = json.concat("]");
		return json;
	}

	@Override
	public String addIP(String ip) {
		String json = "";
		IP Ip = findIpByName(ip);
		if(Ip != null){
			json = "{\"status\":1,\"mess\":\"Ip段已存在！\"}";
		}else{
			Ip = new IP();
			Ip.setIp_head(ip);
			ipDao.addIP(Ip);
			json = "{\"status\":0,\"mess\":\"添加成功！\"}";
		}
		return json;
	}

	@Override
	public String delIP(String ip) {
		String json = "";
		IP Ip = findIpByName(ip);
		if(Ip != null){
			ipDao.delIP(ip);
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		}else{
			json = "{\"status\":1,\"mess\":\"删除失败,请检查是否存在该IP段！\"}";
		}
		return json;
	}

	@Override
	public IP findIpByName(String ip) {
		IP Ip = ipDao.findIpByName(ip);
		return Ip;
	}

}
