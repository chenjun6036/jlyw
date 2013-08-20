package com.jlyw.hibernate.view;

import java.sql.Date;

/**
 * ViewQjtz entity. @author MyEclipse Persistence Tools
 */

public class ViewQjtz implements java.io.Serializable {

	// Fields

	private ViewQjtzId id;
	private String customerName;
	private String customerZipCode;
	private String customerAddress;
	private String applianceName;
	private String mandatoryCode;
	private String applianceModel;
	private Integer testCycle;
	private String remark;
	private Date workDate;
	private Date mandatoryDate;

	// Constructors

	/** default constructor */
	public ViewQjtz() {
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerZipCode() {
		return customerZipCode;
	}

	public void setCustomerZipCode(String customerZipCode) {
		this.customerZipCode = customerZipCode;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}

	public String getMandatoryCode() {
		return mandatoryCode;
	}

	public void setMandatoryCode(String mandatoryCode) {
		this.mandatoryCode = mandatoryCode;
	}

	public String getApplianceModel() {
		return applianceModel;
	}

	public void setApplianceModel(String applianceModel) {
		this.applianceModel = applianceModel;
	}

	public Integer getTestCycle() {
		return testCycle;
	}

	public void setTestCycle(Integer testCycle) {
		this.testCycle = testCycle;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getWorkDate() {
		return workDate;
	}

	public void setWorkDate(Date workDate) {
		this.workDate = workDate;
	}

	public Date getMandatoryDate() {
		return mandatoryDate;
	}

	public void setMandatoryDate(Date mandatoryDate) {
		this.mandatoryDate = mandatoryDate;
	}

	public ViewQjtz(ViewQjtzId id, String customerName, String customerZipCode,
			String customerAddress, String applianceName, String mandatoryCode,
			String applianceModel, Integer testCycle, String remark,
			Date workDate, Date mandatoryDate) {
		super();
		this.id = id;
		this.customerName = customerName;
		this.customerZipCode = customerZipCode;
		this.customerAddress = customerAddress;
		this.applianceName = applianceName;
		this.mandatoryCode = mandatoryCode;
		this.applianceModel = applianceModel;
		this.testCycle = testCycle;
		this.remark = remark;
		this.workDate = workDate;
		this.mandatoryDate = mandatoryDate;
	}

	/** full constructor */
	public ViewQjtz(ViewQjtzId id) {
		this.id = id;
	}

	// Property accessors

	public ViewQjtzId getId() {
		return this.id;
	}

	public void setId(ViewQjtzId id) {
		this.id = id;
	}

}