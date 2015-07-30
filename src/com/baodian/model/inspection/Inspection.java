package com.baodian.model.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Inspection entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "inspection")
public class Inspection implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer inspId;
	private String inspCreatetime;
	private String userName;

	// Constructors

	/** default constructor */
	public Inspection() {
	}

	/** full constructor */
	public Inspection(Integer inspId, String inspCreatetime, String userName) {
		this.inspId = inspId;
		this.inspCreatetime = inspCreatetime;
		this.userName = userName;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "insp_id")
	public Integer getInspId() {
		return this.inspId;
	}

	public void setInspId(Integer inspId) {
		this.inspId = inspId;
	}

	@Column(name = "insp_createtime", length = 50)
	public String getInspCreatetime() {
		return this.inspCreatetime;
	}

	public void setInspCreatetime(String inspCreatetime) {
		this.inspCreatetime = inspCreatetime;
	}

	@Column(name = "user_name", length = 10)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}