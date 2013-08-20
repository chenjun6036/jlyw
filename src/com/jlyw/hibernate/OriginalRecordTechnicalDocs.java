package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * OriginalRecordTechnicalDocs entity. @author MyEclipse Persistence Tools
 */

public class OriginalRecordTechnicalDocs implements java.io.Serializable {

	// Fields

	private Integer id;
	private Specification specification;
	private OriginalRecord originalRecord;
	private SysUser sysUser;
	private Timestamp lastEditTime;

	// Constructors

	/** default constructor */
	public OriginalRecordTechnicalDocs() {
	}

	/** minimal constructor */
	public OriginalRecordTechnicalDocs(Specification specification,
			OriginalRecord originalRecord, Timestamp lastEditTime) {
		this.specification = specification;
		this.originalRecord = originalRecord;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public OriginalRecordTechnicalDocs(Specification specification,
			OriginalRecord originalRecord, SysUser sysUser,
			Timestamp lastEditTime) {
		this.specification = specification;
		this.originalRecord = originalRecord;
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

	public Specification getSpecification() {
		return this.specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
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

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

}