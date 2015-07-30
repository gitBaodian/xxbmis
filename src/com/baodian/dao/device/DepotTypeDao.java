package com.baodian.dao.device;

import java.util.List;

import com.baodian.model.device.DepotType;

public interface DepotTypeDao {
//c
	public void add(DepotType dt);
//r
	/**
	 * 按照先后顺序读取设备用途
	 */
	public List<DepotType> getDts();
	/**
	 * 排序获取设备用途
	 * @return [{id:1,name:""}]
	 */
	public String getDtsOnStr();
//u
	public void update(DepotType dt);
//d
	public void delete(String id);
}
