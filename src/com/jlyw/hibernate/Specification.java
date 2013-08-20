package com.jlyw.hibernate;

import java.sql.Date;

/**
 * Specification entity. @author MyEclipse Persistence Tools
 */

public class Specification implements java.io.Serializable {

	// Fields

	private Integer id;
	private String specificationCode;
	private String nameCn;
	private String nameEn;
	private String brief;
	private String type;
	private Boolean inCharge;
	private Integer status;
	private String localeCode;
	private Date issueDate;
	private Date effectiveDate;
	private Date repealDate;
	private String oldSpecification;
	private String copy;
	private String methodConfirm;
	private String remark;
	private Integer warnSlot;

	// Constructors

	/** default constructor */
	public Specification() {
	}

	/** minimal constructor */
	public Specification(String specificationCode, String nameCn, String brief,
			String type, Boolean inCharge, Integer status, Date issueDate,
			Date effectiveDate) {
		this.specificationCode = specificationCode;
		this.nameCn = nameCn;
		this.brief = brief;
		this.type = type;
		this.inCharge = inCharge;
		this.status = status;
		this.issueDate = issueDate;
		this.effectiveDate = effectiveDate;
	}

	/** full constructor */
	public Specification(String specificationCode, String nameCn,
			String nameEn, String brief, String type, Boolean inCharge,
			Integer status, String localeCode, Date issueDate,
			Date effectiveDate, Date repealDate, String oldSpecification,
			String copy, String methodConfirm, String remark, Integer warnSlot) {
		this.specificationCode = specificationCode;
		this.nameCn = nameCn;
		this.nameEn = nameEn;
		this.brief = brief;
		this.type = type;
		this.inCharge = inCharge;
		this.status = status;
		this.localeCode = localeCode;
		this.issueDate = issueDate;
		this.effectiveDate = effectiveDate;
		this.repealDate = repealDate;
		this.oldSpecification = oldSpecification;
		this.copy = copy;
		this.methodConfirm = methodConfirm;
		this.remark = remark;
		this.warnSlot = warnSlot;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSpecificationCode() {
		return this.specificationCode;
	}

	public void setSpecificationCode(String specificationCode) {
		this.specificationCode = specificationCode;
	}

	public String getNameCn() {
		return this.nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getInCharge() {
		return this.inCharge;
	}

	public void setInCharge(Boolean inCharge) {
		this.inCharge = inCharge;
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

	public Date getIssueDate() {
		return this.issueDate;
	}

	public void setIssueDate(Date issueDate) {
		this.issueDate = issueDate;
	}

	public Date getEffectiveDate() {
		return this.effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getRepealDate() {
		return this.repealDate;
	}

	public void setRepealDate(Date repealDate) {
		this.repealDate = repealDate;
	}

	public String getOldSpecification() {
		return this.oldSpecification;
	}

	public void setOldSpecification(String oldSpecification) {
		this.oldSpecification = oldSpecification;
	}

	public String getCopy() {
		return this.copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public String getMethodConfirm() {
		return this.methodConfirm;
	}

	public void setMethodConfirm(String methodConfirm) {
		this.methodConfirm = methodConfirm;
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

}