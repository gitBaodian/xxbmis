package com.baodian.model.news;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="newsclass")
public class Newsclass {
	private int id;
	private String name;
	private int display;//是否显示
	private int position;//显示顺序
	private int review;//是否需要审核
	private int num;//此新闻类型的新闻条数
	private List<Newsbase> newsbases;
	
	public Newsclass() {}
	public Newsclass(int id) {
		this.id = id;
	}
	public Newsclass(int id, String name) {
		this.id = id;
		this.name = name;
	}
	public Newsclass(int ncId, String ncName, int position) {
		this.id = ncId;
		this.name = ncName;
		this.position = position;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=20, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getDisplay() {
		return display;
	}
	public void setDisplay(int display) {
		this.display = display;
	}
	@Column(columnDefinition="tinyint(1) not null")
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	@Column(columnDefinition="tinyint(1) not null default'0'")
	public int getReview() {
		return review;
	}
	public void setReview(int review) {
		this.review = review;
	}
	@Column(columnDefinition="int not null default'0'", insertable=false)
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@OneToMany(mappedBy="nclass")
	public List<Newsbase> getNewsbases() {
		return newsbases;
	}
	public void setNewsbases(List<Newsbase> newsbases) {
		this.newsbases = newsbases;
	}
}
