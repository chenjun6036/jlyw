package com.jlyw.hibernate;

/**
 * TargetAppliance entity. @author MyEclipse Persistence Tools
 */

public class TargetAppliance implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceStandardName applianceStandardName;
	private String name;
	private String nameEn;
	private String brief;
	private String code;
	private Double fee;
	private Double srfee;
	private Double mrfee;
	private Double lrfee;
	private Integer promiseDuration;
	private Integer testCycle;
	private Integer status;
	private Integer certification;
	private String remark;

	// Constructors

	/** default constructor */
	public TargetAppliance() {
	}

	/** minimal constructor */
	public TargetAppliance(ApplianceStandardName applianceStandardName,
			String name, String nameEn, String brief, String code,
			Integer status) {
		this.applianceStandardName = applianceStandardName;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.code = code;
		this.status = status;
	}

	/** full constructor */
	public TargetAppliance(ApplianceStandardName applianceStandardName,
			String name, String nameEn, String brief, String code, Double fee,
			Double srfee, Double mrfee, Double lrfee, Integer promiseDuration,
			Integer testCycle, Integer status, Integer certification,
			String remark) {
		this.applianceStandardName = applianceStandardName;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.code = code;
		this.fee = fee;
		this.srfee = srfee;
		this.mrfee = mrfee;
		this.lrfee = lrfee;
		this.promiseDuration = promiseDuration;
		this.testCycle = testCycle;
		this.status = status;
		this.certification = certification;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplianceStandardName getApplianceStandardName() {
		return this.applianceStandardName;
	}

	public void setApplianceStandardName(
			ApplianceStandardName applianceStandardName) {
		this.applianceStandardName = applianceStandardName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getSrfee() {
		return this.srfee;
	}

	public void setSrfee(Double srfee) {
		this.srfee = srfee;
	}

	public Double getMrfee() {
		return this.mrfee;
	}

	public void setMrfee(Double mrfee) {
		this.mrfee = mrfee;
	}

	public Double getLrfee() {
		return this.lrfee;
	}

	public void setLrfee(Double lrfee) {
		this.lrfee = lrfee;
	}

	public Integer getPromiseDuration() {
		return this.promiseDuration;
	}

	public void setPromiseDuration(Integer promiseDuration) {
		this.promiseDuration = promiseDuration;
	}

	public Integer getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Integer testCycle) {
		this.testCycle = testCycle;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getCertification() {
		return this.certification;
	}

	public void setCertification(Integer certification) {
		this.certification = certification;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}