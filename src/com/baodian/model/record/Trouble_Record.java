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

import com.baodian.model.user.User;

@Entity
@Table(name="trouble_record")
public class Trouble_Record {
	
	private int id;
	private String IP;      //机器IP
	private String detail;  //故障内容
	private String state;   //故障状态
	private Date f_time;   //发现时间
	private User f_user;   //发现人
	private String solve_approach;  //解决方案
	private Date s_time;   //解决时间
	private User s_user;   //解决人
	private Date r_time;    //记录时间
	private User r_user;      //记录人
	private String filename;  //附件名
	
	public Trouble_Record(){}
	
	public Trouble_Record(int id,String IP,String detail,String state,Date f_time,int f_user_id,
			              String f_user_name,String solve_approach,Date s_time,Object s_user_id,
			              Date r_time,int r_user_id,String r_user_name,String filename) {
		this.id = id;
		this.IP = IP;
		this.detail = detail;
		this.state = state;
		this.f_time = f_time;
		User f_user = new User();
		f_user.setId(f_user_id);
		f_user.setName(f_user_name);
		this.f_user = f_user;
		this.solve_approach = solve_approach;
		this.s_time = s_time;
		User s_user = new User();
		if(s_user_id != null){
			s_user.setId((Integer)s_user_id);
		}
		this.s_user = s_user;
		this.r_time = r_time;
		User r_user = new User();
		r_user.setId(r_user_id);
		r_user.setName(r_user_name);
		this.filename = filename;
		this.r_user = r_user;
	}
	
/*	public Trouble_Record(int id,String IP,String detail,String state,Date f_time,User f_user,
			String solve_approach,Date s_time,User s_user,
            Date r_time,User r_user) {
			this.id = id;
			this.IP = IP;
			this.detail = detail;
			this.state = state;
			this.f_time = f_time;
			User f_u = new User();
			f_u.setId(f_user.getId());
			f_u.setName(f_user.getName());
			this.f_user = f_u;
			this.solve_approach = solve_approach;
			this.s_time = s_time;
			User s_u = new User();
			s_u.setId(s_user.getId());
			s_u.setName(s_user.getName());
			this.s_user = s_u;
			this.r_time = r_time;
			User r_u = new User();
			r_u.setId(r_user.getId());
			r_u.setName(r_user.getName());
			this.r_user = r_u;
	}*/
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(length=16,nullable=false)
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
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
	public Date getF_time() {
		return f_time;
	}
	public void setF_time(Date f_time) {
		this.f_time = f_time;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="f_user_id",nullable=false)
	public User getF_user() {
		return f_user;
	}
	public void setF_user(User f_user) {
		this.f_user = f_user;
	}
	
	@Column(length=300,nullable=true)
	public String getSolve_approach() {
		return solve_approach;
	}
	public void setSolve_approach(String solve_approach) {
		this.solve_approach = solve_approach;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=true)
	public Date getS_time() {
		return s_time;
	}
	public void setS_time(Date s_time) {
		this.s_time = s_time;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="s_user_id",nullable=true)
	public User getS_user() {
		return s_user;
	}
	public void setS_user(User s_user) {
		this.s_user = s_user;
	}
	
	@Column(length=5,nullable=false)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getR_time() {
		return r_time;
	}
	public void setR_time(Date rTime) {
		r_time = rTime;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="r_user_id",nullable=false)
	public User getR_user() {
		return r_user;
	}
	public void setR_user(User r_user) {
		this.r_user = r_user;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	

}
