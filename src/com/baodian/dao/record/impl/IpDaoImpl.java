package com.baodian.dao.record.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.baodian.dao.record.IpDao;
import com.baodian.model.record.IP;

@Repository("ipDao")
public class IpDaoImpl implements IpDao {
	
	private HibernateTemplate ht;
	@Resource(name="hibernateTemplate")
	public void setHt(HibernateTemplate ht) {
		this.ht = ht;
	}

	public List getAllIp() {
		List list = ht.find("from IP");
		return list;
	}

	@Override
	public void addIP(IP Ip) {
		ht.save(Ip);		
	}

	@Override
	public void delIP(String ip) {
		ht.bulkUpdate("delete IP ip where ip.ip_head=?",ip);
	}

	@Override
	public IP findIpByName(String ip) {
		List list = ht.find("from IP ip where ip.ip_head=?",ip);
		if(list.size() != 0){
			return (IP) list.get(0);
		}else{
			return null;
		}
	}

}
