package com.baodian.service.record;

import com.baodian.model.record.IP;

public interface IpManager {
	
	public String getAllIp();
	
	public String addIP(String ip);
	
	public String delIP(String ip);
	
	public IP findIpByName(String ip);
}
