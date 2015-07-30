package com.baodian.service.device.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baodian.dao.device.EnvDao;
import com.baodian.dao.device.EquipmentDao;
import com.baodian.model.device.Env;
import com.baodian.service.device.EnvManager;

@Service("envManager")
public class EnvManagerImpl implements EnvManager{

	private EnvDao envDao;
	private EquipmentDao equipmentDao;
	
	/**
	 * 环境列表
	 */
	@SuppressWarnings("unchecked")
	public String env_list(){
		String json = "";
		json = "{\"total\":" + "0" + "," +
				"\"rows\":[";
		List<?> env_lists = (List<?>) envDao.findhql("select COUNT(*) from Env e");
		if(((Long) env_lists.get(0)).intValue()==0){
			json = "{\"total\":0,\"rows\":[]}";
			return json;
		}
		List<Env> env_list = (List<Env>) envDao.findhql("from Env e order by e.id asc");
		for(Env env : env_list){
			json = json.concat("{\"id\":\"" + env.getId() + "\"," +
					"\"env\":\"" + env.getEnv() + "\"," +
					"\"env_name\":\"" + env.getEnvName() + "\"," +
					"\"dsh\":\"待+\"},");
		}
		if(env_list.size() != 0)
			json = json.substring(0, json.length()-1).concat("]}");
		else
			json = json.concat("]}");
		return json;
	}
	
	/**
	 * env添加
	 * @param env
	 * @return
	 */
	public String env_add(Env env){
		String json = "";
		try {
			envDao.save(env);
			json = "{\"status\":0,\"mess\":\"添加成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"添加失败！\"}";
		}
		return json;
	}
	
	/**
	 * env修改成功
	 * @param env
	 * @return
	 */
	public String env_update(Env env){
		String json = "";
		try {
			envDao.attachDirty(env);
			json = "{\"status\":0,\"mess\":\"修改成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"修改失败！\"}";
		}
		return json;
	}

	/**
	 * env删除
	 * @param env
	 * @return
	 */
	public String env_del(Env env){
		String json = "";
		Env ee = (Env) envDao.findById(env.getId());
		try {
			envDao.delete(ee);
			equipmentDao.delhql("from Equipment e where e.env = '"+ee.getEnv()+"'");
			json = "{\"status\":0,\"mess\":\"删除成功！\"}";
		} catch (Exception e) {
			json = "{\"status\":1,\"mess\":\"删除失败！\"}";
			e.printStackTrace();
		}
		return json;
	}


	/**
	 * env option
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String get_env_list() {
		String json = "[";
		List<Env> env_list = (List<Env>) envDao.findhql("from Env e order by e.id asc");
		for(Env env : env_list){
			json = json.concat("{\"env\":\""+env.getEnv()+"\",\"env_name\":\""+env.getEnvName()+"\"},");
		}
		if(!env_list.isEmpty()){
			json = json.substring(0, json.length()-1);//去掉逗号
		}
		json = json.concat("]");
		return json;
	}
	
	@Resource(name = "envDao")
	public void setEnvDao(EnvDao envDao) {
		this.envDao = envDao;
	}
	@Resource(name = "equipmentDao")
	public void setEquipmentDao(EquipmentDao equipmentDao) {
		this.equipmentDao = equipmentDao;
	}
	
}
