package com.baodian.model.document;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.User;

/**
 * 文档
 * @author LF_eng
 */
@Entity
@Table(name="f_document")
public class Document {
	private int id;
	private String name;
	private Date date;
	private String url;
	private String content;
	private User owner;
	private DocDir dir;
	
	public Document() {}
	//DocumentDaoImpl.getDocs
	public Document(int id, String name, Date date, int dirId) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.dir = new DocDir(dirId);
	}
	//DocumentDaoImpl.getDoc
	public Document(int id, String name, Date date, int dirId, String dirName, Object dirOwner,
			String url, String content, int uId, String uName) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.dir = new DocDir(dirId, dirName, dirOwner);
		this.url = url;
		this.content = content;
		this.owner = new User(uId, uName);
	}

	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(length=100, nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@Column(length=255, nullable=false)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Basic(fetch=FetchType.LAZY)
	@Column(columnDefinition="text", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getOwner() {
		return owner;
	}
	public void setOwner(User owner) {
		this.owner = owner;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public DocDir getDir() {
		return dir;
	}
	public void setDir(DocDir dir) {
		this.dir = dir;
	}
	/**
	 * check name
	 * @return 空:-1, 超出100:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 100) {
			return -2;
		}
		return name.length();
	}
	/**
	 * check content
	 * @return 空:-1, 超出100M:-2, 其他返回长度
	 */
	public int ckContent() {
		if(content == null) {
			return -1;
		}
		if(content.length() > 100000000) {
			return -2;
		}
		return content.length();
	}
	/**
	 * check url
	 * @return 空:-1, 超出100:-2, 其他返回长度
	 */
	public int ckUrl() {
		if(url == null) {
			return -1;
		}
		if(url.length() > 100) {
			return -2;
		}
		return url.length();
	}
}
