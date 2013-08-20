package com.jlyw.hibernate.view;

/**
 * ViewTargetApplianceModelAccuracyRange entity. @author MyEclipse Persistence
 * Tools
 * 用于ApplianceServlet.java类等
 */

public class ViewTargetApplianceModelAccuracyRange implements
		java.io.Serializable {

	// Fields

	private ViewTargetApplianceModelAccuracyRangeId id;
	private Integer standardNameId;
	private String targetApplianceName;
	private Integer targetApplianceStatus;
	private String model;
	private String modelEn;
	private String accuracy;
	private String accuracyEn;
	private String range;
	private String rangeEn;

	// Constructors

	/** default constructor */
	public ViewTargetApplianceModelAccuracyRange() {
	}

	/** full constructor */
	

	// Property accessors

	public ViewTargetApplianceModelAccuracyRangeId getId() {
		return this.id;
	}

	public ViewTargetApplianceModelAccuracyRange(
			ViewTargetApplianceModelAccuracyRangeId id, Integer standardNameId,
			String targetApplianceName, Integer targetApplianceStatus,
			String model, String modelEn, String accuracy, String accuracyEn,
			String range, String rangeEn) {
		super();
		this.id = id;
		this.standardNameId = standardNameId;
		this.targetApplianceName = targetApplianceName;
		this.targetApplianceStatus = targetApplianceStatus;
		this.model = model;
		this.modelEn = modelEn;
		this.accuracy = accuracy;
		this.accuracyEn = accuracyEn;
		this.range = range;
		this.rangeEn = rangeEn;
	}

	public void setId(ViewTargetApplianceModelAccuracyRangeId id) {
		this.id = id;
	}

	public Integer getStandardNameId() {
		return standardNameId;
	}

	public void setStandardNameId(Integer standardNameId) {
		this.standardNameId = standardNameId;
	}

	public String getTargetApplianceName() {
		return targetApplianceName;
	}

	public void setTargetApplianceName(String targetApplianceName) {
		this.targetApplianceName = targetApplianceName;
	}

	public Integer getTargetApplianceStatus() {
		return targetApplianceStatus;
	}

	public void setTargetApplianceStatus(Integer targetApplianceStatus) {
		this.targetApplianceStatus = targetApplianceStatus;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getModelEn() {
		return modelEn;
	}

	public void setModelEn(String modelEn) {
		this.modelEn = modelEn;
	}

	public String getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getAccuracyEn() {
		return accuracyEn;
	}

	public void setAccuracyEn(String accuracyEn) {
		this.accuracyEn = accuracyEn;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getRangeEn() {
		return rangeEn;
	}

	public void setRangeEn(String rangeEn) {
		this.rangeEn = rangeEn;
	}
	

}