package com.jlyw.hibernate.view;

/**
 * ViewTargetApplianceModelAccuracyRangeId entity. @author MyEclipse Persistence
 * Tools
 */

public class ViewTargetApplianceModelAccuracyRangeId implements
		java.io.Serializable {

	// Fields

	private Integer targetApplianceId;
	private Integer modelId;
	private Integer accuracyId;
	private Integer rangeId;
	// Constructors

	/** default constructor */
	public ViewTargetApplianceModelAccuracyRangeId() {
	}

	public ViewTargetApplianceModelAccuracyRangeId(Integer targetApplianceId,
			Integer modelId, Integer accuracyId, Integer rangeId) {
		super();
		this.targetApplianceId = targetApplianceId;
		this.modelId = modelId;
		this.accuracyId = accuracyId;
		this.rangeId = rangeId;
	}

	public Integer getTargetApplianceId() {
		return targetApplianceId;
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