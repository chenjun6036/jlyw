package com.jlyw.hibernate;

/**
 * Privilege entity. @author MyEclipse Persistence Tools
 */

public class Privilege implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysResources sysResources;
	private String name;
	private String description;
	private Integer status;
	private String parameters;

	// Constructors

	/** default constructor */
	public Privilege() {
	}

	/** minimal constructor */
	public Privilege(SysResources sysResources, String name, Integer status) {
		this.sysResources = sysResources;
		this.name = name;
		this.status = status;
	}

	/** full constructor */
	public Privilege(SysResources sysResources, String name,
			String description, Integer status, String parameters) {
		this.sysResources = sysResources;
		this.name = name;
		this.description = description;
		this.status = status;
		this.parameters = parameters;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysResources getSysResources() {
		return this.sysResources;
	}

	public void setSysResources(SysResources sysResources) {
		this.sysResources = sysResources;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getParameters() {
		return this.parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

}