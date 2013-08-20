package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * DetailList entity. @author MyEclipse Persistence Tools
 */

public class DetailList implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String invoiceCode;
	private String code;
	private Double totalFee;
	private Double paidFee;
	private Timestamp lastEditTime;
	private String remark;
	private Integer status;
	private Integer version;
	private Double cashPaid;
	private Double chequePaid;
	private Double accountPaid;

	// Constructors

	/** default constructor */
	public DetailList() {
	}

	/** minimal constructor */
	public DetailList(SysUser sysUser, String code, Double totalFee,
			Timestamp lastEditTime, Integer status, Integer version) {
		this.sysUser = sysUser;
		this.code = code;
		this.totalFee = totalFee;
		this.lastEditTime = lastEditTime;
		this.status = status;
		this.version = version;
	}

	/** full constructor */
	public DetailList(SysUser sysUser, String invoiceCode, String code,
			Double totalFee, Double paidFee, Timestamp lastEditTime,
			String remark, Integer status, Integer version, Double cashPaid,
			Double chequePaid, Double accountPaid) {
		this.sysUser = sysUser;
		this.invoiceCode = invoiceCode;
		this.code = code;
		this.totalFee = totalFee;
		this.paidFee = paidFee;
		this.lastEditTime = lastEditTime;
		this.remark = remark;
		this.status = status;
		this.version = version;
		this.cashPaid = cashPaid;
		this.chequePaid = chequePaid;
		this.accountPaid = accountPaid;
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

	public String getInvoiceCode() {
		return this.invoiceCode;
	}

	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Double getPaidFee() {
		return this.paidFee;
	}

	public void setPaidFee(Double paidFee) {
		this.paidFee = paidFee;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
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

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Double getCashPaid() {
		return this.cashPaid;
	}

	public void setCashPaid(Double cashPaid) {
		this.cashPaid = cashPaid;
	}

	public Double getChequePaid() {
		return this.chequePaid;
	}

	public void setChequePaid(Double chequePaid) {
		this.chequePaid = chequePaid;
	}

	public Double getAccountPaid() {
		return this.accountPaid;
	}

	public void setAccountPaid(Double accountPaid) {
		this.accountPaid = accountPaid;
	}

}