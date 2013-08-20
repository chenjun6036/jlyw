package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * SubContract entity. @author MyEclipse Persistence Tools
 */

public class SubContract implements java.io.Serializable {

	// Fields

	private Integer id;
	private CommissionSheet commissionSheet;
	private SysUser sysUserByReceiverId;
	private SysUser sysUserByHandlerId;
	private SysUser sysUserByLastEditorId;
	private SubContractor subContractor;
	private Timestamp subContractDate;
	private String attachment;
	private Integer status;
	private Timestamp receiveDate;
	private Timestamp lastEditTime;
	private String remark;
	private Double totalFee;

	// Constructors

	/** default constructor */
	public SubContract() {
	}

	/** minimal constructor */
	public SubContract(CommissionSheet commissionSheet,
			SysUser sysUserByLastEditorId, SubContractor subContractor,
			Integer status, Timestamp lastEditTime) {
		this.commissionSheet = commissionSheet;
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.subContractor = subContractor;
		this.status = status;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public SubContract(CommissionSheet commissionSheet,
			SysUser sysUserByReceiverId, SysUser sysUserByHandlerId,
			SysUser sysUserByLastEditorId, SubContractor subContractor,
			Timestamp subContractDate, String attachment, Integer status,
			Timestamp receiveDate, Timestamp lastEditTime, String remark,
			Double totalFee) {
		this.commissionSheet = commissionSheet;
		this.sysUserByReceiverId = sysUserByReceiverId;
		this.sysUserByHandlerId = sysUserByHandlerId;
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.subContractor = subContractor;
		this.subContractDate = subContractDate;
		this.attachment = attachment;
		this.status = status;
		this.receiveDate = receiveDate;
		this.lastEditTime = lastEditTime;
		this.remark = remark;
		this.totalFee = totalFee;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
	}

	public SysUser getSysUserByReceiverId() {
		return this.sysUserByReceiverId;
	}

	public void setSysUserByReceiverId(SysUser sysUserByReceiverId) {
		this.sysUserByReceiverId = sysUserByReceiverId;
	}

	public SysUser getSysUserByHandlerId() {
		return this.sysUserByHandlerId;
	}

	public void setSysUserByHandlerId(SysUser sysUserByHandlerId) {
		this.sysUserByHandlerId = sysUserByHandlerId;
	}

	public SysUser getSysUserByLastEditorId() {
		return this.sysUserByLastEditorId;
	}

	public void setSysUserByLastEditorId(SysUser sysUserByLastEditorId) {
		this.sysUserByLastEditorId = sysUserByLastEditorId;
	}

	public SubContractor getSubContractor() {
		return this.subContractor;
	}

	public void setSubContractor(SubContractor subContractor) {
		this.subContractor = subContractor;
	}

	public Timestamp getSubContractDate() {
		return this.subContractDate;
	}

	public void setSubContractDate(Timestamp subContractDate) {
		this.subContractDate = subContractDate;
	}

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getReceiveDate() {
		return this.receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
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

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

}