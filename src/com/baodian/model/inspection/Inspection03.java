package com.baodian.model.inspection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Inspection03 entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "inspection_03")
public class Inspection03 implements java.io.Serializable {

	// Fields

	private Integer id;
	private String inspTime03;
	private String inspTp03;
	private String inspHum03;
	private String inspHj03;
	private String inspQj03;
	private String inspYx03;
	private String inspYw03;
	private String inspPw03;
	private String inspAir03;
	private String inspFire03;
	private String inspBatpw103;
	private String inspBat103;
	private String inspBatpw203;
	private String inspBat203;
	private String inspBatpw303;
	private String inspBat303;
	private String inspBatpw403;
	private String inspBat403;
	private String inspBatpw503;
	private String inspBat503;
	private String inspBatpw603;
	private String inspBat603;
	private String inspUps1Out03;
	private String inspUps1By03;
	private String inspUps1Batt03;
	private String inspUps1Outb03;
	private String inspUps1Battb03;
	private String inspUps103;
	private String inspUps2Out03;
	private String inspUps2By03;
	private String inspUps2Batt03;
	private String inspUps2Outb03;
	private String inspUps2Battb03;
	private String inspUps203;
	private String inspUps3Out03;
	private String inspUps3By03;
	private String inspUps3Batt03;
	private String inspUps3Outb03;
	private String inspUps3Battb03;
	private String inspUps303;
	private String inspUps4Out03;
	private String inspUps4By03;
	private String inspUps4Batt03;
	private String inspUps4Outb03;
	private String inspUps4Battb03;
	private String inspUps403;
	private String inspRemark03;
	private Integer inspId;

	// Constructors

	/** default constructor */
	public Inspection03() {
	}

	/** full constructor */
	public Inspection03(String inspTime03, String inspTp03, String inspHum03,
			String inspHj03, String inspQj03, String inspYx03, String inspYw03,
			String inspPw03, String inspAir03, String inspFire03,
			String inspBatpw103, String inspBat103, String inspBatpw203,
			String inspBat203, String inspBatpw303, String inspBat303,
			String inspBatpw403, String inspBat403, String inspBatpw503,
			String inspBat503, String inspBatpw603, String inspBat603,
			String inspUps1Out03, String inspUps1By03, String inspUps1Batt03,
			String inspUps1Outb03, String inspUps1Battb03, String inspUps103,
			String inspUps2Out03, String inspUps2By03, String inspUps2Batt03,
			String inspUps2Outb03, String inspUps2Battb03, String inspUps203,
			String inspUps3Out03, String inspUps3By03, String inspUps3Batt03,
			String inspUps3Outb03, String inspUps3Battb03, String inspUps303,
			String inspUps4Out03, String inspUps4By03, String inspUps4Batt03,
			String inspUps4Outb03, String inspUps4Battb03, String inspUps403,
			String inspRemark03, Integer inspId) {
		this.inspTime03 = inspTime03;
		this.inspTp03 = inspTp03;
		this.inspHum03 = inspHum03;
		this.inspHj03 = inspHj03;
		this.inspQj03 = inspQj03;
		this.inspYx03 = inspYx03;
		this.inspYw03 = inspYw03;
		this.inspPw03 = inspPw03;
		this.inspAir03 = inspAir03;
		this.inspFire03 = inspFire03;
		this.inspBatpw103 = inspBatpw103;
		this.inspBat103 = inspBat103;
		this.inspBatpw203 = inspBatpw203;
		this.inspBat203 = inspBat203;
		this.inspBatpw303 = inspBatpw303;
		this.inspBat303 = inspBat303;
		this.inspBatpw403 = inspBatpw403;
		this.inspBat403 = inspBat403;
		this.inspBatpw503 = inspBatpw503;
		this.inspBat503 = inspBat503;
		this.inspBatpw603 = inspBatpw603;
		this.inspBat603 = inspBat603;
		this.inspUps1Out03 = inspUps1Out03;
		this.inspUps1By03 = inspUps1By03;
		this.inspUps1Batt03 = inspUps1Batt03;
		this.inspUps1Outb03 = inspUps1Outb03;
		this.inspUps1Battb03 = inspUps1Battb03;
		this.inspUps103 = inspUps103;
		this.inspUps2Out03 = inspUps2Out03;
		this.inspUps2By03 = inspUps2By03;
		this.inspUps2Batt03 = inspUps2Batt03;
		this.inspUps2Outb03 = inspUps2Outb03;
		this.inspUps2Battb03 = inspUps2Battb03;
		this.inspUps203 = inspUps203;
		this.inspUps3Out03 = inspUps3Out03;
		this.inspUps3By03 = inspUps3By03;
		this.inspUps3Batt03 = inspUps3Batt03;
		this.inspUps3Outb03 = inspUps3Outb03;
		this.inspUps3Battb03 = inspUps3Battb03;
		this.inspUps303 = inspUps303;
		this.inspUps4Out03 = inspUps4Out03;
		this.inspUps4By03 = inspUps4By03;
		this.inspUps4Batt03 = inspUps4Batt03;
		this.inspUps4Outb03 = inspUps4Outb03;
		this.inspUps4Battb03 = inspUps4Battb03;
		this.inspUps403 = inspUps403;
		this.inspRemark03 = inspRemark03;
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

	@Column(name = "insp_time_03", length = 50)
	public String getInspTime03() {
		return this.inspTime03;
	}

	public void setInspTime03(String inspTime03) {
		this.inspTime03 = inspTime03;
	}

	@Column(name = "insp_tp_03", length = 5)
	public String getInspTp03() {
		return this.inspTp03;
	}

	public void setInspTp03(String inspTp03) {
		this.inspTp03 = inspTp03;
	}

	@Column(name = "insp_hum_03", length = 5)
	public String getInspHum03() {
		return this.inspHum03;
	}

	public void setInspHum03(String inspHum03) {
		this.inspHum03 = inspHum03;
	}

	@Column(name = "insp_hj_03", length = 5)
	public String getInspHj03() {
		return this.inspHj03;
	}

	public void setInspHj03(String inspHj03) {
		this.inspHj03 = inspHj03;
	}

	@Column(name = "insp_qj_03", length = 5)
	public String getInspQj03() {
		return this.inspQj03;
	}

	public void setInspQj03(String inspQj03) {
		this.inspQj03 = inspQj03;
	}

	@Column(name = "insp_yx_03", length = 5)
	public String getInspYx03() {
		return this.inspYx03;
	}

	public void setInspYx03(String inspYx03) {
		this.inspYx03 = inspYx03;
	}

	@Column(name = "insp_yw_03", length = 5)
	public String getInspYw03() {
		return this.inspYw03;
	}

	public void setInspYw03(String inspYw03) {
		this.inspYw03 = inspYw03;
	}

	@Column(name = "insp_pw_03", length = 5)
	public String getInspPw03() {
		return this.inspPw03;
	}

	public void setInspPw03(String inspPw03) {
		this.inspPw03 = inspPw03;
	}

	@Column(name = "insp_air_03", length = 5)
	public String getInspAir03() {
		return this.inspAir03;
	}

	public void setInspAir03(String inspAir03) {
		this.inspAir03 = inspAir03;
	}

	@Column(name = "insp_fire_03", length = 5)
	public String getInspFire03() {
		return this.inspFire03;
	}

	public void setInspFire03(String inspFire03) {
		this.inspFire03 = inspFire03;
	}

	@Column(name = "insp_batpw1_03", length = 5)
	public String getInspBatpw103() {
		return this.inspBatpw103;
	}

	public void setInspBatpw103(String inspBatpw103) {
		this.inspBatpw103 = inspBatpw103;
	}

	@Column(name = "insp_bat1_03", length = 5)
	public String getInspBat103() {
		return this.inspBat103;
	}

	public void setInspBat103(String inspBat103) {
		this.inspBat103 = inspBat103;
	}

	@Column(name = "insp_batpw2_03", length = 5)
	public String getInspBatpw203() {
		return this.inspBatpw203;
	}

	public void setInspBatpw203(String inspBatpw203) {
		this.inspBatpw203 = inspBatpw203;
	}

	@Column(name = "insp_bat2_03", length = 5)
	public String getInspBat203() {
		return this.inspBat203;
	}

	public void setInspBat203(String inspBat203) {
		this.inspBat203 = inspBat203;
	}

	@Column(name = "insp_batpw3_03", length = 5)
	public String getInspBatpw303() {
		return this.inspBatpw303;
	}

	public void setInspBatpw303(String inspBatpw303) {
		this.inspBatpw303 = inspBatpw303;
	}

	@Column(name = "insp_bat3_03", length = 5)
	public String getInspBat303() {
		return this.inspBat303;
	}

	public void setInspBat303(String inspBat303) {
		this.inspBat303 = inspBat303;
	}

	@Column(name = "insp_batpw4_03", length = 5)
	public String getInspBatpw403() {
		return this.inspBatpw403;
	}

	public void setInspBatpw403(String inspBatpw403) {
		this.inspBatpw403 = inspBatpw403;
	}

	@Column(name = "insp_bat4_03", length = 5)
	public String getInspBat403() {
		return this.inspBat403;
	}

	public void setInspBat403(String inspBat403) {
		this.inspBat403 = inspBat403;
	}

	@Column(name = "insp_batpw5_03", length = 5)
	public String getInspBatpw503() {
		return this.inspBatpw503;
	}

	public void setInspBatpw503(String inspBatpw503) {
		this.inspBatpw503 = inspBatpw503;
	}

	@Column(name = "insp_bat5_03", length = 5)
	public String getInspBat503() {
		return this.inspBat503;
	}

	public void setInspBat503(String inspBat503) {
		this.inspBat503 = inspBat503;
	}

	@Column(name = "insp_batpw6_03", length = 5)
	public String getInspBatpw603() {
		return this.inspBatpw603;
	}

	public void setInspBatpw603(String inspBatpw603) {
		this.inspBatpw603 = inspBatpw603;
	}

	@Column(name = "insp_bat6_03", length = 5)
	public String getInspBat603() {
		return this.inspBat603;
	}

	public void setInspBat603(String inspBat603) {
		this.inspBat603 = inspBat603;
	}

	@Column(name = "insp_ups1_out_03", length = 5)
	public String getInspUps1Out03() {
		return this.inspUps1Out03;
	}

	public void setInspUps1Out03(String inspUps1Out03) {
		this.inspUps1Out03 = inspUps1Out03;
	}

	@Column(name = "insp_ups1_by_03", length = 5)
	public String getInspUps1By03() {
		return this.inspUps1By03;
	}

	public void setInspUps1By03(String inspUps1By03) {
		this.inspUps1By03 = inspUps1By03;
	}

	@Column(name = "insp_ups1_batt_03", length = 5)
	public String getInspUps1Batt03() {
		return this.inspUps1Batt03;
	}

	public void setInspUps1Batt03(String inspUps1Batt03) {
		this.inspUps1Batt03 = inspUps1Batt03;
	}

	@Column(name = "insp_ups1_outb_03", length = 5)
	public String getInspUps1Outb03() {
		return this.inspUps1Outb03;
	}

	public void setInspUps1Outb03(String inspUps1Outb03) {
		this.inspUps1Outb03 = inspUps1Outb03;
	}

	@Column(name = "insp_ups1_battb_03", length = 5)
	public String getInspUps1Battb03() {
		return this.inspUps1Battb03;
	}

	public void setInspUps1Battb03(String inspUps1Battb03) {
		this.inspUps1Battb03 = inspUps1Battb03;
	}

	@Column(name = "insp_ups1_03", length = 5)
	public String getInspUps103() {
		return this.inspUps103;
	}

	public void setInspUps103(String inspUps103) {
		this.inspUps103 = inspUps103;
	}

	@Column(name = "insp_ups2_out_03", length = 5)
	public String getInspUps2Out03() {
		return this.inspUps2Out03;
	}

	public void setInspUps2Out03(String inspUps2Out03) {
		this.inspUps2Out03 = inspUps2Out03;
	}

	@Column(name = "insp_ups2_by_03", length = 5)
	public String getInspUps2By03() {
		return this.inspUps2By03;
	}

	public void setInspUps2By03(String inspUps2By03) {
		this.inspUps2By03 = inspUps2By03;
	}

	@Column(name = "insp_ups2_batt_03", length = 5)
	public String getInspUps2Batt03() {
		return this.inspUps2Batt03;
	}

	public void setInspUps2Batt03(String inspUps2Batt03) {
		this.inspUps2Batt03 = inspUps2Batt03;
	}

	@Column(name = "insp_ups2_outb_03", length = 5)
	public String getInspUps2Outb03() {
		return this.inspUps2Outb03;
	}

	public void setInspUps2Outb03(String inspUps2Outb03) {
		this.inspUps2Outb03 = inspUps2Outb03;
	}

	@Column(name = "insp_ups2_battb_03", length = 5)
	public String getInspUps2Battb03() {
		return this.inspUps2Battb03;
	}

	public void setInspUps2Battb03(String inspUps2Battb03) {
		this.inspUps2Battb03 = inspUps2Battb03;
	}

	@Column(name = "insp_ups2_03", length = 5)
	public String getInspUps203() {
		return this.inspUps203;
	}

	public void setInspUps203(String inspUps203) {
		this.inspUps203 = inspUps203;
	}

	@Column(name = "insp_ups3_out_03", length = 5)
	public String getInspUps3Out03() {
		return this.inspUps3Out03;
	}

	public void setInspUps3Out03(String inspUps3Out03) {
		this.inspUps3Out03 = inspUps3Out03;
	}

	@Column(name = "insp_ups3_by_03", length = 5)
	public String getInspUps3By03() {
		return this.inspUps3By03;
	}

	public void setInspUps3By03(String inspUps3By03) {
		this.inspUps3By03 = inspUps3By03;
	}

	@Column(name = "insp_ups3_batt_03", length = 5)
	public String getInspUps3Batt03() {
		return this.inspUps3Batt03;
	}

	public void setInspUps3Batt03(String inspUps3Batt03) {
		this.inspUps3Batt03 = inspUps3Batt03;
	}

	@Column(name = "insp_ups3_outb_03", length = 5)
	public String getInspUps3Outb03() {
		return this.inspUps3Outb03;
	}

	public void setInspUps3Outb03(String inspUps3Outb03) {
		this.inspUps3Outb03 = inspUps3Outb03;
	}

	@Column(name = "insp_ups3_battb_03", length = 5)
	public String getInspUps3Battb03() {
		return this.inspUps3Battb03;
	}

	public void setInspUps3Battb03(String inspUps3Battb03) {
		this.inspUps3Battb03 = inspUps3Battb03;
	}

	@Column(name = "insp_ups3_03", length = 5)
	public String getInspUps303() {
		return this.inspUps303;
	}

	public void setInspUps303(String inspUps303) {
		this.inspUps303 = inspUps303;
	}

	@Column(name = "insp_ups4_out_03", length = 5)
	public String getInspUps4Out03() {
		return this.inspUps4Out03;
	}

	public void setInspUps4Out03(String inspUps4Out03) {
		this.inspUps4Out03 = inspUps4Out03;
	}

	@Column(name = "insp_ups4_by_03", length = 5)
	public String getInspUps4By03() {
		return this.inspUps4By03;
	}

	public void setInspUps4By03(String inspUps4By03) {
		this.inspUps4By03 = inspUps4By03;
	}

	@Column(name = "insp_ups4_batt_03", length = 5)
	public String getInspUps4Batt03() {
		return this.inspUps4Batt03;
	}

	public void setInspUps4Batt03(String inspUps4Batt03) {
		this.inspUps4Batt03 = inspUps4Batt03;
	}

	@Column(name = "insp_ups4_outb_03", length = 5)
	public String getInspUps4Outb03() {
		return this.inspUps4Outb03;
	}

	public void setInspUps4Outb03(String inspUps4Outb03) {
		this.inspUps4Outb03 = inspUps4Outb03;
	}

	@Column(name = "insp_ups4_battb_03", length = 5)
	public String getInspUps4Battb03() {
		return this.inspUps4Battb03;
	}

	public void setInspUps4Battb03(String inspUps4Battb03) {
		this.inspUps4Battb03 = inspUps4Battb03;
	}

	@Column(name = "insp_ups4_03", length = 5)
	public String getInspUps403() {
		return this.inspUps403;
	}

	public void setInspUps403(String inspUps403) {
		this.inspUps403 = inspUps403;
	}

	@Column(name = "insp_remark_03")
	public String getInspRemark03() {
		return this.inspRemark03;
	}

	public void setInspRemark03(String inspRemark03) {
		this.inspRemark03 = inspRemark03;
	}

	@Column(name = "insp_id")
	public Integer getInspId() {
		return this.inspId;
	}

	public void setInspId(Integer inspId) {
		this.inspId = inspId;
	}

}