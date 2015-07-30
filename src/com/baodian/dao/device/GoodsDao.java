package com.baodian.dao.device;

import java.util.List;

import com.baodian.model.device.Goods;

public interface GoodsDao {
//c
	public void save(Goods g);
//r
	/**
	 * 排序获取全部设备
	 */
	public List<Goods> getGoods();
	/**
	 * 排序获取全部设备类型
	 * @return [{id:1,name:"",pId:2}]
	 */
	public String getGds();
//u
	/**
	 * 更新name和detail
	 */
	public void updateND(Goods goods);
	/**
	 * 更新排序和父节点
	 * @param id
	 * @param pid -1表示不更新 0表示移到顶层
	 * @param sort -1表示不更新
	 */
	public void updateSort(int id, int pid, int sort);
//d
	public void delete(int gid);
}
