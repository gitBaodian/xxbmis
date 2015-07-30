package com.baodian.model.handover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Transfer entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "transfer")
public class Transfer implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private Integer id;
	private String applicant;
	private String applicantTime;
	private String object;
	private String applicationTime;
	private String whether;

	// Constructors

	/** default constructor */
	public Transfer() {
	}

	/** full constructor */
	public Transfer(String applicant, String applicantTime, String object,
			String applicationTime, String whether) {
		this.applicant = applicant;
		this.applicantTime = applicantTime;
		this.object = object;
		this.applicationTime = applicationTime;
		this.whether = whether;
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

	@Column(name = "applicant", length = 10)
	public String getApplicant() {
		return this.applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	@Column(name = "applicant_time", length = 10)
	public String getApplicantTime() {
		return this.applicantTime;
	}

	public void setApplicantTime(String applicantTime) {
		this.applicantTime = applicantTime;
	}

	@Column(name = "object", length = 10)
	public String getObject() {
		return this.object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Column(name = "application_time", length = 50)
	public String getApplicationTime() {
		return this.applicationTime;
	}

	public void setApplicationTime(String applicationTime) {
		this.applicationTime = applicationTime;
	}

	@Column(name = "whether", length = 2)
	public String getWhether() {
		return this.whether;
	}

	public void setWhether(String whether) {
		this.whether = whether;
	}

}