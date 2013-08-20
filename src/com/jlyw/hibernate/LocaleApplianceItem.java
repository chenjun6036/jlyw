package com.jlyw.hibernate;

/**
 * LocaleApplianceItem entity. @author MyEclipse Persistence Tools
 */

public class LocaleApplianceItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private LocaleMission localeMission;
	private Boolean speciesType;
	private Integer applianceSpeciesId;
	private String applianceName;
	private Integer quantity;
	private String assistStaff;
	private String remark;
	private String model;
	private String range;
	private String accuracy;
	private Double testCost;
	private String certType;
	private String appFactoryCode;
	private String appManageCode;
	private String manufacturer;
	private Double repairCost;
	private Double materialCost;

	// Constructors

	/** default constructor */
	public LocaleApplianceItem() {
	}

	/** minimal constructor */
	public LocaleApplianceItem(LocaleMission localeMission, Integer quantity) {
		this.localeMission = localeMission;
		this.quantity = quantity;
	}

	/** full constructor */
	public LocaleApplianceItem(SysUser sysUser, LocaleMission localeMission,
			Boolean speciesType, Integer applianceSpeciesId,
			String applianceName, Integer quantity, String assistStaff,
			String remark, String model, String range, String accuracy,
			Double testCost, String certType, String appFactoryCode,
			String appManageCode, String manufacturer, Double repairCost,
			Double materialCost) {
		this.sysUser = sysUser;
		this.localeMission = localeMission;
		this.speciesType = speciesType;
		this.applianceSpeciesId = applianceSpeciesId;
		this.applianceName = applianceName;
		this.quantity = quantity;
		this.assistStaff = assistStaff;
		this.remark = remark;
		this.model = model;
		this.range = range;
		this.accuracy = accuracy;
		this.testCost = testCost;
		this.certType = certType;
		this.appFactoryCode = appFactoryCode;
		this.appManageCode = appManageCode;
		this.manufacturer = manufacturer;
		this.repairCost = repairCost;
		this.materialCost = materialCost;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public LocaleMission getLocaleMission() {
		return this.localeMission;
	}

	public void setLocaleMission(LocaleMission localeMission) {
		this.localeMission = localeMission;
	}

	public Boolean getSpeciesType() {
		return this.speciesType;
	}

	public void setSpeciesType(Boolean speciesType) {
		this.speciesType = speciesType;
	}

	public Integer getApplianceSpeciesId() {
		return this.applianceSpeciesId;
	}

	public void setApplianceSpeciesId(Integer applianceSpeciesId) {
		this.applianceSpeciesId = applianceSpeciesId;
	}

	public String getApplianceName() {
		return this.applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getAssistStaff() {
		return this.assistStaff;
	}

	public void setAssistStaff(String assistStaff) {
		this.assistStaff = assistStaff;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRange() {
		return this.range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	public String getAccuracy() {
		return this.accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public Double getTestCost() {
		return this.testCost;
	}

	public void setTestCost(Double testCost) {
		this.testCost = testCost;
	}

	public String getCertType() {
		return this.certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getAppFactoryCode() {
		return this.appFactoryCode;
	}

	public void setAppFactoryCode(String appFactoryCode) {
		this.appFactoryCode = appFactoryCode;
	}

	public String getAppManageCode() {
		return this.appManageCode;
	}

	public void setAppManageCode(String appManageCode) {
		this.appManageCode = appManageCode;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Double getRepairCost() {
		return this.repairCost;
	}

	public void setRepairCost(Double repairCost) {
		this.repairCost = repairCost;
	}

	public Double getMaterialCost() {
		return this.materialCost;
	}

	public void setMaterialCost(Double materialCost) {
		this.materialCost = materialCost;
	}

}