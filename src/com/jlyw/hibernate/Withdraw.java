package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Withdraw entity. @author MyEclipse Persistence Tools
 */

public class Withdraw implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByExecutorId;
	private CommissionSheet commissionSheet;
	private SysUser sysUserByRequesterId;
	private Reason reason;
	private Integer number;
	private String description;
	private Timestamp withdrawDate;
	private String location;
	private Timestamp requestTime;
	private Timestamp executeTime;
	private Boolean executeResult;
	private String executeMsg;

	// Constructors

	/** default constructor */
	public Withdraw() {
	}

	/** minimal constructor */
	public Withdraw(CommissionSheet commissionSheet,
			SysUser sysUserByRequesterId, Reason reason, Integer number,
			Timestamp requestTime) {
		this.commissionSheet = commissionSheet;
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.number = number;
		this.requestTime = requestTime;
	}

	/** full constructor */
	public Withdraw(SysUser sysUserByExecutorId,
			CommissionSheet commissionSheet, SysUser sysUserByRequesterId,
			Reason reason, Integer number, String description,
			Timestamp withdrawDate, String location, Timestamp requestTime,
			Timestamp executeTime, Boolean executeResult, String executeMsg) {
		this.sysUserByExecutorId = sysUserByExecutorId;
		this.commissionSheet = commissionSheet;
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.number = number;
		this.description = description;
		this.withdrawDate = withdrawDate;
		this.location = location;
		this.requestTime = requestTime;
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

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
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

	public Integer getNumber() {
		return this.number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getWithdrawDate() {
		return this.withdrawDate;
	}

	public void setWithdrawDate(Timestamp withdrawDate) {
		this.withdrawDate = withdrawDate;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Timestamp getRequestTime() {
		return this.requestTime;
	}

	public void setRequestTime(Timestamp requestTime) {
		this.requestTime = requestTime;
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