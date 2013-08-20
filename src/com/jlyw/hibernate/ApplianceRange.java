package com.jlyw.hibernate;

/**
 * ApplianceRange entity. @author MyEclipse Persistence Tools
 */

public class ApplianceRange implements java.io.Serializable {

	// Fields

	private Integer id;
	private TargetAppliance targetAppliance;
	private String range;
	private String rangeEn;

	// Constructors

	/** default constructor */
	public ApplianceRange() {
	}

	/** minimal constructor */
	public ApplianceRange(TargetAppliance targetAppliance, String range) {
		this.targetAppliance = targetAppliance;
		this.range = range;
	}

	/** full constructor */
	public ApplianceRange(TargetAppliance targetAppliance, String range,
			String rangeEn) {
		this.targetAppliance = targetAppliance;
		this.range = range;
		this.rangeEn = rangeEn;
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

	public String getRange() {
		return this.range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getRangeEn() {
		return this.rangeEn;
	}

	public void setRangeEn(String rangeEn) {
		this.rangeEn = rangeEn;
	}

}