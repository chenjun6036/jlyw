package com.jlyw.hibernate;


/**
 * ApplianceSpecies entity. @author MyEclipse Persistence Tools
 */

public class ApplianceSpecies implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceSpecies parentSpecies;
	private String name;
	private String brief;
	private Integer hierarchy;
	private Integer status;
	private Integer sequence;

	// Constructors

	/** default constructor */
	public ApplianceSpecies() {
	}

	/** minimal constructor */
	public ApplianceSpecies(String name, String brief, Integer hierarchy,
			Integer status) {
		this.name = name;
		this.brief = brief;
		this.hierarchy = hierarchy;
		this.status = status;
	}

	/** full constructor */
	public ApplianceSpecies(ApplianceSpecies applianceSpecies, String name,
			String brief, Integer hierarchy, Integer status, Integer sequence) {
		this.parentSpecies = applianceSpecies;
		this.name = name;
		this.brief = brief;
		this.hierarchy = hierarchy;
		this.status = status;
		this.sequence = sequence;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplianceSpecies getParentSpecies() {
		return this.parentSpecies;
	}

	public void setParentSpecies(ApplianceSpecies applianceSpecies) {
		this.parentSpecies = applianceSpecies;
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

	public Integer getHierarchy() {
		return this.hierarchy;
	}

	public void setHierarchy(Integer hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
}