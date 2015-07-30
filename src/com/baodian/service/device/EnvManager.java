package com.baodian.service.device;

import com.baodian.model.device.Env;

public interface EnvManager {
	
	public String env_list();
	
	public String env_add(Env env);
		
	public String env_update(Env env);
		
	public String env_del(Env env);
	
	public String get_env_list();
}
