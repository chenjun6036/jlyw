package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * CustomerAccount entity. @author MyEclipse Persistence Tools
 */

public class CustomerAccount implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private Customer customer;
	private Integer handleType;
	private Timestamp handleTime;
	private String handlerName;
	private Double amount;
	private String remark;

	// Constructors

	/** default constructor */
	public CustomerAccount() {
	}

	/** minimal constructor */
	public CustomerAccount(Customer customer, Integer handleType,
			Timestamp handleTime, Double amount) {
		this.customer = customer;
		this.handleType = handleType;
		this.handleTime = handleTime;
		this.amount = amount;
	}

	/** full constructor */
	public CustomerAccount(SysUser sysUser, Customer customer,
			Integer handleType, Timestamp handleTime, String handlerName,
			Double amount, String remark) {
		this.sysUser = sysUser;
		this.customer = customer;
		this.handleType = handleType;
		this.handleTime = handleTime;
		this.handlerName = handlerName;
		this.amount = amount;
		this.remark = remark;
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

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getHandleType() {
		return this.handleType;
	}

	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}

	public Timestamp getHandleTime() {
		return this.handleTime;
	}

	public void setHandleTime(Timestamp handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandlerName() {
		return this.handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}