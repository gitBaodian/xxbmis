package com.baodian.model.device;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Env entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "env")
public class Env implements java.io.Serializable {

	// Fields

	private Integer id;
	private String env;
	private String envName;

	// Constructors

	/** default constructor */
	public Env() {
	}

	/** full constructor */
	public Env(String env, String envName) {
		this.env = env;
		this.envName = envName;
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

	@Column(name = "env", length = 10)
	public String getEnv() {
		return this.env;
	}

	public void setEnv(String env) {
		this.env = env;
	}

	@Column(name = "env_name", length = 10)
	public String getEnvName() {
		return this.envName;
	}

	public void setEnvName(String envName) {
		this.envName = envName;
	}

}