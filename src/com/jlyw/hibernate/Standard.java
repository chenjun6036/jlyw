package com.jlyw.hibernate;

import java.sql.Date;

/**
 * Standard entity. @author MyEclipse Persistence Tools
 */

public class Standard implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String name;
	private String nameEn;
	private String brief;
	private String certificateCode;
	private String standardCode;
	private String projectCode;
	private Integer status;
	private String createdBy;
	private String issuedBy;
	private Date issueDate;
	private Date validDate;
	private String range;
	private String uncertain;
	private String sissuedBy;
	private Date sissueDate;
	private Date svalidDate;
	private String scertificateCode;
	private String sregion;
	private String sauthorizationCode;
	private String slocaleCode;
	private String copy;
	private String scopy;
	private String remark;
	private Integer warnSlot;
	private String projectType;

	// Constructors

	/** default constructor */
	public Standard() {
	}

	/** minimal constructor */
	public Standard(SysUser sysUser, String name, String nameEn, String brief,
			String certificateCode, String standardCode, Integer status,
			String createdBy, String issuedBy, Date issueDate, Date validDate,
			String range, String uncertain, String sissuedBy, Date sissueDate,
			Date svalidDate, String scertificateCode, String sregion,
			String sauthorizationCode, String slocaleCode, String copy,
			String scopy, String projectType) {
		this.sysUser = sysUser;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.certificateCode = certificateCode;
		this.standardCode = standardCode;
		this.status = status;
		this.createdBy = createdBy;
		this.issuedBy = issuedBy;
		this.issueDate = issueDate;
		this.validDate = validDate;
		this.range = range;
		this.uncertain = uncertain;
		this.sissuedBy = sissuedBy;
		this.sissueDate = sissueDate;
		this.svalidDate = svalidDate;
		this.scertificateCode = scertificateCode;
		this.sregion = sregion;
		this.sauthorizationCode = sauthorizationCode;
		this.slocaleCode = slocaleCode;
		this.copy = copy;
		this.scopy = scopy;
		this.projectType = projectType;
	}

	/** full constructor */
	public Standard(SysUser sysUser, String name, String nameEn, String brief,
			String certificateCode, String standardCode, String projectCode,
			Integer status, String createdBy, String issuedBy, Date issueDate,
			Date validDate, String range, String uncertain, String sissuedBy,
			Date sissueDate, Date svalidDate, String scertificateCode,
			String sregion, String sauthorizationCode, String slocaleCode,
			String copy, String scopy, String remark, Integer warnSlot,
			String projectType) {
		this.sysUser = sysUser;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.certificateCode = certificateCode;
		this.standardCode = standardCode;
		this.projectCode = projectCode;
		this.status = status;
		this.createdBy = createdBy;
		this.issuedBy = issuedBy;
		this.issueDate = issueDate;
		this.validDate = validDate;
		this.range = range;
		this.uncertain = uncertain;
		this.sissuedBy = sissuedBy;
		this.sissueDate = sissueDate;
		this.svalidDate = svalidDate;
		this.scertificateCode = scertificateCode;
		this.sregion = sregion;
		this.sauthorizationCode = sauthorizationCode;
		this.slocaleCode = slocaleCode;
		this.copy = copy;
		this.scopy = scopy;
		this.remark = remark;
		this.warnSlot = warnSlot;
		this.projectType = projectType;
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

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public String getStandardCode() {
		return this.standardCode;
	}

	public void setStandardCode(String standardCode) {
		this.standardCode = standardCode;
	}

	public String getProjectCode() {
		return this.projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getIssuedBy() {
		return this.issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getValidDate() {
		return this.validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
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

	public String getSissuedBy() {
		return this.sissuedBy;
	}

	public void setSissuedBy(String sissuedBy) {
		this.sissuedBy = sissuedBy;
	}

	public Date getSissueDate() {
		return this.sissueDate;
	}

	public void setSissueDate(Date sissueDate) {
		this.sissueDate = sissueDate;
	}

	public Date getSvalidDate() {
		return this.svalidDate;
	}

	public void setSvalidDate(Date svalidDate) {
		this.svalidDate = svalidDate;
	}

	public String getScertificateCode() {
		return this.scertificateCode;
	}

	public void setScertificateCode(String scertificateCode) {
		this.scertificateCode = scertificateCode;
	}

	public String getSregion() {
		return this.sregion;
	}

	public void setSregion(String sregion) {
		this.sregion = sregion;
	}

	public String getSauthorizationCode() {
		return this.sauthorizationCode;
	}

	public void setSauthorizationCode(String sauthorizationCode) {
		this.sauthorizationCode = sauthorizationCode;
	}

	public String getSlocaleCode() {
		return this.slocaleCode;
	}

	public void setSlocaleCode(String slocaleCode) {
		this.slocaleCode = slocaleCode;
	}

	public String getCopy() {
		return this.copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public String getScopy() {
		return this.scopy;
	}

	public void setScopy(String scopy) {
		this.scopy = scopy;
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

	public String getProjectType() {
		return this.projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

}