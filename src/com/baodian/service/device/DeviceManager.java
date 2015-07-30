package com.baodian.service.device;

import java.io.InputStream;
import java.util.List;

import com.baodian.model.device.Equipment;
import com.baodian.util.page.EquPage;

public interface DeviceManager {
	
	public String get_device_list(EquPage page);
	
	public String get_ip_equ(String ip);
	
	public String get_batch_equ(String ip);
	
	public boolean ip_equ_del(String ip);
	
	public String equ_batch_up(List<String> ip_list ,Equipment equ);
	
	public String equ_batch_add(List<String> ip_list ,Equipment equ);
	
	public InputStream get_env_list_xls(String env);
	
}
