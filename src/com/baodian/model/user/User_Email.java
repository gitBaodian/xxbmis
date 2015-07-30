package com.baodian.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.email.Email;

@Entity
@IdClass(User_EmailPK.class)
@Table(name="user_email")
public class User_Email {
	private User user;//收件人
	private Email email;
	private Date date;//收件时间
	private int readst;//读取状态-未读
	private int recest;//收件状态-抄送
	private int emailst;//邮件状态-删除
	
	public User_Email() {}
	//EmailManagerImpl.saveEmail
	public User_Email(int uid, int eid, Date date, int recest) {
		this.user = new User(uid);
		this.email = new Email(eid);
		this.date = date;
		this.recest = recest;
	}
	//User_EmailDaoImpl.getE_UsByEId
	public User_Email(Date date, int readst, int recest, int emailst,
			int id, String name, int did, String dname) {
		this.date = date;
		this.readst = readst;
		this.recest = recest;
		this.emailst = emailst;
		this.user = new User(id, name, did, dname);
	}
	//User_EmailDaoImpl.getE_Us(page/id,date)
	public User_Email(int eId, Date date, int readst, int recest, int emailst,
			String title, int aId, String aName, String dname) {
		this.date = date;
		this.readst = readst;
		this.recest = recest;
		this.emailst = emailst;
		this.email = new Email(eId, title, aId, aName, dname);
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Id
	@ManyToOne(fetch=FetchType.LAZY)
	public Email getEmail() {
		return email;
	}
	public void setEmail(Email email) {
		this.email = email;
	}
	/**
	 * 1未读, 2已读, 3已回复
	 */
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getReadst() {
		return readst;
	}
	public void setReadst(int readst) {
		this.readst = readst;
	}
	/**
	 * 1收件, 2抄送, 3转发
	 */
	@Column(columnDefinition="tinyint(1) not null default'1'")
	public int getRecest() {
		return recest;
	}
	public void setRecest(int recest) {
		this.recest = recest;
	}
	/**
	 * 1正常, 2已删除(可找回), 3彻底删除(不再显示，但数据库仍然存在，直到所有收件人和发件人都删除掉)
	 */
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getEmailst() {
		return emailst;
	}
	public void setEmailst(int emailst) {
		this.emailst = emailst;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
