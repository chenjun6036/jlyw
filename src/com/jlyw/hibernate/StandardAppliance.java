package com.jlyw.hibernate;

import java.sql.Date;

/**
 * StandardAppliance entity. @author MyEclipse Persistence Tools
 */

public class StandardAppliance implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String name;
	private String brief;
	private String model;
	private String range;
	private String uncertain;
	private String seriaNumber;
	private String manufacturer;
	private Date releaseDate;
	private String releaseNumber;
	private Integer num;
	private Integer testCycle;
	private Double price;
	private Integer status;
	private String localeCode;
	private String permanentCode;
	private String projectCode;
	private String budget;
	private String remark;
	private Integer warnSlot;
	private Integer inspectMonth;
	private Date validDate;

	// Constructors

	/** default constructor */
	public StandardAppliance() {
	}

	/** minimal constructor */
	public StandardAppliance(SysUser sysUser, String name, String brief,
			String model, String range, String uncertain, String seriaNumber,
			String manufacturer, String releaseNumber, Integer num,
			Integer testCycle, Double price, Integer status, String localeCode,
			String permanentCode, String projectCode, String budget) {
		this.sysUser = sysUser;
		this.name = name;
		this.brief = brief;
		this.model = model;
		this.range = range;
		this.uncertain = uncertain;
		this.seriaNumber = seriaNumber;
		this.manufacturer = manufacturer;
		this.releaseNumber = releaseNumber;
		this.num = num;
		this.testCycle = testCycle;
		this.price = price;
		this.status = status;
		this.localeCode = localeCode;
		this.permanentCode = permanentCode;
		this.projectCode = projectCode;
		this.budget = budget;
	}

	/** full constructor */
	public StandardAppliance(SysUser sysUser, String name, String brief,
			String model, String range, String uncertain, String seriaNumber,
			String manufacturer, Date releaseDate, String releaseNumber,
			Integer num, Integer testCycle, Double price, Integer status,
			String localeCode, String permanentCode, String projectCode,
			String budget, String remark, Integer warnSlot,
			Integer inspectMonth, Date validDate) {
		this.sysUser = sysUser;
		this.name = name;
		this.brief = brief;
		this.model = model;
		this.range = range;
		this.uncertain = uncertain;
		this.seriaNumber = seriaNumber;
		this.manufacturer = manufacturer;
		this.releaseDate = releaseDate;
		this.releaseNumber = releaseNumber;
		this.num = num;
		this.testCycle = testCycle;
		this.price = price;
		this.status = status;
		this.localeCode = localeCode;
		this.permanentCode = permanentCode;
		this.projectCode = projectCode;
		this.budget = budget;
		this.remark = remark;
		this.warnSlot = warnSlot;
		this.inspectMonth = inspectMonth;
		this.validDate = validDate;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
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

	public String getUncertain() {
		return this.uncertain;
	}

	public void setUncertain(String uncertain) {
		this.uncertain = uncertain;
	}

	public String getSeriaNumber() {
		return this.seriaNumber;
	}

	public void setSeriaNumber(String seriaNumber) {
		this.seriaNumber = seriaNumber;
	}

	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public Date getReleaseDate() {
		return this.releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getReleaseNumber() {
		return this.releaseNumber;
	}

	public void setReleaseNumber(String releaseNumber) {
		this.releaseNumber = releaseNumber;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getTestCycle() {
		return this.testCycle;
	}

	public void setTestCycle(Integer testCycle) {
		this.testCycle = testCycle;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLocaleCode() {
		return this.localeCode;
	}

	public void setLocaleCode(String localeCode) {
		this.localeCode = localeCode;
	}

	public String getPermanentCode() {
		return this.permanentCode;
	}

	public void setPermanentCode(String permanentCode) {
		this.permanentCode = permanentCode;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getBudget() {
		return this.budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getWarnSlot() {
		return this.warnSlot;
	}

	public void setWarnSlot(Integer warnSlot) {
		this.warnSlot = warnSlot;
	}

	public Integer getInspectMonth() {
		return this.inspectMonth;
	}

	public void setInspectMonth(Integer inspectMonth) {
		this.inspectMonth = inspectMonth;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

}