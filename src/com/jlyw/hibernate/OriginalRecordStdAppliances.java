package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * OriginalRecordStdAppliances entity. @author MyEclipse Persistence Tools
 */

public class OriginalRecordStdAppliances implements java.io.Serializable {

	// Fields

	private Integer id;
	private OriginalRecord originalRecord;
	private StandardAppliance standardAppliance;
	private SysUser sysUser;
	private Timestamp lastEditTime;

	// Constructors

	/** default constructor */
	public OriginalRecordStdAppliances() {
	}

	/** minimal constructor */
	public OriginalRecordStdAppliances(OriginalRecord originalRecord,
			StandardAppliance standardAppliance, Timestamp lastEditTime) {
		this.originalRecord = originalRecord;
		this.standardAppliance = standardAppliance;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public OriginalRecordStdAppliances(OriginalRecord originalRecord,
			StandardAppliance standardAppliance, SysUser sysUser,
			Timestamp lastEditTime) {
		this.originalRecord = originalRecord;
		this.standardAppliance = standardAppliance;
		this.sysUser = sysUser;
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

	public StandardAppliance getStandardAppliance() {
		return this.standardAppliance;
	}

	public void setStandardAppliance(StandardAppliance standardAppliance) {
		this.standardAppliance = standardAppliance;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}