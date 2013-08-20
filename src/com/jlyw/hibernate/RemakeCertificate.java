package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * RemakeCertificate entity. @author MyEclipse Persistence Tools
 */

public class RemakeCertificate implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByCreatorId;
	private OriginalRecord originalRecord;
	private SysUser sysUserByReceiverId;
	private String certificateCode;
	private Timestamp createTime;
	private String createRemark;
	private Timestamp finishTime;
	private String finishRemark;
	private Timestamp passedTime;

	// Constructors

	/** default constructor */
	public RemakeCertificate() {
	}

	/** minimal constructor */
	public RemakeCertificate(SysUser sysUserByCreatorId,
			OriginalRecord originalRecord, SysUser sysUserByReceiverId,
			String certificateCode, Timestamp createTime) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.originalRecord = originalRecord;
		this.sysUserByReceiverId = sysUserByReceiverId;
		this.certificateCode = certificateCode;
		this.createTime = createTime;
	}

	/** full constructor */
	public RemakeCertificate(SysUser sysUserByCreatorId,
			OriginalRecord originalRecord, SysUser sysUserByReceiverId,
			String certificateCode, Timestamp createTime, String createRemark,
			Timestamp finishTime, String finishRemark, Timestamp passedTime) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.originalRecord = originalRecord;
		this.sysUserByReceiverId = sysUserByReceiverId;
		this.certificateCode = certificateCode;
		this.createTime = createTime;
		this.createRemark = createRemark;
		this.finishTime = finishTime;
		this.finishRemark = finishRemark;
		this.passedTime = passedTime;
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

	public OriginalRecord getOriginalRecord() {
		return this.originalRecord;
	}

	public void setOriginalRecord(OriginalRecord originalRecord) {
		this.originalRecord = originalRecord;
	}

	public SysUser getSysUserByReceiverId() {
		return this.sysUserByReceiverId;
	}

	public void setSysUserByReceiverId(SysUser sysUserByReceiverId) {
		this.sysUserByReceiverId = sysUserByReceiverId;
	}

	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getCreateRemark() {
		return this.createRemark;
	}

	public void setCreateRemark(String createRemark) {
		this.createRemark = createRemark;
	}

	public Timestamp getFinishTime() {
		return this.finishTime;
	}

	public void setFinishTime(Timestamp finishTime) {
		this.finishTime = finishTime;
	}

	public String getFinishRemark() {
		return this.finishRemark;
	}

	public void setFinishRemark(String finishRemark) {
		this.finishRemark = finishRemark;
	}

	public Timestamp getPassedTime() {
		return this.passedTime;
	}

	public void setPassedTime(Timestamp passedTime) {
		this.passedTime = passedTime;
	}

}