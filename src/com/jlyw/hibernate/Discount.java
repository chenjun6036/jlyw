package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Discount entity. @author MyEclipse Persistence Tools
 */

public class Discount implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByExecutorId;
	private SysUser sysUserByRequesterId;
	private Reason reason;
	private Customer customer;
	private String contector;
	private String contectorTel;
	private Timestamp applyTime;
	private Timestamp executeTime;
	private Boolean executeResult;
	private String executeMsg;

	// Constructors

	/** default constructor */
	public Discount() {
	}

	/** minimal constructor */
	public Discount(SysUser sysUserByRequesterId, Reason reason,
			Customer customer, Timestamp applyTime) {
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.customer = customer;
		this.applyTime = applyTime;
	}

	/** full constructor */
	public Discount(SysUser sysUserByExecutorId, SysUser sysUserByRequesterId,
			Reason reason, Customer customer, String contector,
			String contectorTel, Timestamp applyTime, Timestamp executeTime,
			Boolean executeResult, String executeMsg) {
		this.sysUserByExecutorId = sysUserByExecutorId;
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.customer = customer;
		this.contector = contector;
		this.contectorTel = contectorTel;
		this.applyTime = applyTime;
		this.executeTime = executeTime;
		this.executeResult = executeResult;
		this.executeMsg = executeMsg;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByExecutorId() {
		return this.sysUserByExecutorId;
	}

	public void setSysUserByExecutorId(SysUser sysUserByExecutorId) {
		this.sysUserByExecutorId = sysUserByExecutorId;
	}

	public SysUser getSysUserByRequesterId() {
		return this.sysUserByRequesterId;
	}

	public void setSysUserByRequesterId(SysUser sysUserByRequesterId) {
		this.sysUserByRequesterId = sysUserByRequesterId;
	}

	public Reason getReason() {
		return this.reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getContector() {
		return this.contector;
	}

	public void setContector(String contector) {
		this.contector = contector;
	}

	public String getContectorTel() {
		return this.contectorTel;
	}

	public void setContectorTel(String contectorTel) {
		this.contectorTel = contectorTel;
	}

	public Timestamp getApplyTime() {
		return this.applyTime;
	}

	public void setApplyTime(Timestamp applyTime) {
		this.applyTime = applyTime;
	}

	public Timestamp getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(Timestamp executeTime) {
		this.executeTime = executeTime;
	}

	public Boolean getExecuteResult() {
		return this.executeResult;
	}

	public void setExecuteResult(Boolean executeResult) {
		this.executeResult = executeResult;
	}

	public String getExecuteMsg() {
		return this.executeMsg;
	}

	public void setExecuteMsg(String executeMsg) {
		this.executeMsg = executeMsg;
	}

}