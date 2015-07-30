package com.baodian.model.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Equipment entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "equipment")
public class Equipment implements java.io.Serializable {

	// Fields

	private String ip;
	private String hostsname;
	private String role;
	private String system;
	private String cpu;
	private String hard;
	private String motherboard;
	private String memory;
	private String type;
	private String env;
	private String other;

	// Constructors

	/** default constructor */
	public Equipment() {
	}

	/** minimal constructor */
	public Equipment(String ip) {
		this.ip = ip;
	}

	/** full constructor */
	public Equipment(String ip, String hostsname, String role, String system,
			String cpu, String hard, String motherboard, String memory,
			String type, String env, String other) {
		this.ip = ip;
		this.hostsname = hostsname;
		this.role = role;
		this.system = system;
		this.cpu = cpu;
		this.hard = hard;
		this.motherboard = motherboard;
		this.memory = memory;
		this.type = type;
		this.env = env;
		this.other = other;
	}

	// Property accessors
	@Id
	@Column(name = "ip", unique = true, nullable = false, length = 20)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "hostsname", length = 50)
	public String getHostsname() {
		return this.hostsname;
	}

	public void setHostsname(String hostsname) {
		this.hostsname = hostsname;
	}

	@Column(name = "role")
	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "system", length = 50)
	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	@Column(name = "cpu", length = 50)
	public String getCpu() {
		return this.cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	@Column(name = "hard", length = 50)
	public String getHard() {
		return this.hard;
	}

	public void setHard(String hard) {
		this.hard = hard;
	}

	@Column(name = "motherboard", length = 50)
	public String getMotherboard() {
		return this.motherboard;
	}

	public void setMotherboard(String motherboard) {
		this.motherboard = motherboard;
	}

	@Column(name = "memory", length = 50)
	public String getMemory() {
		return this.memory;
	}

	public void setMemory(String memory) {
		this.memory = memory;
	}

	@Column(name = "type", length = 50)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "env", length = 20)
	public String getEnv() {
		return this.env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	@Column(name = "other")
	public String getOther() {
		return this.other;
	}

	public void setOther(String other) {
		this.other = other;
	}

}