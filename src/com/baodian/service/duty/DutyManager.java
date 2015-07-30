package com.baodian.service.duty;

public interface DutyManager {

//c
//r
	/**
	 * 读取全部值班部门
	 * @return
	 */
	public String findAll();
//u
	/**
	 * 更新值班对应的部门，先删除全部，然后重新添加新的，中间出错忽略
	 * @param json 值间用A，部间用a区分，没有补零 例：1a2aA0A3a4A0A
	 * @return
	 */
	public void changeAll(String json);
//d

}
