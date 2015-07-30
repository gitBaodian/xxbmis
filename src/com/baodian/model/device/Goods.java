package com.baodian.model.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="f_goods")
//仓库设备
public class Goods {
	private int id;
	private String name;//简要描述
	private String detail;//详细描述
	private int sort;
	private Goods parent;
	
	public Goods() {}
	//GoodsRecord.init
	public Goods(int id) {
		this.id = id;
	}
	//GoodsRecordDaoImpl.getGoods
	public Goods(int id, String name, Object parent) {
		this.id = id;
		this.name = name;
		if(parent != null) {
			this.parent = new Goods((Integer) parent);
		}
	}
	@Id
	@GeneratedValue
	@Column(columnDefinition="tinyint(1) unsigned")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(columnDefinition="char(20)", nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(length=100, nullable=false)
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="pid")
	public Goods getParent() {
		return parent;
	}
	public void setParent(Goods parent) {
		this.parent = parent;
	}
	
	/**
	 * check name简要信息
	 * @return 空:-1, 超出20:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 20) {
			return -2;
		}
		return name.length();
	}
	/**
	 * check detail详细信息
	 * @return 空:-1, 超出100:-2, 其他返回长度
	 */
	public int ckDetail() {
		if(detail == null) {
			return -1;
		}
		if(detail.length() > 100) {
			return -2;
		}
		return detail.length();
	}
}
