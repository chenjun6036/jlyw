package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Certificate entity. @author MyEclipse Persistence Tools
 */

public class Certificate implements java.io.Serializable {

	// Fields

	private Integer id;
	private CommissionSheet commissionSheet;
	private OriginalRecord originalRecord;
	private SysUser sysUser;
	private String code;
	private Integer version;
	private Integer type;
	private Timestamp lastEditTime;
	private String pdf;
	private String doc;
	private Integer sequece;
	private String certificateCode;
	private String fileName;
	private String xml;

	// Constructors

	/** default constructor */
	public Certificate() {
	}

	/** minimal constructor */
	public Certificate(CommissionSheet commissionSheet,
			OriginalRecord originalRecord, String code, Integer version,
			Timestamp lastEditTime, String doc, Integer sequece,
			String certificateCode, String fileName) {
		this.commissionSheet = commissionSheet;
		this.originalRecord = originalRecord;
		this.code = code;
		this.version = version;
		this.lastEditTime = lastEditTime;
		this.doc = doc;
		this.sequece = sequece;
		this.certificateCode = certificateCode;
		this.fileName = fileName;
	}

	/** full constructor */
	public Certificate(CommissionSheet commissionSheet,
			OriginalRecord originalRecord, SysUser sysUser, String code,
			Integer version, Integer type, Timestamp lastEditTime, String pdf,
			String doc, Integer sequece, String certificateCode,
			String fileName, String xml) {
		this.commissionSheet = commissionSheet;
		this.originalRecord = originalRecord;
		this.sysUser = sysUser;
		this.code = code;
		this.version = version;
		this.type = type;
		this.lastEditTime = lastEditTime;
		this.pdf = pdf;
		this.doc = doc;
		this.sequece = sequece;
		this.certificateCode = certificateCode;
		this.fileName = fileName;
		this.xml = xml;
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

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public String getPdf() {
		return this.pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public String getDoc() {
		return this.doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public Integer getSequece() {
		return this.sequece;
	}

	public void setSequece(Integer sequece) {
		this.sequece = sequece;
	}

	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}
}