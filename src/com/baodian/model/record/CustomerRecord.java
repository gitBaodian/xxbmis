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
@Table(name="customer_record")
public class CustomerRecord {
	
	private int id;
	private Date time;           	//记录时间
	private String ticket_id;		//itsm ticket id
	private String title;		 	//事件标题
	private String detail;			//时间内容
	private String type;		 	//事件类型:简单、一线处理、二线处理
	private String solve_approach;  //解决方法
	private User operator;			//操作员
	private User r_user;			//记录人
	private String state;			//事件进度
	
	public CustomerRecord(){}
	
	/**
	 * 
	 * @param id
	 * @param time
	 * @param ticket_id
	 * @param title
	 * @param detail
	 * @param type
	 * @param solve_approach
	 * @param operator_id
	 * @param operator_name
	 * @param r_user_id
	 * @param r_user_name
	 */
	public CustomerRecord(int id,Date time,String ticket_id,String title,String detail,String type,
						  String solve_approach,int operator_id,String operator_name,
						  int r_user_id,String r_user_name,String state){
		this.id = id;
		this.time = time;
		this.ticket_id = ticket_id;
		this.title = title;
		this.detail = detail;
		this.type = type;
		this.solve_approach = solve_approach;
		this.operator = new User();
		operator.setId(operator_id);
		operator.setName(operator_name);
		this.r_user = new User();
		r_user.setId(r_user_id);
		r_user.setName(r_user_name);
		this.state = state;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}

	@Column(length=30,nullable=true)
	public String getTicket_id() {
		return ticket_id;
	}
	public void setTicket_id(String ticket_id) {
		this.ticket_id = ticket_id;
	}

	@Column(length=50,nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@Column(length=300,nullable=false)
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Column(length=20,nullable=false)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	@Column(length=300,nullable=true)
	public String getSolve_approach() {
		return solve_approach;
	}
	public void setSolve_approach(String solve_approach) {
		this.solve_approach = solve_approach;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="op_user_id",nullable=false)
	public User getOperator() {
		return operator;
	}	
	public void setOperator(User operator) {
		this.operator = operator;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="r_user_id",nullable=false)
	public User getR_user() {
		return r_user;
	}
	public void setR_user(User r_user) {
		this.r_user = r_user;
	}
	
	@Column(length=8,nullable=false)
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String acquireOp_name(){
		return operator.getName();
	}
}
