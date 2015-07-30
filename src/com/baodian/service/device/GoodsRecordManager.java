package com.baodian.service.device;

import com.baodian.model.device.GoodsRecord;
import com.baodian.util.page.GrPage;

public interface GoodsRecordManager {

//c
	public String save(GoodsRecord gr);
//r
	/**
	 * 分页获取设备记录
	 * @param page
	 * @return
	 */
	public String findGsByPage(GrPage page);
	/**
	 * 返回设备类型,设备用途,使用类别
	 * @return {gds:[{id,name,pId}],dts:[{id,name}],uts:[{id,name,sort,gd,dtin,dtout}]}
	 */
	public String findGoods();
	/**
	 * 统计设备
	 * @return {gds:[{id,name,pId}],dts:[{id,name}],records:[{id,detail:[{id,num}]}]}
	 */
	public String findGRS();
//u
	public String change(GoodsRecord gr);
//d
	public String remove(String id);

}
