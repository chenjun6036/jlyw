package com.jlyw.hibernate;

import java.sql.Date;

/**
 * TestLog entity. @author MyEclipse Persistence Tools
 */

public class TestLog implements java.io.Serializable {

	// Fields

	private Integer id;
	private StandardAppliance standardAppliance;
	private SysUser sysUser;
	private Date testDate;
	private Date validDate;
	private String tester;
	private String certificateId;
	private String confirmMeasure;
	private Date confirmDate;
	private String certificateCopy;
	private String durationCheck;
	private String maintenance;
	private String remark;
	private Integer status;

	// Constructors

	/** default constructor */
	public TestLog() {
	}

	/** minimal constructor */
	public TestLog(StandardAppliance standardAppliance, Date testDate,
			Date validDate, String tester, String certificateId, Integer status) {
		this.standardAppliance = standardAppliance;
		this.testDate = testDate;
		this.validDate = validDate;
		this.tester = tester;
		this.certificateId = certificateId;
		this.status = status;
	}

	/** full constructor */
	public TestLog(StandardAppliance standardAppliance, SysUser sysUser,
			Date testDate, Date validDate, String tester, String certificateId,
			String confirmMeasure, Date confirmDate, String certificateCopy,
			String durationCheck, String maintenance, String remark,
			Integer status) {
		this.standardAppliance = standardAppliance;
		this.sysUser = sysUser;
		this.testDate = testDate;
		this.validDate = validDate;
		this.tester = tester;
		this.certificateId = certificateId;
		this.confirmMeasure = confirmMeasure;
		this.confirmDate = confirmDate;
		this.certificateCopy = certificateCopy;
		this.durationCheck = durationCheck;
		this.maintenance = maintenance;
		this.remark = remark;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public StandardAppliance getStandardAppliance() {
		return this.standardAppliance;
	}

	public void setStandardAppliance(StandardAppliance standardAppliance) {
		this.standardAppliance = standardAppliance;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Date getTestDate() {
		return this.testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public String getTester() {
		return this.tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getCertificateId() {
		return this.certificateId;
	}

	public void setCertificateId(String certificateId) {
		this.certificateId = certificateId;
	}

	public String getConfirmMeasure() {
		return this.confirmMeasure;
	}

	public void setConfirmMeasure(String confirmMeasure) {
		this.confirmMeasure = confirmMeasure;
	}

	public Date getConfirmDate() {
		return this.confirmDate;
	}

	public void setConfirmDate(Date confirmDate) {
		this.confirmDate = confirmDate;
	}

	public String getCertificateCopy() {
		return this.certificateCopy;
	}

	public void setCertificateCopy(String certificateCopy) {
		this.certificateCopy = certificateCopy;
	}

	public String getDurationCheck() {
		return this.durationCheck;
	}

	public void setDurationCheck(String durationCheck) {
		this.durationCheck = durationCheck;
	}

	public String getMaintenance() {
		return this.maintenance;
	}

	public void setMaintenance(String maintenance) {
		this.maintenance = maintenance;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}