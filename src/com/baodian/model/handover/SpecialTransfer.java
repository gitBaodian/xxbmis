package com.baodian.model.handover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpecialTransfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "special_transfer")
public class SpecialTransfer implements java.io.Serializable {

	// Fields

	private String specialDate;
	private String one;
	private String two;
	private String there;

	// Constructors

	/** default constructor */
	public SpecialTransfer() {
	}

	/** full constructor */
	public SpecialTransfer(String specialDate, String one, String two,
			String there) {
		this.specialDate = specialDate;
		this.one = one;
		this.two = two;
		this.there = there;
	}

	// Property accessors
	@Id
	@Column(name = "special_date", unique = true, nullable = false, length = 50)
	public String getSpecialDate() {
		return this.specialDate;
	}

	public void setSpecialDate(String specialDate) {
		this.specialDate = specialDate;
	}

	@Column(name = "one", nullable = false, length = 10)
	public String getOne() {
		return this.one;
	}

	public void setOne(String one) {
		this.one = one;
	}

	@Column(name = "two", nullable = false, length = 10)
	public String getTwo() {
		return this.two;
	}

	public void setTwo(String two) {
		this.two = two;
	}

	@Column(name = "there", nullable = false, length = 10)
	public String getThere() {
		return this.there;
	}

	public void setThere(String there) {
		this.there = there;
	}

}