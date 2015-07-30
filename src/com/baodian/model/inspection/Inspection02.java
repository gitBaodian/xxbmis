package com.baodian.model.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Inspection02 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "inspection_02")
public class Inspection02 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String inspTime02;
	private String inspTp02;
	private String inspHum02;
	private String inspHj02;
	private String inspQj02;
	private String inspYx02;
	private String inspYw02;
	private String inspPw02;
	private String inspBat02;
	private String inspAir02;
	private String inspFire02;
	private String inspOttest02;
	private String inspOtfa02;
	private String inspOtmo302;
	private String inspTelwdm02;
	private String inspTelups02;
	private String inspUniwdm02;
	private String inspUniups02;
	private String inspRemark02;
	private Integer inspId;

	// Constructors

	/** default constructor */
	public Inspection02() {
	}

	/** full constructor */
	public Inspection02(String inspTime02, String inspTp02, String inspHum02,
			String inspHj02, String inspQj02, String inspYx02, String inspYw02,
			String inspPw02, String inspBat02, String inspAir02,
			String inspFire02, String inspOttest02, String inspOtfa02,
			String inspOtmo302, String inspTelwdm02, String inspTelups02,
			String inspUniwdm02, String inspUniups02, String inspRemark02,
			Integer inspId) {
		this.inspTime02 = inspTime02;
		this.inspTp02 = inspTp02;
		this.inspHum02 = inspHum02;
		this.inspHj02 = inspHj02;
		this.inspQj02 = inspQj02;
		this.inspYx02 = inspYx02;
		this.inspYw02 = inspYw02;
		this.inspPw02 = inspPw02;
		this.inspBat02 = inspBat02;
		this.inspAir02 = inspAir02;
		this.inspFire02 = inspFire02;
		this.inspOttest02 = inspOttest02;
		this.inspOtfa02 = inspOtfa02;
		this.inspOtmo302 = inspOtmo302;
		this.inspTelwdm02 = inspTelwdm02;
		this.inspTelups02 = inspTelups02;
		this.inspUniwdm02 = inspUniwdm02;
		this.inspUniups02 = inspUniups02;
		this.inspRemark02 = inspRemark02;
		this.inspId = inspId;
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

	@Column(name = "insp_time_02", length = 50)
	public String getInspTime02() {
		return this.inspTime02;
	}

	public void setInspTime02(String inspTime02) {
		this.inspTime02 = inspTime02;
	}

	@Column(name = "insp_tp_02", length = 5)
	public String getInspTp02() {
		return this.inspTp02;
	}

	public void setInspTp02(String inspTp02) {
		this.inspTp02 = inspTp02;
	}

	@Column(name = "insp_hum_02", length = 5)
	public String getInspHum02() {
		return this.inspHum02;
	}

	public void setInspHum02(String inspHum02) {
		this.inspHum02 = inspHum02;
	}

	@Column(name = "insp_hj_02", length = 5)
	public String getInspHj02() {
		return this.inspHj02;
	}

	public void setInspHj02(String inspHj02) {
		this.inspHj02 = inspHj02;
	}

	@Column(name = "insp_qj_02", length = 5)
	public String getInspQj02() {
		return this.inspQj02;
	}

	public void setInspQj02(String inspQj02) {
		this.inspQj02 = inspQj02;
	}

	@Column(name = "insp_yx_02", length = 5)
	public String getInspYx02() {
		return this.inspYx02;
	}

	public void setInspYx02(String inspYx02) {
		this.inspYx02 = inspYx02;
	}

	@Column(name = "insp_yw_02", length = 5)
	public String getInspYw02() {
		return this.inspYw02;
	}

	public void setInspYw02(String inspYw02) {
		this.inspYw02 = inspYw02;
	}

	@Column(name = "insp_pw_02", length = 5)
	public String getInspPw02() {
		return this.inspPw02;
	}

	public void setInspPw02(String inspPw02) {
		this.inspPw02 = inspPw02;
	}

	@Column(name = "insp_bat_02", length = 5)
	public String getInspBat02() {
		return this.inspBat02;
	}

	public void setInspBat02(String inspBat02) {
		this.inspBat02 = inspBat02;
	}

	@Column(name = "insp_air_02", length = 5)
	public String getInspAir02() {
		return this.inspAir02;
	}

	public void setInspAir02(String inspAir02) {
		this.inspAir02 = inspAir02;
	}

	@Column(name = "insp_fire_02", length = 5)
	public String getInspFire02() {
		return this.inspFire02;
	}

	public void setInspFire02(String inspFire02) {
		this.inspFire02 = inspFire02;
	}

	@Column(name = "insp_ottest_02", length = 5)
	public String getInspOttest02() {
		return this.inspOttest02;
	}

	public void setInspOttest02(String inspOttest02) {
		this.inspOttest02 = inspOttest02;
	}

	@Column(name = "insp_otfa_02", length = 5)
	public String getInspOtfa02() {
		return this.inspOtfa02;
	}

	public void setInspOtfa02(String inspOtfa02) {
		this.inspOtfa02 = inspOtfa02;
	}

	@Column(name = "insp_otmo3_02", length = 5)
	public String getInspOtmo302() {
		return this.inspOtmo302;
	}

	public void setInspOtmo302(String inspOtmo302) {
		this.inspOtmo302 = inspOtmo302;
	}

	@Column(name = "insp_telwdm_02", length = 5)
	public String getInspTelwdm02() {
		return this.inspTelwdm02;
	}

	public void setInspTelwdm02(String inspTelwdm02) {
		this.inspTelwdm02 = inspTelwdm02;
	}

	@Column(name = "insp_telups_02", length = 5)
	public String getInspTelups02() {
		return this.inspTelups02;
	}

	public void setInspTelups02(String inspTelups02) {
		this.inspTelups02 = inspTelups02;
	}

	@Column(name = "insp_uniwdm_02", length = 5)
	public String getInspUniwdm02() {
		return this.inspUniwdm02;
	}

	public void setInspUniwdm02(String inspUniwdm02) {
		this.inspUniwdm02 = inspUniwdm02;
	}

	@Column(name = "insp_uniups_02", length = 5)
	public String getInspUniups02() {
		return this.inspUniups02;
	}

	public void setInspUniups02(String inspUniups02) {
		this.inspUniups02 = inspUniups02;
	}

	@Column(name = "insp_remark_02")
	public String getInspRemark02() {
		return this.inspRemark02;
	}

	public void setInspRemark02(String inspRemark02) {
		this.inspRemark02 = inspRemark02;
	}

	@Column(name = "insp_id")
	public Integer getInspId() {
		return this.inspId;
	}

	public void setInspId(Integer inspId) {
		this.inspId = inspId;
	}

}