package com.baodian.model.notepad;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.baodian.model.user.Department;
import com.baodian.model.user.User;

/**
 * 每日记事
 * @author LF_eng
 */
@Entity
@Table(name="f_notepad")
public class Notepad {
	private int id;
	private String name;
	private Date date;
	private int top;
	private User author;
	private Department depm;
	
	public Notepad() {}
	public Notepad(String name) {
		this.name = name;
	}
	//NotepadDaoImpl.getNpsByPage
	public Notepad(int id, String name, Date date, int top,
			int uid, String uname, int did, String dname) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.top = top;
		this.author = new User(uid, uname);
		this.depm = new Department(did, dname);
	}
	public Notepad(int id, String name, Date date, int top) {
		this.id = id;
		this.name = name;
		this.date = date;
		this.top = top;
	}
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Column(columnDefinition="tinyint(2) not null default'2'")
	public int getTop() {
		return top;
	}
	/**
	 * 置顶标志
	 * @param top 1置顶 2无
	 */
	public void setTop(int top) {
		this.top = top;
	}
	@Column(columnDefinition="varchar(21785) not null")
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
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="author_id", nullable=false)
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="depm_id", nullable=false)
	public Department getDepm() {
		return depm;
	}
	public void setDepm(Department depm) {
		this.depm = depm;
	}
	/**
	 * check name
	 * @return 空:-1, 超出21785:-2, 其他返回长度
	 */
	public int ckName() {
		if(name == null) {
			return -1;
		}
		if(name.length() > 21785) {
			return -2;
		}
		return name.length();
	}
}