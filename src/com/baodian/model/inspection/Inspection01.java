package com.baodian.model.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Inspection01 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "inspection_01")
public class Inspection01 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String inspTime01;
	private String inspTp01;
	private String inspHum01;
	private String inspHj01;
	private String inspQj01;
	private String inspYx01;
	private String inspYw01;
	private String inspPw01;
	private String inspBat01;
	private String inspAir01;
	private String inspFire01;
	private String inspPs101;
	private String inspPs201;
	private String inspPs301;
	private String inspPs401;
	private String inspOttest01;
	private String inspMiu01;
	private String inspRemark01;
	private Integer inspId;

	// Constructors

	/** default constructor */
	public Inspection01() {
	}

	/** full constructor */
	public Inspection01(String inspTime01, String inspTp01, String inspHum01,
			String inspHj01, String inspQj01, String inspYx01, String inspYw01,
			String inspPw01, String inspBat01, String inspAir01,
			String inspFire01, String inspPs101, String inspPs201,
			String inspPs301, String inspPs401, String inspOttest01,
			String inspMiu01, String inspRemark01, Integer inspId) {
		this.inspTime01 = inspTime01;
		this.inspTp01 = inspTp01;
		this.inspHum01 = inspHum01;
		this.inspHj01 = inspHj01;
		this.inspQj01 = inspQj01;
		this.inspYx01 = inspYx01;
		this.inspYw01 = inspYw01;
		this.inspPw01 = inspPw01;
		this.inspBat01 = inspBat01;
		this.inspAir01 = inspAir01;
		this.inspFire01 = inspFire01;
		this.inspPs101 = inspPs101;
		this.inspPs201 = inspPs201;
		this.inspPs301 = inspPs301;
		this.inspPs401 = inspPs401;
		this.inspOttest01 = inspOttest01;
		this.inspMiu01 = inspMiu01;
		this.inspRemark01 = inspRemark01;
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

	@Column(name = "insp_time_01", length = 50)
	public String getInspTime01() {
		return this.inspTime01;
	}

	public void setInspTime01(String inspTime01) {
		this.inspTime01 = inspTime01;
	}

	@Column(name = "insp_tp_01", length = 5)
	public String getInspTp01() {
		return this.inspTp01;
	}

	public void setInspTp01(String inspTp01) {
		this.inspTp01 = inspTp01;
	}

	@Column(name = "insp_hum_01", length = 5)
	public String getInspHum01() {
		return this.inspHum01;
	}

	public void setInspHum01(String inspHum01) {
		this.inspHum01 = inspHum01;
	}

	@Column(name = "insp_hj_01", length = 5)
	public String getInspHj01() {
		return this.inspHj01;
	}

	public void setInspHj01(String inspHj01) {
		this.inspHj01 = inspHj01;
	}

	@Column(name = "insp_qj_01", length = 5)
	public String getInspQj01() {
		return this.inspQj01;
	}

	public void setInspQj01(String inspQj01) {
		this.inspQj01 = inspQj01;
	}

	@Column(name = "insp_yx_01", length = 5)
	public String getInspYx01() {
		return this.inspYx01;
	}

	public void setInspYx01(String inspYx01) {
		this.inspYx01 = inspYx01;
	}

	@Column(name = "insp_yw_01", length = 5)
	public String getInspYw01() {
		return this.inspYw01;
	}

	public void setInspYw01(String inspYw01) {
		this.inspYw01 = inspYw01;
	}

	@Column(name = "insp_pw_01", length = 5)
	public String getInspPw01() {
		return this.inspPw01;
	}

	public void setInspPw01(String inspPw01) {
		this.inspPw01 = inspPw01;
	}

	@Column(name = "insp_bat_01", length = 5)
	public String getInspBat01() {
		return this.inspBat01;
	}

	public void setInspBat01(String inspBat01) {
		this.inspBat01 = inspBat01;
	}

	@Column(name = "insp_air_01", length = 5)
	public String getInspAir01() {
		return this.inspAir01;
	}

	public void setInspAir01(String inspAir01) {
		this.inspAir01 = inspAir01;
	}

	@Column(name = "insp_fire_01", length = 5)
	public String getInspFire01() {
		return this.inspFire01;
	}

	public void setInspFire01(String inspFire01) {
		this.inspFire01 = inspFire01;
	}

	@Column(name = "insp_ps1_01", length = 5)
	public String getInspPs101() {
		return this.inspPs101;
	}

	public void setInspPs101(String inspPs101) {
		this.inspPs101 = inspPs101;
	}

	@Column(name = "insp_ps2_01", length = 5)
	public String getInspPs201() {
		return this.inspPs201;
	}

	public void setInspPs201(String inspPs201) {
		this.inspPs201 = inspPs201;
	}

	@Column(name = "insp_ps3_01", length = 5)
	public String getInspPs301() {
		return this.inspPs301;
	}

	public void setInspPs301(String inspPs301) {
		this.inspPs301 = inspPs301;
	}

	@Column(name = "insp_ps4_01", length = 5)
	public String getInspPs401() {
		return this.inspPs401;
	}

	public void setInspPs401(String inspPs401) {
		this.inspPs401 = inspPs401;
	}

	@Column(name = "insp_ottest_01", length = 5)
	public String getInspOttest01() {
		return this.inspOttest01;
	}

	public void setInspOttest01(String inspOttest01) {
		this.inspOttest01 = inspOttest01;
	}

	@Column(name = "insp_miu_01", length = 5)
	public String getInspMiu01() {
		return this.inspMiu01;
	}

	public void setInspMiu01(String inspMiu01) {
		this.inspMiu01 = inspMiu01;
	}

	@Column(name = "insp_remark_01")
	public String getInspRemark01() {
		return this.inspRemark01;
	}

	public void setInspRemark01(String inspRemark01) {
		this.inspRemark01 = inspRemark01;
	}

	@Column(name = "insp_id")
	public Integer getInspId() {
		return this.inspId;
	}

	public void setInspId(Integer inspId) {
		this.inspId = inspId;
	}

}