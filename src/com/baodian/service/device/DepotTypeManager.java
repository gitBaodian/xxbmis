package com.baodian.service.device;

import com.baodian.model.device.DepotType;

public interface DepotTypeManager {
//c
	public String save(DepotType dt);
//r
	/**
	 * 排序获取全部设备用途
	 * @return [id:1,name:"",sort:1]
	 */
	public String findDts();
//u
	public String change(DepotType dt);
//d
	public String remove(String id);
}
