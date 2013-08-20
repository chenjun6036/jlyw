package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * TaskAssign entity. @author MyEclipse Persistence Tools
 */

public class TaskAssign implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByAssignerId;
	private CommissionSheet commissionSheet;
	private AppStdNameProTeam appStdNameProTeam;
	private SysUser sysUserByLastFeeAssignerId;
	private SysUser sysUserByAlloteeId;
	private Timestamp assignTime;
	private String remark;
	private Integer status;
	private Timestamp finishTime;
	private Double testFee;
	private Double repairFee;
	private Double materialFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;
	private Timestamp lastFeeAssignTime;

	// Constructors

	/** default constructor */
	public TaskAssign() {
	}

	/** minimal constructor */
	public TaskAssign(CommissionSheet commissionSheet,
			SysUser sysUserByAlloteeId, Timestamp assignTime) {
		this.commissionSheet = commissionSheet;
		this.sysUserByAlloteeId = sysUserByAlloteeId;
		this.assignTime = assignTime;
	}

	/** full constructor */
	public TaskAssign(SysUser sysUserByAssignerId,
			CommissionSheet commissionSheet,
			AppStdNameProTeam appStdNameProTeam,
			SysUser sysUserByLastFeeAssignerId, SysUser sysUserByAlloteeId,
			Timestamp assignTime, String remark, Integer status,
			Timestamp finishTime, Double testFee, Double repairFee,
			Double materialFee, Double carFee, Double debugFee,
			Double otherFee, Double totalFee, Timestamp lastFeeAssignTime) {
		this.sysUserByAssignerId = sysUserByAssignerId;
		this.commissionSheet = commissionSheet;
		this.appStdNameProTeam = appStdNameProTeam;
		this.sysUserByLastFeeAssignerId = sysUserByLastFeeAssignerId;
		this.sysUserByAlloteeId = sysUserByAlloteeId;
		this.assignTime = assignTime;
		this.remark = remark;
		this.status = status;
		this.finishTime = finishTime;
		this.testFee = testFee;
		this.repairFee = repairFee;
		this.materialFee = materialFee;
		this.carFee = carFee;
		this.debugFee = debugFee;
		this.otherFee = otherFee;
		this.totalFee = totalFee;
		this.lastFeeAssignTime = lastFeeAssignTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByAssignerId() {
		return this.sysUserByAssignerId;
	}

	public void setSysUserByAssignerId(SysUser sysUserByAssignerId) {
		this.sysUserByAssignerId = sysUserByAssignerId;
	}

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
	}

	public AppStdNameProTeam getAppStdNameProTeam() {
		return this.appStdNameProTeam;
	}

	public void setAppStdNameProTeam(AppStdNameProTeam appStdNameProTeam) {
		this.appStdNameProTeam = appStdNameProTeam;
	}

	public SysUser getSysUserByLastFeeAssignerId() {
		return this.sysUserByLastFeeAssignerId;
	}

	public void setSysUserByLastFeeAssignerId(SysUser sysUserByLastFeeAssignerId) {
		this.sysUserByLastFeeAssignerId = sysUserByLastFeeAssignerId;
	}

	public SysUser getSysUserByAlloteeId() {
		return this.sysUserByAlloteeId;
	}

	public void setSysUserByAlloteeId(SysUser sysUserByAlloteeId) {
		this.sysUserByAlloteeId = sysUserByAlloteeId;
	}

	public Timestamp getAssignTime() {
		return this.assignTime;
	}

	public void setAssignTime(Timestamp assignTime) {
		this.assignTime = assignTime;
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

	public Timestamp getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	public Double getTestFee() {
		return this.testFee;
	}

	public void setTestFee(Double testFee) {
		this.testFee = testFee;
	}

	public Double getRepairFee() {
		return this.repairFee;
	}

	public void setRepairFee(Double repairFee) {
		this.repairFee = repairFee;
	}

	public Double getMaterialFee() {
		return this.materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public Double getCarFee() {
		return this.carFee;
	}

	public void setCarFee(Double carFee) {
		this.carFee = carFee;
	}

	public Double getDebugFee() {
		return this.debugFee;
	}

	public void setDebugFee(Double debugFee) {
		this.debugFee = debugFee;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Timestamp getLastFeeAssignTime() {
		return this.lastFeeAssignTime;
	}

	public void setLastFeeAssignTime(Timestamp lastFeeAssignTime) {
		this.lastFeeAssignTime = lastFeeAssignTime;
	}

}