package com.baodian.dao.device;

import java.util.List;

import com.baodian.model.device.GoodsRecord;
import com.baodian.util.page.GrPage;

public interface GoodsRecordDao {

//c
	public void add(GoodsRecord gr);
//r
	/**
	 * 分页获取设备记录
	 */
	public List<GoodsRecord> getGsByPage(GrPage page);
	/**
	 * 获取全部记录，用于统计数量
	 */
	public List<GoodsRecord> getGs();
//u
	public void update(GoodsRecord gr);
//d
	public void delete(String id);

}
