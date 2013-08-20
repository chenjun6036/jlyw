package com.jlyw.hibernate;

/**
 * ApplianceManufacturer entity. @author MyEclipse Persistence Tools
 */

public class ApplianceManufacturer implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceStandardName applianceStandardName;
	private String manufacturer;
	private String brief;
	private Integer status;

	// Constructors

	/** default constructor */
	public ApplianceManufacturer() {
	}

	/** full constructor */
	public ApplianceManufacturer(ApplianceStandardName applianceStandardName,
			String manufacturer, String brief, Integer status) {
		this.applianceStandardName = applianceStandardName;
		this.manufacturer = manufacturer;
		this.brief = brief;
		this.status = status;
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

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}