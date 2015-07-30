package com.baodian.model.handover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Shift entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "shift")
public class Shift implements java.io.Serializable {

	// Fields

	private Integer id;
	private String successionTime;
	private String shiftTime;
	private String work;
	private String class_;
	private String crew;
	private String remarkAccept;
	private String remarkShift;
	private String remark;
	private String wdate;

	// Constructors

	/** default constructor */
	public Shift() {
	}

	/** full constructor */
	public Shift(String successionTime, String shiftTime, String work,
			String class_, String crew, String remarkAccept,
			String remarkShift, String remark, String wdate) {
		this.successionTime = successionTime;
		this.shiftTime = shiftTime;
		this.work = work;
		this.class_ = class_;
		this.crew = crew;
		this.remarkAccept = remarkAccept;
		this.remarkShift = remarkShift;
		this.remark = remark;
		this.wdate = wdate;
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

	@Column(name = "succession_time", length = 50)
	public String getSuccessionTime() {
		return this.successionTime;
	}

	public void setSuccessionTime(String successionTime) {
		this.successionTime = successionTime;
	}

	@Column(name = "shift_time", length = 50)
	public String getShiftTime() {
		return this.shiftTime;
	}

	public void setShiftTime(String shiftTime) {
		this.shiftTime = shiftTime;
	}

	@Column(name = "work", length = 10)
	public String getWork() {
		return this.work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	@Column(name = "class", length = 10)
	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	@Column(name = "crew", length = 50)
	public String getCrew() {
		return this.crew;
	}

	public void setCrew(String crew) {
		this.crew = crew;
	}

	@Column(name = "remark_accept")
	public String getRemarkAccept() {
		return this.remarkAccept;
	}

	public void setRemarkAccept(String remarkAccept) {
		this.remarkAccept = remarkAccept;
	}

	@Column(name = "remark_shift")
	public String getRemarkShift() {
		return this.remarkShift;
	}

	public void setRemarkShift(String remarkShift) {
		this.remarkShift = remarkShift;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "wdate", length = 50)
	public String getWdate() {
		return this.wdate;
	}

	public void setWdate(String wdate) {
		this.wdate = wdate;
	}

}