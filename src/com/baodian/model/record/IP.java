package com.baodian.model.record;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ip")
public class IP {
	

	private int id;
	private String ip_head;      //机器IP段
	
	public IP(){}
	
	public IP(String ip_head){
		this.ip_head = ip_head;
	}
	
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Column(length=16,nullable=false)
	public String getIp_head() {
		return ip_head;
	}

	public void setIp_head(String ip_head) {
		this.ip_head = ip_head;
	}
}
