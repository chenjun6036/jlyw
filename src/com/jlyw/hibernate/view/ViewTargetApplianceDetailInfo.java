package com.jlyw.hibernate.view;

/**
 * ViewTargetApplianceDetailInfo entity. @author MyEclipse Persistence Tools
 */

public class ViewTargetApplianceDetailInfo implements java.io.Serializable {

	// Fields

	private ViewTargetApplianceDetailInfoId id;
	private Integer speciesId;
	private String standardNameName;
	private String standardNameNameEn;
	private String standardNameBrief;
	private Boolean standardNameHoldMany;
	private Integer standardNameStatus;
	
	private String targetApplianceName;
	private String targetApplianceNameEn;
	private String targetApplianceBrief;
	private String targetApplianceCode;
	private Double fee;
	private Double srfee;
	private Double mrfee;
	private Double lrfee;
	private Integer promiseDuration;
	private Integer testCycle;
	private Integer targetApplianceStatus;
	private Integer certification;
	private String targetApplianceRemark;
	
	private String model;
	private String modelEn;
	
	private String accuracy;
	private String accuracyEn;
	
	private String range;
	private String rangeEn;

	
	// Constructors

	/** default constructor */
	public ViewTargetApplianceDetailInfo() {
	}

	/** full constructor */
	public ViewTargetApplianceDetailInfo(ViewTargetApplianceDetailInfoId id,
			Integer speciesId, String standardNameName,
			String standardNameNameEn, String standardNameBrief,
			Boolean standardNameHoldMany, Integer standardNameStatus,
			String targetApplianceName, String targetApplianceNameEn,
			String targetApplianceBrief, String targetApplianceCode,
			Double fee, Double srfee, Double mrfee, Double lrfee,
			Integer promiseDuration, Integer testCycle,
			Integer targetApplianceStatus, Integer certification,
			String targetApplianceRemark, String model,
			String modelEn, String accuracy,
			String accuracyEn, String range, String rangeEn) {
		super();
		this.id = id;
		this.speciesId = speciesId;
		this.standardNameName = standardNameName;
		this.standardNameNameEn = standardNameNameEn;
		this.standardNameBrief = standardNameBrief;
		this.standardNameHoldMany = standardNameHoldMany;
		this.standardNameStatus = standardNameStatus;
		this.targetApplianceName = targetApplianceName;
		this.targetApplianceNameEn = targetApplianceNameEn;
		this.targetApplianceBrief = targetApplianceBrief;
		this.targetApplianceCode = targetApplianceCode;
		this.fee = fee;
		this.srfee = srfee;
		this.mrfee = mrfee;
		this.lrfee = lrfee;
		this.promiseDuration = promiseDuration;
		this.testCycle = testCycle;
		this.targetApplianceStatus = targetApplianceStatus;
		this.certification = certification;
		this.targetApplianceRemark = targetApplianceRemark;
		this.model = model;
		this.modelEn = modelEn;
		this.accuracy = accuracy;
		this.accuracyEn = accuracyEn;
		this.range = range;
		this.rangeEn = rangeEn;
	}


	// Property accessors
	public ViewTargetApplianceDetailInfoId getId() {
		return this.id;
	}
	public void setId(ViewTargetApplianceDetailInfoId id) {
		this.id = id;
	}

	public Integer getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Integer speciesId) {
		this.speciesId = speciesId;
	}

	public String getStandardNameName() {
		return standardNameName;
	}

	public void setStandardNameName(String standardNameName) {
		this.standardNameName = standardNameName;
	}

	public String getStandardNameNameEn() {
		return standardNameNameEn;
	}

	public void setStandardNameNameEn(String standardNameNameEn) {
		this.standardNameNameEn = standardNameNameEn;
	}

	public String getStandardNameBrief() {
		return standardNameBrief;
	}

	public void setStandardNameBrief(String standardNameBrief) {
		this.standardNameBrief = standardNameBrief;
	}

	public Boolean getStandardNameHoldMany() {
		return standardNameHoldMany;
	}

	public void setStandardNameHoldMany(Boolean standardNameHoldMany) {
		this.standardNameHoldMany = standardNameHoldMany;
	}

	public Integer getStandardNameStatus() {
		return standardNameStatus;
	}

	public void setStandardNameStatus(Integer standardNameStatus) {
		this.standardNameStatus = standardNameStatus;
	}

	public String getTargetApplianceName() {
		return targetApplianceName;
	}

	public void setTargetApplianceName(String targetApplianceName) {
		this.targetApplianceName = targetApplianceName;
	}

	public String getTargetApplianceNameEn() {
		return targetApplianceNameEn;
	}

	public void setTargetApplianceNameEn(String targetApplianceNameEn) {
		this.targetApplianceNameEn = targetApplianceNameEn;
	}

	public String getTargetApplianceBrief() {
		return targetApplianceBrief;
	}

	public void setTargetApplianceBrief(String targetApplianceBrief) {
		this.targetApplianceBrief = targetApplianceBrief;
	}

	public String getTargetApplianceCode() {
		return targetApplianceCode;
	}

	public void setTargetApplianceCode(String targetApplianceCode) {
		this.targetApplianceCode = targetApplianceCode;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getSrfee() {
		return srfee;
	}

	public void setSrfee(Double srfee) {
		this.srfee = srfee;
	}

	public Double getMrfee() {
		return mrfee;
	}

	public void setMrfee(Double mrfee) {
		this.mrfee = mrfee;
	}

	public Double getLrfee() {
		return lrfee;
	}

	public void setLrfee(Double lrfee) {
		this.lrfee = lrfee;
	}

	public Integer getPromiseDuration() {
		return promiseDuration;
	}

	public void setPromiseDuration(Integer promiseDuration) {
		this.promiseDuration = promiseDuration;
	}

	public Integer getTestCycle() {
		return testCycle;
	}

	public void setTestCycle(Integer testCycle) {
		this.testCycle = testCycle;
	}

	public Integer getTargetApplianceStatus() {
		return targetApplianceStatus;
	}

	public void setTargetApplianceStatus(Integer targetApplianceStatus) {
		this.targetApplianceStatus = targetApplianceStatus;
	}

	public Integer getCertification() {
		return certification;
	}

	public void setCertification(Integer certification) {
		this.certification = certification;
	}

	public String getTargetApplianceRemark() {
		return targetApplianceRemark;
	}

	public void setTargetApplianceRemark(String targetApplianceRemark) {
		this.targetApplianceRemark = targetApplianceRemark;
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