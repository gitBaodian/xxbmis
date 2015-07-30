package com.baodian.dao.device;

import com.baodian.model.device.UseType;

public interface UseTypeDao {
//c
	public void add(UseType ut);
//r
	/**
	 * 按照先后顺序读取使用类型
	 * @return [id:1,name:"",sort:1,gd:1,dtin:1,dtout:0]
	 */
	public String getUts();
//u
	public void update(UseType ut);
//d
	public void delete(String id);
}
