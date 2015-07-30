package com.baodian.model.document;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.User;

/**
 * 文档目录
 * @author LF_eng
 */
@Entity
@Table(name="f_docdir")
public class DocDir {
	private int id;
	private String name;
	private int sort;
	private User owner;
	private DocDir parent;
	
	public DocDir() {}
	public DocDir(int id) {
		this.id = id;
	}
	public DocDir(int id, String name, Object owner) {
		this.id = id;
		this.name = name;
		if(owner != null) {
			this.owner = new User((Integer) owner);
		}
	}
	//DocDirDaoImpl.getDirs
	public DocDir(int id, String name, int sort, Object pid) {
		this.id = id;
		this.name = name;
		this.sort = sort;
		if(pid != null) {
			this.parent = new DocDir((Integer) pid);
		}
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=30, nullable=false)
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
	 * 文档目录的所有者，为null表示为公共目录
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	public DocDir getParent() {
		return parent;
	}
	public void setParent(DocDir parent) {
		this.parent = parent;
	}
	/**
	 * check name
	 * @return 空:-1, 超出30:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 30) {
			return -2;
		}
		return name.length();
	}
}
