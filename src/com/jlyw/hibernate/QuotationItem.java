package com.jlyw.hibernate;

/**
 * QuotationItem entity. @author MyEclipse Persistence Tools
 */

public class QuotationItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Quotation quotation;
	private ApplianceStandardName applianceStandardName;
	private String certificateName;
	private String standardName;
	private String model;
	private String range;
	private String accuracy;
	private Integer quantity;
	private String minCost;
	private String certType;
	private Boolean siteTest;
	private String remark;
	private String maxCost;
	private String appFactoryCode;
	private String appManageCode;
	private String manufacturer;
	private Integer xh;

	// Constructors

	/** default constructor */
	public QuotationItem() {
	}

	/** minimal constructor */
	public QuotationItem(Quotation quotation, String standardName,
			Boolean siteTest) {
		this.quotation = quotation;
		this.standardName = standardName;
		this.siteTest = siteTest;
	}

	/** full constructor */
	public QuotationItem(Quotation quotation,
			ApplianceStandardName applianceStandardName,
			String certificateName, String standardName, String model,
			String range, String accuracy, Integer quantity, String minCost,
			String certType, Boolean siteTest, String remark, String maxCost,
			String appFactoryCode, String appManageCode, String manufacturer,
			Integer xh) {
		this.quotation = quotation;
		this.applianceStandardName = applianceStandardName;
		this.certificateName = certificateName;
		this.standardName = standardName;
		this.model = model;
		this.range = range;
		this.accuracy = accuracy;
		this.quantity = quantity;
		this.minCost = minCost;
		this.certType = certType;
		this.siteTest = siteTest;
		this.remark = remark;
		this.maxCost = maxCost;
		this.appFactoryCode = appFactoryCode;
		this.appManageCode = appManageCode;
		this.manufacturer = manufacturer;
		this.xh = xh;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Quotation getQuotation() {
		return this.quotation;
	}

	public void setQuotation(Quotation quotation) {
		this.quotation = quotation;
	}

	public ApplianceStandardName getApplianceStandardName() {
		return this.applianceStandardName;
	}

	public void setApplianceStandardName(
			ApplianceStandardName applianceStandardName) {
		this.applianceStandardName = applianceStandardName;
	}

	public String getCertificateName() {
		return this.certificateName;
	}

	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}

	public String getStandardName() {
		return this.standardName;
	}

	public void setStandardName(String standardName) {
		this.standardName = standardName;
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

	public Integer getQuantity() {
		return this.quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getMinCost() {
		return this.minCost;
	}

	public void setMinCost(String minCost) {
		this.minCost = minCost;
	}

	public String getCertType() {
		return this.certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public Boolean getSiteTest() {
		return this.siteTest;
	}

	public void setSiteTest(Boolean siteTest) {
		this.siteTest = siteTest;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMaxCost() {
		return this.maxCost;
	}

	public void setMaxCost(String maxCost) {
		this.maxCost = maxCost;
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

	public Integer getXh() {
		return this.xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

}