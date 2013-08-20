package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * OriginalRecord entity. @author MyEclipse Persistence Tools
 */

public class OriginalRecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByCreatorId;
	private CommissionSheet commissionSheet;
	private Certificate certificate;
	private VerifyAndAuthorize verifyAndAuthorize;
	private TaskAssign taskAssign;
	private TargetAppliance targetAppliance;
	private SysUser sysUserByStaffId;
	private OriginalRecordExcel originalRecordExcel;
	private String model;
	private String range;
	private String manufacturer;
	private String applianceCode;
	private String accuracy;
	private String workType;
	private String workLocation;
	private String temp;
	private String humidity;
	private String pressure;
	private String conclusion;
	private Date workDate;
	private Date validity;
	private Double testFee;
	private Double repairFee;
	private Double materialFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;
	private String remark;
	private String manageCode;
	private Integer status;
	private Timestamp createTime;
	private String repairLevel;
	private String repairContent;
	private String materialDetail;
	private String mandatory;
	private String mandatoryCode;
	private String mandatoryItemType;
	private String mandatoryType;
	private String mandatoryPurpose;
	private String mandatoryApplyPlace;
	private Integer quantity;
	private String applianceName;
	private String otherCond;
	private String stdOrStdAppUsageStatus;
	private String abnormalDesc;
	private String firstIsUnqualified;
	private String unqualifiedReason;

	// Constructors

	/** default constructor */
	public OriginalRecord() {
	}

	/** minimal constructor */
	public OriginalRecord(SysUser sysUserByCreatorId,
			CommissionSheet commissionSheet, TaskAssign taskAssign,
			TargetAppliance targetAppliance, String model, String range,
			String manufacturer, String applianceCode, String accuracy,
			Integer status, Timestamp createTime) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.commissionSheet = commissionSheet;
		this.taskAssign = taskAssign;
		this.targetAppliance = targetAppliance;
		this.model = model;
		this.range = range;
		this.manufacturer = manufacturer;
		this.applianceCode = applianceCode;
		this.accuracy = accuracy;
		this.status = status;
		this.createTime = createTime;
	}

	/** full constructor */
	public OriginalRecord(SysUser sysUserByCreatorId,
			CommissionSheet commissionSheet, Certificate certificate,
			VerifyAndAuthorize verifyAndAuthorize, TaskAssign taskAssign,
			TargetAppliance targetAppliance, SysUser sysUserByStaffId,
			OriginalRecordExcel originalRecordExcel, String model,
			String range, String manufacturer, String applianceCode,
			String accuracy, String workType, String workLocation, String temp,
			String humidity, String pressure, String conclusion, Date workDate,
			Date validity, Double testFee, Double repairFee,
			Double materialFee, Double carFee, Double debugFee,
			Double otherFee, Double totalFee, String remark, String manageCode,
			Integer status, Timestamp createTime, String repairLevel,
			String repairContent, String materialDetail, String mandatory,
			String mandatoryCode, String mandatoryItemType,
			String mandatoryType, String mandatoryPurpose,
			String mandatoryApplyPlace, Integer quantity, String applianceName,
			String otherCond, String stdOrStdAppUsageStatus,
			String abnormalDesc, String firstIsUnqualified,
			String unqualifiedReason) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.commissionSheet = commissionSheet;
		this.certificate = certificate;
		this.verifyAndAuthorize = verifyAndAuthorize;
		this.taskAssign = taskAssign;
		this.targetAppliance = targetAppliance;
		this.sysUserByStaffId = sysUserByStaffId;
		this.originalRecordExcel = originalRecordExcel;
		this.model = model;
		this.range = range;
		this.manufacturer = manufacturer;
		this.applianceCode = applianceCode;
		this.accuracy = accuracy;
		this.workType = workType;
		this.workLocation = workLocation;
		this.temp = temp;
		this.humidity = humidity;
		this.pressure = pressure;
		this.conclusion = conclusion;
		this.workDate = workDate;
		this.validity = validity;
		this.testFee = testFee;
		this.repairFee = repairFee;
		this.materialFee = materialFee;
		this.carFee = carFee;
		this.debugFee = debugFee;
		this.otherFee = otherFee;
		this.totalFee = totalFee;
		this.remark = remark;
		this.manageCode = manageCode;
		this.status = status;
		this.createTime = createTime;
		this.repairLevel = repairLevel;
		this.repairContent = repairContent;
		this.materialDetail = materialDetail;
		this.mandatory = mandatory;
		this.mandatoryCode = mandatoryCode;
		this.mandatoryItemType = mandatoryItemType;
		this.mandatoryType = mandatoryType;
		this.mandatoryPurpose = mandatoryPurpose;
		this.mandatoryApplyPlace = mandatoryApplyPlace;
		this.quantity = quantity;
		this.applianceName = applianceName;
		this.otherCond = otherCond;
		this.stdOrStdAppUsageStatus = stdOrStdAppUsageStatus;
		this.abnormalDesc = abnormalDesc;
		this.firstIsUnqualified = firstIsUnqualified;
		this.unqualifiedReason = unqualifiedReason;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByCreatorId() {
		return this.sysUserByCreatorId;
	}

	public void setSysUserByCreatorId(SysUser sysUserByCreatorId) {
		this.sysUserByCreatorId = sysUserByCreatorId;
	}

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
	}

	public Certificate getCertificate() {
		return this.certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public VerifyAndAuthorize getVerifyAndAuthorize() {
		return this.verifyAndAuthorize;
	}

	public void setVerifyAndAuthorize(VerifyAndAuthorize verifyAndAuthorize) {
		this.verifyAndAuthorize = verifyAndAuthorize;
	}

	public TaskAssign getTaskAssign() {
		return this.taskAssign;
	}

	public void setTaskAssign(TaskAssign taskAssign) {
		this.taskAssign = taskAssign;
	}

	public TargetAppliance getTargetAppliance() {
		return this.targetAppliance;
	}

	public void setTargetAppliance(TargetAppliance targetAppliance) {
		this.targetAppliance = targetAppliance;
	}

	public SysUser getSysUserByStaffId() {
		return this.sysUserByStaffId;
	}

	public void setSysUserByStaffId(SysUser sysUserByStaffId) {
		this.sysUserByStaffId = sysUserByStaffId;
	}

	public OriginalRecordExcel getOriginalRecordExcel() {
		return this.originalRecordExcel;
	}

	public void setOriginalRecordExcel(OriginalRecordExcel originalRecordExcel) {
		this.originalRecordExcel = originalRecordExcel;
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

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getApplianceCode() {
		return this.applianceCode;
	}

	public void setApplianceCode(String applianceCode) {
		this.applianceCode = applianceCode;
	}

	public String getAccuracy() {
		return this.accuracy;
	}

	public void setAccuracy(String accuracy) {
		this.accuracy = accuracy;
	}

	public String getWorkType() {
		return this.workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getWorkLocation() {
		return this.workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getTemp() {
		return this.temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getHumidity() {
		return this.humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getPressure() {
		return this.pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getConclusion() {
		return this.conclusion;
	}

	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
	}

	public Date getWorkDate() {
		return this.workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Date getValidity() {
		return this.validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Double getTestFee() {
		return this.testFee;
	}

	public void setTestFee(Double testFee) {
		this.testFee = testFee;
	}

	public Double getRepairFee() {
		return this.repairFee;
	}

	public void setRepairFee(Double repairFee) {
		this.repairFee = repairFee;
	}

	public Double getMaterialFee() {
		return this.materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public Double getCarFee() {
		return this.carFee;
	}

	public void setCarFee(Double carFee) {
		this.carFee = carFee;
	}

	public Double getDebugFee() {
		return this.debugFee;
	}

	public void setDebugFee(Double debugFee) {
		this.debugFee = debugFee;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getManageCode() {
		return this.manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getRepairLevel() {
		return this.repairLevel;
	}

	public void setRepairLevel(String repairLevel) {
		this.repairLevel = repairLevel;
	}

	public String getRepairContent() {
		return this.repairContent;
	}

	public void setRepairContent(String repairContent) {
		this.repairContent = repairContent;
	}

	public String getMaterialDetail() {
		return this.materialDetail;
	}

	public void setMaterialDetail(String materialDetail) {
		this.materialDetail = materialDetail;
	}

	public String getMandatory() {
		return this.mandatory;
	}

	public void setMandatory(String mandatory) {
		this.mandatory = mandatory;
	}

	public String getMandatoryCode() {
		return this.mandatoryCode;
	}

	public void setMandatoryCode(String mandatoryCode) {
		this.mandatoryCode = mandatoryCode;
	}

	public String getMandatoryItemType() {
		return this.mandatoryItemType;
	}

	public void setMandatoryItemType(String mandatoryItemType) {
		this.mandatoryItemType = mandatoryItemType;
	}

	public String getMandatoryType() {
		return this.mandatoryType;
	}

	public void setMandatoryType(String mandatoryType) {
		this.mandatoryType = mandatoryType;
	}

	public String getMandatoryPurpose() {
		return this.mandatoryPurpose;
	}

	public void setMandatoryPurpose(String mandatoryPurpose) {
		this.mandatoryPurpose = mandatoryPurpose;
	}

	public String getMandatoryApplyPlace() {
		return this.mandatoryApplyPlace;
	}

	public void setMandatoryApplyPlace(String mandatoryApplyPlace) {
		this.mandatoryApplyPlace = mandatoryApplyPlace;
	}

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getApplianceName() {
		return this.applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	public String getOtherCond() {
		return this.otherCond;
	}

	public void setOtherCond(String otherCond) {
		this.otherCond = otherCond;
	}

	public String getStdOrStdAppUsageStatus() {
		return this.stdOrStdAppUsageStatus;
	}

	public void setStdOrStdAppUsageStatus(String stdOrStdAppUsageStatus) {
		this.stdOrStdAppUsageStatus = stdOrStdAppUsageStatus;
	}

	public String getAbnormalDesc() {
		return this.abnormalDesc;
	}

	public void setAbnormalDesc(String abnormalDesc) {
		this.abnormalDesc = abnormalDesc;
	}

	public String getFirstIsUnqualified() {
		return this.firstIsUnqualified;
	}

	public void setFirstIsUnqualified(String firstIsUnqualified) {
		this.firstIsUnqualified = firstIsUnqualified;
	}

	public String getUnqualifiedReason() {
		return this.unqualifiedReason;
	}

	public void setUnqualifiedReason(String unqualifiedReason) {
		this.unqualifiedReason = unqualifiedReason;
	}

}