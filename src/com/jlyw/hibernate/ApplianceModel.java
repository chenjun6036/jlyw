package com.jlyw.hibernate;

/**
 * ApplianceModel entity. @author MyEclipse Persistence Tools
 */

public class ApplianceModel implements java.io.Serializable {

	// Fields

	private Integer id;
	private TargetAppliance targetAppliance;
	private String model;
	private String modelEn;

	// Constructors

	/** default constructor */
	public ApplianceModel() {
	}

	/** minimal constructor */
	public ApplianceModel(TargetAppliance targetAppliance, String model) {
		this.targetAppliance = targetAppliance;
		this.model = model;
	}

	/** full constructor */
	public ApplianceModel(TargetAppliance targetAppliance, String model,
			String modelEn) {
		this.targetAppliance = targetAppliance;
		this.model = model;
		this.modelEn = modelEn;
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

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelEn() {
		return this.modelEn;
	}

	public void setModelEn(String modelEn) {
		this.modelEn = modelEn;
	}

}