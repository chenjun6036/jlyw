package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Deal entity. @author MyEclipse Persistence Tools
 */

public class Deal implements java.io.Serializable {

	// Fields

	private String dealCode;
	private SysUser sysUserByCreatorId;
	private SysUser sysUserBySignerId;
	private Customer customer;
	private String contactorName;
	private String contactorTel;
	private Date signDate;
	private Timestamp creatDate;
	private Date validity;
	private Integer status;
	private String remark;
	private String attachment;

	// Constructors

	/** default constructor */
	public Deal() {
	}

	/** minimal constructor */
	public Deal(String dealCode, SysUser sysUserByCreatorId, Customer customer,
			Timestamp creatDate, Date validity, Integer status) {
		this.dealCode = dealCode;
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.customer = customer;
		this.creatDate = creatDate;
		this.validity = validity;
		this.status = status;
	}

	/** full constructor */
	public Deal(String dealCode, SysUser sysUserByCreatorId,
			SysUser sysUserBySignerId, Customer customer, String contactorName,
			String contactorTel, Date signDate, Timestamp creatDate,
			Date validity, Integer status, String remark, String attachment) {
		this.dealCode = dealCode;
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.sysUserBySignerId = sysUserBySignerId;
		this.customer = customer;
		this.contactorName = contactorName;
		this.contactorTel = contactorTel;
		this.signDate = signDate;
		this.creatDate = creatDate;
		this.validity = validity;
		this.status = status;
		this.remark = remark;
		this.attachment = attachment;
	}

	// Property accessors

	public String getDealCode() {
		return this.dealCode;
	}

	public void setDealCode(String dealCode) {
		this.dealCode = dealCode;
	}

	public SysUser getSysUserByCreatorId() {
		return this.sysUserByCreatorId;
	}

	public void setSysUserByCreatorId(SysUser sysUserByCreatorId) {
		this.sysUserByCreatorId = sysUserByCreatorId;
	}

	public SysUser getSysUserBySignerId() {
		return this.sysUserBySignerId;
	}

	public void setSysUserBySignerId(SysUser sysUserBySignerId) {
		this.sysUserBySignerId = sysUserBySignerId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getContactorName() {
		return this.contactorName;
	}

	public void setContactorName(String contactorName) {
		this.contactorName = contactorName;
	}

	public String getContactorTel() {
		return this.contactorTel;
	}

	public void setContactorTel(String contactorTel) {
		this.contactorTel = contactorTel;
	}

	public Date getSignDate() {
		return this.signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public Timestamp getCreatDate() {
		return this.creatDate;
	}

	public void setCreatDate(Timestamp creatDate) {
		this.creatDate = creatDate;
	}

	public Date getValidity() {
		return this.validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}