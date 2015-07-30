package com.baodian.action.device;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.baodian.model.device.Env;
import com.baodian.service.device.EnvManager;
import com.baodian.util.page.EquPage;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
@Component("env")
@Scope("prototype")
public class EnvAction extends ActionSupport{

	private String json;
	private EquPage page;
	private EnvManager envManager;
	
	private String id;
	private String env;
	private String env_name;
	
	public String manager(){
		return SUCCESS;
	}
	
	//环境列表
	public String list(){
		json = envManager.env_list();
		return "json";
	}
	
	//env添加
	public String env_add(){
		if(env==null||env.length()==0) env="";
		if(env_name==null||env_name.length()==0) env_name="";
		Env e = new Env();
		e.setEnv(env);
		e.setEnvName(env_name);
		json = envManager.env_add(e);
		return "json";
	}
	
	//env修改
	public String env_update(){
		if(env==null||env.length()==0) env="";
		if(env_name==null||env_name.length()==0) env_name="";
		Env e = new Env();
		e.setId(Integer.valueOf(id));
		e.setEnv(env);
		e.setEnvName(env_name);
		json = envManager.env_update(e);
		return "json";
	}
	
	//env删除
	public String env_del(){
		Env e = new Env();
		e.setId(Integer.valueOf(id));
		json = envManager.env_del(e);
		return "json";
	}
	
	
	//env option
	public String get_env_list(){
		json = envManager.get_env_list();
		return "json";
	}
	
	
	@Resource(name = "envManager")
	public void setEnvManager(EnvManager envManager) {
		this.envManager = envManager;
	}

	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public EquPage getPage() {
		return page;
	}
	public void setPage(EquPage page) {
		this.page = page;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEnv() {
		return env;
	}
	public void setEnv(String env) {
		this.env = env;
	}
	public String getEnv_name() {
		return env_name;
	}
	public void setEnv_name(String env_name) {
		this.env_name = env_name;
	}
}
