package com.baodian.service.device;

import com.baodian.model.device.UseType;

public interface UseTypeManager {
//c
	public String save(UseType ut);
//r
	/**
	 * 排序获取全部使用类型
	 * @return [id:1,name:"",sort:1,gd:1,dtin:1,dtout:0]
	 */
	public String findUts();
	/**
	 * 返回设备类型和设备用途
	 * @return {gds:[{id,name,pId}],dts:[{id,name}}
	 */
	public String findGoods();
//u
	public String change(UseType ut);
//d
	public String remove(String id);
}
