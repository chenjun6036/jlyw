package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Overdue entity. @author MyEclipse Persistence Tools
 */

public class Overdue implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByExecutorId;
	private CommissionSheet commissionSheet;
	private SysUser sysUserByRequesterId;
	private Reason reason;
	private Integer delayDays;
	private Timestamp applyTime;
	private Timestamp executeTime;
	private Boolean executeResult;
	private String executeMsg;

	// Constructors

	/** default constructor */
	public Overdue() {
	}

	/** minimal constructor */
	public Overdue(CommissionSheet commissionSheet,
			SysUser sysUserByRequesterId, Reason reason, Integer delayDays,
			Timestamp applyTime) {
		this.commissionSheet = commissionSheet;
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.delayDays = delayDays;
		this.applyTime = applyTime;
	}

	/** full constructor */
	public Overdue(SysUser sysUserByExecutorId,
			CommissionSheet commissionSheet, SysUser sysUserByRequesterId,
			Reason reason, Integer delayDays, Timestamp applyTime,
			Timestamp executeTime, Boolean executeResult, String executeMsg) {
		this.sysUserByExecutorId = sysUserByExecutorId;
		this.commissionSheet = commissionSheet;
		this.sysUserByRequesterId = sysUserByRequesterId;
		this.reason = reason;
		this.delayDays = delayDays;
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

	public Integer getDelayDays() {
		return this.delayDays;
	}

	public void setDelayDays(Integer delayDays) {
		this.delayDays = delayDays;
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