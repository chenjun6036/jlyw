package com.jlyw.hibernate.view;

/**
 * ViewTargetApplianceDetailInfoId entity. @author MyEclipse Persistence Tools
 */

public class ViewTargetApplianceDetailInfoId implements java.io.Serializable {

	// Fields

	private Integer standardNameId;
	private Integer targetApplianceId;
	private Integer modelId;
	private Integer accuracyId;
	private Integer rangeId;

	// Constructors

	/** default constructor */
	public ViewTargetApplianceDetailInfoId() {
	}

	/** full constructor */
	public ViewTargetApplianceDetailInfoId(Integer standardNameId,
			Integer targetApplianceId, Integer modelId, Integer accuracyId,
			Integer rangeId) {
		super();
		this.standardNameId = standardNameId;
		this.targetApplianceId = targetApplianceId;
		this.modelId = modelId;
		this.accuracyId = accuracyId;
		this.rangeId = rangeId;
	}

	// Property accessors

	public Integer getStandardNameId() {
		return this.standardNameId;
	}
	
	public void setStandardNameId(Integer standardNameId) {
		this.standardNameId = standardNameId;
	}

	public Integer getTargetApplianceId() {
		return this.targetApplianceId;
	}

	public void setTargetApplianceId(Integer targetApplianceId) {
		this.targetApplianceId = targetApplianceId;
	}
	
	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	

	public Integer getAccuracyId() {
		return accuracyId;
	}

	public void setAccuracyId(Integer accuracyId) {
		this.accuracyId = accuracyId;
	}
	

	public Integer getRangeId() {
		return rangeId;
	}

	public void setRangeId(Integer rangeId) {
		this.rangeId = rangeId;
	}
}