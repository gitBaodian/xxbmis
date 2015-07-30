package com.baodian.service.device;

import com.baodian.model.device.Goods;

public interface GoodsManager {

//c
	/**
	 * 保存
	 * @return {status:0}
	 */
	public String save(Goods goods);
//r
	/**
	 * 排序获取所有的设备
	 * @return [{id:1,name:'',detail,'',pId:1,open:true}]
	 */
	public String findGoods();
//u
	/**
	 * 更改name和detail
	 * @return {status:0}
	 */
	public String changeND(Goods goods);
	/**
	 * 更新顺序和父节点
	 * @param gds 'id_pId_sort'
	 */
	public void changeSort(String[] gds);
//d
	public String remove(String gid);
}
