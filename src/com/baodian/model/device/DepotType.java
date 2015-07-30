package com.baodian.model.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 设备用途
 * @author LF_eng
 */
@Entity
@Table(name="f_g_depot")
public class DepotType {
	private int id;
	private String name;
	private int sort;
	
	public DepotType() {}
	//GoodsRecord.init
	public DepotType(int id) {
		this.id = id;
	}
	//GoodsRecordDaoImpl.getDts
	public DepotType(int id, String name) {
		this.id = id;
		this.name = name;
	}
	@Id
	@GeneratedValue
	@Column(columnDefinition="tinyint(1)")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(columnDefinition="char(10)", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	/**
	 * check name
	 * @return 空:-1, 超出10:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 10) {
			return -2;
		}
		return name.length();
	}
}
