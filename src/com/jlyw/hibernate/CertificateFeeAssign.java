package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * CertificateFeeAssign entity. @author MyEclipse Persistence Tools
 */

public class CertificateFeeAssign implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByFeeAlloteeId;
	private CommissionSheet commissionSheet;
	private Certificate certificate;
	private OriginalRecord originalRecord;
	private SysUser sysUserByLastEditorId;
	private Timestamp lastEditTime;
	private Double testFee;
	private Double repairFee;
	private Double materialFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;
	private String remark;
	private Double testFeeOld;
	private Double repairFeeOld;
	private Double materialFeeOld;
	private Double carFeeOld;
	private Double debugFeeOld;
	private Double otherFeeOld;
	private Double totalFeeOld;

	// Constructors

	/** default constructor */
	public CertificateFeeAssign() {
	}

	/** minimal constructor */
	public CertificateFeeAssign(CommissionSheet commissionSheet,
			Timestamp lastEditTime) {
		this.commissionSheet = commissionSheet;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public CertificateFeeAssign(SysUser sysUserByFeeAlloteeId,
			CommissionSheet commissionSheet, Certificate certificate,
			OriginalRecord originalRecord, SysUser sysUserByLastEditorId,
			Timestamp lastEditTime, Double testFee, Double repairFee,
			Double materialFee, Double carFee, Double debugFee,
			Double otherFee, Double totalFee, String remark, Double testFeeOld,
			Double repairFeeOld, Double materialFeeOld, Double carFeeOld,
			Double debugFeeOld, Double otherFeeOld, Double totalFeeOld) {
		this.sysUserByFeeAlloteeId = sysUserByFeeAlloteeId;
		this.commissionSheet = commissionSheet;
		this.certificate = certificate;
		this.originalRecord = originalRecord;
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.lastEditTime = lastEditTime;
		this.testFee = testFee;
		this.repairFee = repairFee;
		this.materialFee = materialFee;
		this.carFee = carFee;
		this.debugFee = debugFee;
		this.otherFee = otherFee;
		this.totalFee = totalFee;
		this.remark = remark;
		this.testFeeOld = testFeeOld;
		this.repairFeeOld = repairFeeOld;
		this.materialFeeOld = materialFeeOld;
		this.carFeeOld = carFeeOld;
		this.debugFeeOld = debugFeeOld;
		this.otherFeeOld = otherFeeOld;
		this.totalFeeOld = totalFeeOld;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByFeeAlloteeId() {
		return this.sysUserByFeeAlloteeId;
	}

	public void setSysUserByFeeAlloteeId(SysUser sysUserByFeeAlloteeId) {
		this.sysUserByFeeAlloteeId = sysUserByFeeAlloteeId;
	}

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
	}

	public Certificate getCertificate() {
		return this.certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public OriginalRecord getOriginalRecord() {
		return this.originalRecord;
	}

	public void setOriginalRecord(OriginalRecord originalRecord) {
		this.originalRecord = originalRecord;
	}

	public SysUser getSysUserByLastEditorId() {
		return this.sysUserByLastEditorId;
	}

	public void setSysUserByLastEditorId(SysUser sysUserByLastEditorId) {
		this.sysUserByLastEditorId = sysUserByLastEditorId;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Double getTestFeeOld() {
		return this.testFeeOld;
	}

	public void setTestFeeOld(Double testFeeOld) {
		this.testFeeOld = testFeeOld;
	}

	public Double getRepairFeeOld() {
		return this.repairFeeOld;
	}

	public void setRepairFeeOld(Double repairFeeOld) {
		this.repairFeeOld = repairFeeOld;
	}

	public Double getMaterialFeeOld() {
		return this.materialFeeOld;
	}

	public void setMaterialFeeOld(Double materialFeeOld) {
		this.materialFeeOld = materialFeeOld;
	}

	public Double getCarFeeOld() {
		return this.carFeeOld;
	}

	public void setCarFeeOld(Double carFeeOld) {
		this.carFeeOld = carFeeOld;
	}

	public Double getDebugFeeOld() {
		return this.debugFeeOld;
	}

	public void setDebugFeeOld(Double debugFeeOld) {
		this.debugFeeOld = debugFeeOld;
	}

	public Double getOtherFeeOld() {
		return this.otherFeeOld;
	}

	public void setOtherFeeOld(Double otherFeeOld) {
		this.otherFeeOld = otherFeeOld;
	}

	public Double getTotalFeeOld() {
		return this.totalFeeOld;
	}

	public void setTotalFeeOld(Double totalFeeOld) {
		this.totalFeeOld = totalFeeOld;
	}

}