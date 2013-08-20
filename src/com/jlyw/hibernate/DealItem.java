package com.jlyw.hibernate;

/**
 * DealItem entity. @author MyEclipse Persistence Tools
 */

public class DealItem implements java.io.Serializable {

	// Fields

	private Integer id;
	private Deal deal;
	private ApplianceStandardName applianceStandardName;
	private String standardName;
	private String model;
	private String range;
	private String accuracy;
	private Double dealPrice;
	private String appFactoryCode;
	private String appManageCode;
	private String manufacturer;
	private String remark;
	private Integer xh;

	// Constructors

	/** default constructor */
	public DealItem() {
	}

	/** minimal constructor */
	public DealItem(Deal deal, String standardName, Double dealPrice) {
		this.deal = deal;
		this.standardName = standardName;
		this.dealPrice = dealPrice;
	}

	/** full constructor */
	public DealItem(Deal deal, ApplianceStandardName applianceStandardName,
			String standardName, String model, String range, String accuracy,
			Double dealPrice, String appFactoryCode, String appManageCode,
			String manufacturer, String remark, Integer xh) {
		this.deal = deal;
		this.applianceStandardName = applianceStandardName;
		this.standardName = standardName;
		this.model = model;
		this.range = range;
		this.accuracy = accuracy;
		this.dealPrice = dealPrice;
		this.appFactoryCode = appFactoryCode;
		this.appManageCode = appManageCode;
		this.manufacturer = manufacturer;
		this.remark = remark;
		this.xh = xh;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Deal getDeal() {
		return this.deal;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	public ApplianceStandardName getApplianceStandardName() {
		return this.applianceStandardName;
	}

	public void setApplianceStandardName(
			ApplianceStandardName applianceStandardName) {
		this.applianceStandardName = applianceStandardName;
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

	public Double getDealPrice() {
		return this.dealPrice;
	}

	public void setDealPrice(Double dealPrice) {
		this.dealPrice = dealPrice;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getXh() {
		return this.xh;
	}

	public void setXh(Integer xh) {
		this.xh = xh;
	}

}