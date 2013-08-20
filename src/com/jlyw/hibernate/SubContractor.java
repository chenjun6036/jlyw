package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * SubContractor entity. @author MyEclipse Persistence Tools
 */

public class SubContractor implements java.io.Serializable {

	// Fields

	private Integer id;
	private Region region;
	private SysUser sysUser;
	private Reason reason;
	private String name;
	private String brief;
	private String code;
	private String address;
	private String tel;
	private String zipCode;
	private String contactor;
	private String remark;
	private Integer status;
	private Timestamp cancelDate;
	private String copy;
	private Timestamp modifyDate;
	private String contactorTel;

	// Constructors

	/** default constructor */
	public SubContractor() {
	}

	/** minimal constructor */
	public SubContractor(Region region, SysUser sysUser, String name,
			String brief, String code, String address, String tel,
			String zipCode, String contactor, Integer status,
			Timestamp modifyDate) {
		this.region = region;
		this.sysUser = sysUser;
		this.name = name;
		this.brief = brief;
		this.code = code;
		this.address = address;
		this.tel = tel;
		this.zipCode = zipCode;
		this.contactor = contactor;
		this.status = status;
		this.modifyDate = modifyDate;
	}

	/** full constructor */
	public SubContractor(Region region, SysUser sysUser, Reason reason,
			String name, String brief, String code, String address, String tel,
			String zipCode, String contactor, String remark, Integer status,
			Timestamp cancelDate, String copy, Timestamp modifyDate,
			String contactorTel) {
		this.region = region;
		this.sysUser = sysUser;
		this.reason = reason;
		this.name = name;
		this.brief = brief;
		this.code = code;
		this.address = address;
		this.tel = tel;
		this.zipCode = zipCode;
		this.contactor = contactor;
		this.remark = remark;
		this.status = status;
		this.cancelDate = cancelDate;
		this.copy = copy;
		this.modifyDate = modifyDate;
		this.contactorTel = contactorTel;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Reason getReason() {
		return this.reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getContactor() {
		return this.contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
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

	public Timestamp getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getCopy() {
		return this.copy;
	}

	public void setCopy(String copy) {
		this.copy = copy;
	}

	public Timestamp getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getContactorTel() {
		return this.contactorTel;
	}

	public void setContactorTel(String contactorTel) {
		this.contactorTel = contactorTel;
	}

}