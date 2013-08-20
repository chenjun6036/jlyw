package com.jlyw.hibernate;

/**
 * ApplianceAccuracy entity. @author MyEclipse Persistence Tools
 */

public class ApplianceAccuracy implements java.io.Serializable {

	// Fields

	private Integer id;
	private TargetAppliance targetAppliance;
	private String accuracy;
	private String accuracyEn;

	// Constructors

	/** default constructor */
	public ApplianceAccuracy() {
	}

	/** minimal constructor */
	public ApplianceAccuracy(TargetAppliance targetAppliance, String accuracy) {
		this.targetAppliance = targetAppliance;
		this.accuracy = accuracy;
	}

	/** full constructor */
	public ApplianceAccuracy(TargetAppliance targetAppliance, String accuracy,
			String accuracyEn) {
		this.targetAppliance = targetAppliance;
		this.accuracy = accuracy;
		this.accuracyEn = accuracyEn;
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

	public String getAccuracy() {
		return this.accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getAccuracyEn() {
		return this.accuracyEn;
	}

	public void setAccuracyEn(String accuracyEn) {
		this.accuracyEn = accuracyEn;
	}

}