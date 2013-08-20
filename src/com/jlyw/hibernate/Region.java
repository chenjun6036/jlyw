package com.jlyw.hibernate;

/**
 * Region entity. @author MyEclipse Persistence Tools
 */

public class Region implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String name;
	private String brief;
	private Integer status;

	// Constructors

	/** default constructor */
	public Region() {
	}

	/** full constructor */
	public Region(String code, String name, String brief, Integer status) {
		this.code = code;
		this.name = name;
		this.brief = brief;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}