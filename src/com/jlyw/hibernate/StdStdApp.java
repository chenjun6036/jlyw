package com.jlyw.hibernate;

/**
 * StdStdApp entity. @author MyEclipse Persistence Tools
 */

public class StdStdApp implements java.io.Serializable {

	// Fields

	private Integer id;
	private StandardAppliance standardAppliance;
	private Standard standard;
	private Boolean isMain;
	private Integer sequence;

	// Constructors

	/** default constructor */
	public StdStdApp() {
	}

	/** full constructor */
	public StdStdApp(StandardAppliance standardAppliance, Standard standard,
			Boolean isMain, Integer sequence) {
		this.standardAppliance = standardAppliance;
		this.standard = standard;
		this.isMain = isMain;
		this.sequence = sequence;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StandardAppliance getStandardAppliance() {
		return this.standardAppliance;
	}

	public void setStandardAppliance(StandardAppliance standardAppliance) {
		this.standardAppliance = standardAppliance;
	}

	public Standard getStandard() {
		return this.standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public Boolean getIsMain() {
		return this.isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}