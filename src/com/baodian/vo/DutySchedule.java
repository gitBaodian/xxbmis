package com.baodian.vo;

public class DutySchedule {
	private String class_;//班值
	//private String squad = "";//班长
	private String crew;//成员
	private String bc;//班次(白班)
	
	/**
	 * 班值
	 */
	public String getClass_() {
		return class_;
	}
	public void setClass_(String class_) {
		this.class_ = class_;
	}
	/**
	 * 班长
	 */
	//public String getSquad() {
	//	return squad;
	//}
	//public void setSquad(String squad) {
	//	this.squad = squad;
	//}
	/**
	 * 班次(白班 后夜 前夜 休息)
	 */
	public String getBc() {
		return bc;
	}
	public void setBc(String bc) {
		this.bc = bc;
	}
	/**
	 * 成员
	 */
	public String getCrew() {
		return crew;
	}
	public void setCrew(String crew) {
		this.crew = crew;
	}
}
