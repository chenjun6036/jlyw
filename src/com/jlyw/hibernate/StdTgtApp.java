package com.jlyw.hibernate;

/**
 * StdTgtApp entity. @author MyEclipse Persistence Tools
 */

public class StdTgtApp implements java.io.Serializable {

	// Fields

	private Integer id;
	private TargetAppliance targetAppliance;
	private Standard standard;

	// Constructors

	/** default constructor */
	public StdTgtApp() {
	}

	/** full constructor */
	public StdTgtApp(TargetAppliance targetAppliance, Standard standard) {
		this.targetAppliance = targetAppliance;
		this.standard = standard;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public TargetAppliance getTargetAppliance() {
		return this.targetAppliance;
	}

	public void setTargetAppliance(TargetAppliance targetAppliance) {
		this.targetAppliance = targetAppliance;
	}

	public Standard getStandard() {
		return this.standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

}