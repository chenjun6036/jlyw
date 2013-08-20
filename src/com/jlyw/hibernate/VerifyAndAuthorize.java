package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * VerifyAndAuthorize entity. @author MyEclipse Persistence Tools
 */

public class VerifyAndAuthorize implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByCreatorId;
	private CommissionSheet commissionSheet;
	private Certificate certificate;
	private SysUser sysUserByAuthorizerId;
	private OriginalRecordExcel originalRecordExcel;
	private SysUser sysUserByVerifierId;
	private SysUser sysUserByAuthorizeExecutorId;
	private String code;
	private Integer version;
	private Timestamp verifyTime;
	private Boolean verifyResult;
	private String verifyRemark;
	private Timestamp authorizeTime;
	private Boolean authorizeResult;
	private String authorizeRemark;
	private Timestamp createTime;
	private Boolean isAuthBgRuning;

	// Constructors

	/** default constructor */
	public VerifyAndAuthorize() {
	}

	/** minimal constructor */
	public VerifyAndAuthorize(SysUser sysUserByCreatorId,
			CommissionSheet commissionSheet, Certificate certificate,
			OriginalRecordExcel originalRecordExcel, String code,
			Integer version, Timestamp createTime) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.commissionSheet = commissionSheet;
		this.certificate = certificate;
		this.originalRecordExcel = originalRecordExcel;
		this.code = code;
		this.version = version;
		this.createTime = createTime;
	}

	/** full constructor */
	public VerifyAndAuthorize(SysUser sysUserByCreatorId,
			CommissionSheet commissionSheet, Certificate certificate,
			SysUser sysUserByAuthorizerId,
			OriginalRecordExcel originalRecordExcel,
			SysUser sysUserByVerifierId, SysUser sysUserByAuthorizeExecutorId,
			String code, Integer version, Timestamp verifyTime,
			Boolean verifyResult, String verifyRemark, Timestamp authorizeTime,
			Boolean authorizeResult, String authorizeRemark,
			Timestamp createTime, Boolean isAuthBgRuning) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.commissionSheet = commissionSheet;
		this.certificate = certificate;
		this.sysUserByAuthorizerId = sysUserByAuthorizerId;
		this.originalRecordExcel = originalRecordExcel;
		this.sysUserByVerifierId = sysUserByVerifierId;
		this.sysUserByAuthorizeExecutorId = sysUserByAuthorizeExecutorId;
		this.code = code;
		this.version = version;
		this.verifyTime = verifyTime;
		this.verifyResult = verifyResult;
		this.verifyRemark = verifyRemark;
		this.authorizeTime = authorizeTime;
		this.authorizeResult = authorizeResult;
		this.authorizeRemark = authorizeRemark;
		this.createTime = createTime;
		this.isAuthBgRuning = isAuthBgRuning;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByCreatorId() {
		return this.sysUserByCreatorId;
	}

	public void setSysUserByCreatorId(SysUser sysUserByCreatorId) {
		this.sysUserByCreatorId = sysUserByCreatorId;
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

	public SysUser getSysUserByAuthorizerId() {
		return this.sysUserByAuthorizerId;
	}

	public void setSysUserByAuthorizerId(SysUser sysUserByAuthorizerId) {
		this.sysUserByAuthorizerId = sysUserByAuthorizerId;
	}

	public OriginalRecordExcel getOriginalRecordExcel() {
		return this.originalRecordExcel;
	}

	public void setOriginalRecordExcel(OriginalRecordExcel originalRecordExcel) {
		this.originalRecordExcel = originalRecordExcel;
	}

	public SysUser getSysUserByVerifierId() {
		return this.sysUserByVerifierId;
	}

	public void setSysUserByVerifierId(SysUser sysUserByVerifierId) {
		this.sysUserByVerifierId = sysUserByVerifierId;
	}

	public SysUser getSysUserByAuthorizeExecutorId() {
		return this.sysUserByAuthorizeExecutorId;
	}

	public void setSysUserByAuthorizeExecutorId(
			SysUser sysUserByAuthorizeExecutorId) {
		this.sysUserByAuthorizeExecutorId = sysUserByAuthorizeExecutorId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Timestamp getVerifyTime() {
		return this.verifyTime;
	}

	public void setVerifyTime(Timestamp verifyTime) {
		this.verifyTime = verifyTime;
	}

	public Boolean getVerifyResult() {
		return this.verifyResult;
	}

	public void setVerifyResult(Boolean verifyResult) {
		this.verifyResult = verifyResult;
	}

	public String getVerifyRemark() {
		return this.verifyRemark;
	}

	public void setVerifyRemark(String verifyRemark) {
		this.verifyRemark = verifyRemark;
	}

	public Timestamp getAuthorizeTime() {
		return this.authorizeTime;
	}

	public void setAuthorizeTime(Timestamp authorizeTime) {
		this.authorizeTime = authorizeTime;
	}

	public Boolean getAuthorizeResult() {
		return this.authorizeResult;
	}

	public void setAuthorizeResult(Boolean authorizeResult) {
		this.authorizeResult = authorizeResult;
	}

	public String getAuthorizeRemark() {
		return this.authorizeRemark;
	}

	public void setAuthorizeRemark(String authorizeRemark) {
		this.authorizeRemark = authorizeRemark;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsAuthBgRuning() {
		return this.isAuthBgRuning;
	}

	public void setIsAuthBgRuning(Boolean isAuthBgRuning) {
		this.isAuthBgRuning = isAuthBgRuning;
	}

}