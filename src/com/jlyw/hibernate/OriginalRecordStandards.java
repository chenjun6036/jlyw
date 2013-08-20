package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * OriginalRecordStandards entity. @author MyEclipse Persistence Tools
 */

public class OriginalRecordStandards implements java.io.Serializable {

	// Fields

	private Integer id;
	private OriginalRecord originalRecord;
	private SysUser sysUser;
	private Standard standard;
	private Timestamp lastEditTime;

	// Constructors

	/** default constructor */
	public OriginalRecordStandards() {
	}

	/** minimal constructor */
	public OriginalRecordStandards(OriginalRecord originalRecord,
			Standard standard, Timestamp lastEditTime) {
		this.originalRecord = originalRecord;
		this.standard = standard;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public OriginalRecordStandards(OriginalRecord originalRecord,
			SysUser sysUser, Standard standard, Timestamp lastEditTime) {
		this.originalRecord = originalRecord;
		this.sysUser = sysUser;
		this.standard = standard;
		this.lastEditTime = lastEditTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OriginalRecord getOriginalRecord() {
		return this.originalRecord;
	}

	public void setOriginalRecord(OriginalRecord originalRecord) {
		this.originalRecord = originalRecord;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Standard getStandard() {
		return this.standard;
	}

	public void setStandard(Standard standard) {
		this.standard = standard;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}