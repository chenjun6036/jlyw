package com.jlyw.hibernate;

/**
 * TgtAppStdApp entity. @author MyEclipse Persistence Tools
 */

public class TgtAppStdApp implements java.io.Serializable {

	// Fields

	private Integer id;
	private StandardAppliance standardAppliance;
	private TargetAppliance targetAppliance;

	// Constructors

	/** default constructor */
	public TgtAppStdApp() {
	}

	/** full constructor */
	public TgtAppStdApp(StandardAppliance standardAppliance,
			TargetAppliance targetAppliance) {
		this.standardAppliance = standardAppliance;
		this.targetAppliance = targetAppliance;
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

	public TargetAppliance getTargetAppliance() {
		return this.targetAppliance;
	}

	public void setTargetAppliance(TargetAppliance targetAppliance) {
		this.targetAppliance = targetAppliance;
	}

}