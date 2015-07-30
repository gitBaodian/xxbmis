package com.baodian.model.record;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.baodian.model.user.Department;
import com.baodian.model.user.User;

@Entity
@Table(name="working_record")
public class Working_Record {
	
	
	private int id;
	private String detail;  //记录内容
	private Date time;   //记录时间
	private String type;   //操作类型：普通操作、升级操作
	private String file_name;      //附件名
	private User user;      //记录人
	
	//private String userName;
	//private String deptName;
	
	public Working_Record(){}
	
	public Working_Record(int id,String detail,Date time,String type,
			int user_id,String user_name,String user_account,String dept_name,String file_name) {
		this.id = id;
		this.detail = detail;
		this.time = time;
		this.type = type;
		User user = new User();
		user.setId(user_id);
		user.setName(user_name);
		user.setAccount(user_account);
		Department dept = new Department();
		dept.setName(dept_name);
		user.setDpm(dept);
		this.setFile_name(file_name);
		this.user = user;
	}
	
	public Working_Record(int id,String detail,Date time,String type,
			int user_id,String user_name,String user_account) {
		this.id = id;
		this.detail = detail;
		this.time = time;
		this.type = type;
		User user = new User();
		user.setId(user_id);
		user.setName(user_name);
		user.setAccount(user_account);
		this.user = user;
	}
	
	/*	String HQL = "select new Working_Record(wr.id,wr.detail,wr.time,wr.type,wr.user) from Working_Record wr";
 	public Working_Record(int id,String detail,Date time,String type,User user) {
		this.id = id;
		this.detail = detail;
		this.time = time;
		this.type = type;
		User wr_user = new User();
		wr_user = user; 
		this.user = wr_user;
	}
*/	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(length=300,nullable=false)
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	@Column(length=10,nullable=false)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String acquireUserName(){
		return this.getUser().getName();
	}
	
	public String acquireDeptName(){
		return this.getUser().getDpm().getName();
	}

}
