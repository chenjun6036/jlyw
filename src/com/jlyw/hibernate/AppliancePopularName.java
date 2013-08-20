package com.jlyw.hibernate;

/**
 * AppliancePopularName entity. @author MyEclipse Persistence Tools
 */

public class AppliancePopularName implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceStandardName applianceStandardName;
	private String popularName;
	private String brief;
	private Integer status;

	// Constructors

	/** default constructor */
	public AppliancePopularName() {
	}

	/** full constructor */
	public AppliancePopularName(ApplianceStandardName applianceStandardName,
			String popularName, String brief, Integer status) {
		this.applianceStandardName = applianceStandardName;
		this.popularName = popularName;
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

	public String getPopularName() {
		return this.popularName;
	}

	public void setPopularName(String popularName) {
		this.popularName = popularName;
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