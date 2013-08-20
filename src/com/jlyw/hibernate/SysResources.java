package com.jlyw.hibernate;

/**
 * SysResources entity. @author MyEclipse Persistence Tools
 */

public class SysResources implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String description;
	private String mappingUrl;
	private Integer status;

	// Constructors

	/** default constructor */
	public SysResources() {
	}

	/** minimal constructor */
	public SysResources(String name, String mappingUrl, Integer status) {
		this.name = name;
		this.mappingUrl = mappingUrl;
		this.status = status;
	}

	/** full constructor */
	public SysResources(String name, String description, String mappingUrl,
			Integer status) {
		this.name = name;
		this.description = description;
		this.mappingUrl = mappingUrl;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMappingUrl() {
		return this.mappingUrl;
	}

	public void setMappingUrl(String mappingUrl) {
		this.mappingUrl = mappingUrl;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}