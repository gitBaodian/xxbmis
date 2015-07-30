package com.baodian.dao.record;

import java.util.List;

import com.baodian.model.record.IP;

public interface IpDao {
	
	public List getAllIp();
	
	public void addIP(IP Ip);
	
	public void delIP(String ip);
	
	public IP findIpByName(String ip);

}
