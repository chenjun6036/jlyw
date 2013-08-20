package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * CommissionSheet entity. @author MyEclipse Persistence Tools
 */

public class CommissionSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private String code;
	private String pwd;
	private Timestamp commissionDate;
	private Integer customerId;
	private String customerName;
	private String customerTel;
	private String customerAddress;
	private String customerZipCode;
	private String customerContactor;
	private String customerContactorTel;
	private String sampleFrom;
	private String billingTo;
	private Boolean speciesType;
	private Integer applianceSpeciesId;
	private String applianceName;
	private String appFactoryCode;
	private String appManageCode;
	private String manufacturer;
	private Integer quantity;
	private Boolean mandatory;
	private String mandatoryCode;
	private String appearance;
	private Integer reportType;
	private String otherRequirements;
	private Boolean urgent;
	private Boolean repair;
	private Boolean subcontract;
	private String location;
	private String allotee;
	private Integer commissionType;
	private Integer receiverId;
	private Integer sampleAddress;
	private Integer creatorId;
	private Timestamp createDate;
	private Integer reportAddress;
	private String quotationId;
	private Date promiseDate;
	private Double testFee;
	private Double repairFee;
	private Double materialFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;
	private Integer status;
	private String localeCommissionCode;
	private Timestamp localeCommissionDate;
	private Integer localeStaffId;
	private String finishLocation;
	private Integer finishStaffId;
	private Timestamp finishDate;
	private Integer cancelRequesterId;
	private Integer cancelExecuterId;
	private Timestamp cancelDate;
	private Integer cancelReason;
	private String invoiceCode;
	private String remark;
	private String applianceModel;
	private String customerHandler;
	private String range;
	private String accuracy;
	private String receiverName;
	private Integer headNameId;
	private String headName;
	private String headNameEn;
	private Boolean isFromOldSystem;
	private Integer checkOutStaffId;
	private Timestamp checkOutDate;
	private String detailListCode;
	private String attachment;
	private Integer localeApplianceItemId;

	// Constructors

	/** default constructor */
	public CommissionSheet() {
	}

	/** minimal constructor */
	public CommissionSheet(String code, String pwd, Timestamp commissionDate,
			Integer customerId, String customerName, String customerTel,
			String customerAddress, String customerZipCode,
			String customerContactor, String customerContactorTel,
			Boolean speciesType, Integer applianceSpeciesId, Integer quantity,
			Boolean mandatory, String appearance, Integer reportType,
			Boolean urgent, Boolean repair, Boolean subcontract,
			Integer commissionType, Integer creatorId, Integer status,
			Integer headNameId, String headName, String headNameEn,
			String attachment) {
		this.code = code;
		this.pwd = pwd;
		this.commissionDate = commissionDate;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerTel = customerTel;
		this.customerAddress = customerAddress;
		this.customerZipCode = customerZipCode;
		this.customerContactor = customerContactor;
		this.customerContactorTel = customerContactorTel;
		this.speciesType = speciesType;
		this.applianceSpeciesId = applianceSpeciesId;
		this.quantity = quantity;
		this.mandatory = mandatory;
		this.appearance = appearance;
		this.reportType = reportType;
		this.urgent = urgent;
		this.repair = repair;
		this.subcontract = subcontract;
		this.commissionType = commissionType;
		this.creatorId = creatorId;
		this.status = status;
		this.headNameId = headNameId;
		this.headName = headName;
		this.headNameEn = headNameEn;
		this.attachment = attachment;
	}

	/** full constructor */
	public CommissionSheet(String code, String pwd, Timestamp commissionDate,
			Integer customerId, String customerName, String customerTel,
			String customerAddress, String customerZipCode,
			String customerContactor, String customerContactorTel,
			String sampleFrom, String billingTo, Boolean speciesType,
			Integer applianceSpeciesId, String applianceName,
			String appFactoryCode, String appManageCode, String manufacturer,
			Integer quantity, Boolean mandatory, String mandatoryCode,
			String appearance, Integer reportType, String otherRequirements,
			Boolean urgent, Boolean repair, Boolean subcontract,
			String location, String allotee, Integer commissionType,
			Integer receiverId, Integer sampleAddress, Integer creatorId,
			Timestamp createDate, Integer reportAddress, String quotationId,
			Date promiseDate, Double testFee, Double repairFee,
			Double materialFee, Double carFee, Double debugFee,
			Double otherFee, Double totalFee, Integer status,
			String localeCommissionCode, Timestamp localeCommissionDate,
			Integer localeStaffId, String finishLocation,
			Integer finishStaffId, Timestamp finishDate,
			Integer cancelRequesterId, Integer cancelExecuterId,
			Timestamp cancelDate, Integer cancelReason, String invoiceCode,
			String remark, String applianceModel, String customerHandler,
			String range, String accuracy, String receiverName,
			Integer headNameId, String headName, String headNameEn,
			Boolean isFromOldSystem, Integer checkOutStaffId,
			Timestamp checkOutDate, String detailListCode, String attachment,
			Integer localeApplianceItemId) {
		this.code = code;
		this.pwd = pwd;
		this.commissionDate = commissionDate;
		this.customerId = customerId;
		this.customerName = customerName;
		this.customerTel = customerTel;
		this.customerAddress = customerAddress;
		this.customerZipCode = customerZipCode;
		this.customerContactor = customerContactor;
		this.customerContactorTel = customerContactorTel;
		this.sampleFrom = sampleFrom;
		this.billingTo = billingTo;
		this.speciesType = speciesType;
		this.applianceSpeciesId = applianceSpeciesId;
		this.applianceName = applianceName;
		this.appFactoryCode = appFactoryCode;
		this.appManageCode = appManageCode;
		this.manufacturer = manufacturer;
		this.quantity = quantity;
		this.mandatory = mandatory;
		this.mandatoryCode = mandatoryCode;
		this.appearance = appearance;
		this.reportType = reportType;
		this.otherRequirements = otherRequirements;
		this.urgent = urgent;
		this.repair = repair;
		this.subcontract = subcontract;
		this.location = location;
		this.allotee = allotee;
		this.commissionType = commissionType;
		this.receiverId = receiverId;
		this.sampleAddress = sampleAddress;
		this.creatorId = creatorId;
		this.createDate = createDate;
		this.reportAddress = reportAddress;
		this.quotationId = quotationId;
		this.promiseDate = promiseDate;
		this.testFee = testFee;
		this.repairFee = repairFee;
		this.materialFee = materialFee;
		this.carFee = carFee;
		this.debugFee = debugFee;
		this.otherFee = otherFee;
		this.totalFee = totalFee;
		this.status = status;
		this.localeCommissionCode = localeCommissionCode;
		this.localeCommissionDate = localeCommissionDate;
		this.localeStaffId = localeStaffId;
		this.finishLocation = finishLocation;
		this.finishStaffId = finishStaffId;
		this.finishDate = finishDate;
		this.cancelRequesterId = cancelRequesterId;
		this.cancelExecuterId = cancelExecuterId;
		this.cancelDate = cancelDate;
		this.cancelReason = cancelReason;
		this.invoiceCode = invoiceCode;
		this.remark = remark;
		this.applianceModel = applianceModel;
		this.customerHandler = customerHandler;
		this.range = range;
		this.accuracy = accuracy;
		this.receiverName = receiverName;
		this.headNameId = headNameId;
		this.headName = headName;
		this.headNameEn = headNameEn;
		this.isFromOldSystem = isFromOldSystem;
		this.checkOutStaffId = checkOutStaffId;
		this.checkOutDate = checkOutDate;
		this.detailListCode = detailListCode;
		this.attachment = attachment;
		this.localeApplianceItemId = localeApplianceItemId;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public Timestamp getCommissionDate() {
		return this.commissionDate;
	}

	public void setCommissionDate(Timestamp commissionDate) {
		this.commissionDate = commissionDate;
	}

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerTel() {
		return this.customerTel;
	}

	public void setCustomerTel(String customerTel) {
		this.customerTel = customerTel;
	}

	public String getCustomerAddress() {
		return this.customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getCustomerZipCode() {
		return this.customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}

	public String getCustomerContactor() {
		return this.customerContactor;
	}

	public void setCustomerContactor(String customerContactor) {
		this.customerContactor = customerContactor;
	}

	public String getCustomerContactorTel() {
		return this.customerContactorTel;
	}

	public void setCustomerContactorTel(String customerContactorTel) {
		this.customerContactorTel = customerContactorTel;
	}

	public String getSampleFrom() {
		return this.sampleFrom;
	}

	public void setSampleFrom(String sampleFrom) {
		this.sampleFrom = sampleFrom;
	}

	public String getBillingTo() {
		return this.billingTo;
	}

	public void setBillingTo(String billingTo) {
		this.billingTo = billingTo;
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

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Boolean getMandatory() {
		return this.mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getMandatoryCode() {
		return this.mandatoryCode;
	}

	public void setMandatoryCode(String mandatoryCode) {
		this.mandatoryCode = mandatoryCode;
	}

	public String getAppearance() {
		return this.appearance;
	}

	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}

	public Integer getReportType() {
		return this.reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public String getOtherRequirements() {
		return this.otherRequirements;
	}

	public void setOtherRequirements(String otherRequirements) {
		this.otherRequirements = otherRequirements;
	}

	public Boolean getUrgent() {
		return this.urgent;
	}

	public void setUrgent(Boolean urgent) {
		this.urgent = urgent;
	}

	public Boolean getRepair() {
		return this.repair;
	}

	public void setRepair(Boolean repair) {
		this.repair = repair;
	}

	public Boolean getSubcontract() {
		return this.subcontract;
	}

	public void setSubcontract(Boolean subcontract) {
		this.subcontract = subcontract;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAllotee() {
		return this.allotee;
	}

	public void setAllotee(String allotee) {
		this.allotee = allotee;
	}

	public Integer getCommissionType() {
		return this.commissionType;
	}

	public void setCommissionType(Integer commissionType) {
		this.commissionType = commissionType;
	}

	public Integer getReceiverId() {
		return this.receiverId;
	}

	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}

	public Integer getSampleAddress() {
		return this.sampleAddress;
	}

	public void setSampleAddress(Integer sampleAddress) {
		this.sampleAddress = sampleAddress;
	}

	public Integer getCreatorId() {
		return this.creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Integer getReportAddress() {
		return this.reportAddress;
	}

	public void setReportAddress(Integer reportAddress) {
		this.reportAddress = reportAddress;
	}

	public String getQuotationId() {
		return this.quotationId;
	}

	public void setQuotationId(String quotationId) {
		this.quotationId = quotationId;
	}

	public Date getPromiseDate() {
		return this.promiseDate;
	}

	public void setPromiseDate(Date promiseDate) {
		this.promiseDate = promiseDate;
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

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocaleCommissionCode() {
		return this.localeCommissionCode;
	}

	public void setLocaleCommissionCode(String localeCommissionCode) {
		this.localeCommissionCode = localeCommissionCode;
	}

	public Timestamp getLocaleCommissionDate() {
		return this.localeCommissionDate;
	}

	public void setLocaleCommissionDate(Timestamp localeCommissionDate) {
		this.localeCommissionDate = localeCommissionDate;
	}

	public Integer getLocaleStaffId() {
		return this.localeStaffId;
	}

	public void setLocaleStaffId(Integer localeStaffId) {
		this.localeStaffId = localeStaffId;
	}

	public String getFinishLocation() {
		return this.finishLocation;
	}

	public void setFinishLocation(String finishLocation) {
		this.finishLocation = finishLocation;
	}

	public Integer getFinishStaffId() {
		return this.finishStaffId;
	}

	public void setFinishStaffId(Integer finishStaffId) {
		this.finishStaffId = finishStaffId;
	}

	public Timestamp getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(Timestamp finishDate) {
		this.finishDate = finishDate;
	}

	public Integer getCancelRequesterId() {
		return this.cancelRequesterId;
	}

	public void setCancelRequesterId(Integer cancelRequesterId) {
		this.cancelRequesterId = cancelRequesterId;
	}

	public Integer getCancelExecuterId() {
		return this.cancelExecuterId;
	}

	public void setCancelExecuterId(Integer cancelExecuterId) {
		this.cancelExecuterId = cancelExecuterId;
	}

	public Timestamp getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Integer getCancelReason() {
		return this.cancelReason;
	}

	public void setCancelReason(Integer cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getInvoiceCode() {
		return this.invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getApplianceModel() {
		return this.applianceModel;
	}

	public void setApplianceModel(String applianceModel) {
		this.applianceModel = applianceModel;
	}

	public String getCustomerHandler() {
		return this.customerHandler;
	}

	public void setCustomerHandler(String customerHandler) {
		this.customerHandler = customerHandler;
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

	public String getReceiverName() {
		return this.receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public Integer getHeadNameId() {
		return this.headNameId;
	}

	public void setHeadNameId(Integer headNameId) {
		this.headNameId = headNameId;
	}

	public String getHeadName() {
		return this.headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getHeadNameEn() {
		return this.headNameEn;
	}

	public void setHeadNameEn(String headNameEn) {
		this.headNameEn = headNameEn;
	}

	public Boolean getIsFromOldSystem() {
		return this.isFromOldSystem;
	}

	public void setIsFromOldSystem(Boolean isFromOldSystem) {
		this.isFromOldSystem = isFromOldSystem;
	}

	public Integer getCheckOutStaffId() {
		return this.checkOutStaffId;
	}

	public void setCheckOutStaffId(Integer checkOutStaffId) {
		this.checkOutStaffId = checkOutStaffId;
	}

	public Timestamp getCheckOutDate() {
		return this.checkOutDate;
	}

	public void setCheckOutDate(Timestamp checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public String getDetailListCode() {
		return this.detailListCode;
	}

	public void setDetailListCode(String detailListCode) {
		this.detailListCode = detailListCode;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Integer getLocaleApplianceItemId() {
		return this.localeApplianceItemId;
	}

	public void setLocaleApplianceItemId(Integer localeApplianceItemId) {
		this.localeApplianceItemId = localeApplianceItemId;
	}

}