package com.baodian.model.news;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.User;

@Entity
@Table(name="newsreply")
public class Newsreply {
	private int id;
	private String content;
	private Date reptime;
	private User author;
	private Newsbase nbase;
	
	public Newsreply() {}
	public Newsreply(int id ,Date reptime, String content,
			int aId, String aName, Date aDate, String aHimage, int dId, String dName) {
		this.id = id;
		this.reptime = reptime;
		this.content = content;
		this.author = new User(aId, aName, aDate, aHimage, dId, dName);
	}
	public Newsreply(int id ,Date reptime, String content, int nbId,
			int aId, String aName, Date aDate, String aHimage, int dId, String dName) {
		this.id = id;
		this.reptime = reptime;
		this.content = content;
		this.nbase = new Newsbase(nbId);
		this.author = new User(aId, aName, aDate, aHimage, dId, dName);
	}
	public Newsreply(int id) {
		this.id = id;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(columnDefinition="text", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp", insertable=false, updatable=false)
	public Date getReptime() {
		return reptime;
	}
	public void setReptime(Date reptime) {
		this.reptime = reptime;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="newsbase_id", nullable=false)
	public Newsbase getNbase() {
		return nbase;
	}
	public void setNbase(Newsbase nbase) {
		this.nbase = nbase;
	}
	/**
	 * check content
	 * @return 空:-1, 超出21845:-2, 其他返回长度
	 */
	public int ckContent() {
		if(content == null) {
			return -1;
		}
		if(content.length() > 21845) {
			return -2;
		}
		return content.length();
	}
}
