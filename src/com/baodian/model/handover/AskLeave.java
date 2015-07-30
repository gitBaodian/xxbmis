package com.baodian.model.handover;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AskLeave entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ask_leave")
public class AskLeave implements java.io.Serializable {

	// Fields

	private Integer id;
	private String applicant;
	private String startDate;
	private String endDate;
	private String applicationTime;
	private String whether;

	// Constructors

	/** default constructor */
	public AskLeave() {
	}

	/** full constructor */
	public AskLeave(String applicant, String startDate, String endDate,
			String applicationTime, String whether) {
		this.applicant = applicant;
		this.startDate = startDate;
		this.endDate = endDate;
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

	@Column(name = "start_date", length = 50)
	public String getStartDate() {
		return this.startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date", length = 50)
	public String getEndDate() {
		return this.endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
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