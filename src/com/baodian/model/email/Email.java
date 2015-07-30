package com.baodian.model.email;

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

@Entity
@Table(name="email")
public class Email {
	private int id;
	private String title;
	private String content;
	private Date date;
	private User addressor;//发件人
	private int sendst;//发送状态-已发
	private int emailst;//邮件状态-删除
	//private List<User_Email> u_ms;
	
	public Email() {}
	public Email(int id) {
		this.id = id;
	}
	//EmailDaoImpl.getEms
	public Email(int id, String title, Date date, int sendst, int emailst) {
		this.id = id;
		this.title = title;
		this.date = date;
		this.sendst = sendst;
		this.emailst = emailst;
	}
	//User_Email.init
	public Email(int eId, String title, int aId, String aName, String dname) {
		this.id = eId;
		this.title = title;
		this.addressor = new User(aId, aName, 0, dname);
	}
	//EmailDaoImpl.getEm
	public Email(int id, String title, String content, Date date,
			int sendst, int emailst, int aId, String aName, int did, String dName) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.date = date;
		this.sendst = sendst;
		this.emailst = emailst;
		this.addressor = new User(aId, aName, did, dName);
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Basic(fetch=FetchType.LAZY)
	@Column(columnDefinition="text", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(columnDefinition="timestamp not null default current_timestamp")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(nullable=false)
	public User getAddressor() {
		return addressor;
	}
	public void setAddressor(User addressor) {
		this.addressor = addressor;
	}
	
	/**
	 * 1已经发送, 2已回复
	 * @return
	 */
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getSendst() {
		return sendst;
	}
	public void setSendst(int sendst) {
		this.sendst = sendst;
	}
	/**
	 * 1正常, 2已删除(可找回), 3彻底删除(不再显示，但数据库仍然存在，直到所有收件人和发件人都删除掉)
	 * 4草稿箱
	 */
	@Column(columnDefinition="tinyint(1) not null default'1'", insertable=false)
	public int getEmailst() {
		return emailst;
	}
	public void setEmailst(int emailst) {
		this.emailst = emailst;
	}
	/*@OneToMany(mappedBy="email")
	public List<User_Email> getU_ms() {
		return u_ms;
	}
	public void setU_ms(List<User_Email> u_ms) {
		this.u_ms = u_ms;
	}*/
}
