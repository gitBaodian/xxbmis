package com.baodian.model.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 使用种类
 * @author LF_eng
 */
@Entity
@Table(name="f_g_use")
public class UseType {
	private int id;
	private String name;
	private int sort;
	private Goods gd;
	private DepotType dtin;
	private DepotType dtout;
	
	@Id
	@GeneratedValue
	@Column(columnDefinition="tinyint(1)")
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
	@Column(columnDefinition="tinyint(1) not null")
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public Goods getGd() {
		return gd;
	}
	public void setGd(Goods gd) {
		this.gd = gd;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dtin")
	public DepotType getDtin() {
		return dtin;
	}
	public void setDtin(DepotType dtin) {
		this.dtin = dtin;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="dtout")
	public DepotType getDtout() {
		return dtout;
	}
	public void setDtout(DepotType dtout) {
		this.dtout = dtout;
	}
	/**
	 * check name
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
}
