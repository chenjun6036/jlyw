package com.jlyw.hibernate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * OriginalRecordExcel entity. @author MyEclipse Persistence Tools
 */

public class OriginalRecordExcel implements java.io.Serializable {

	// Fields

	private Integer id;
	private CommissionSheet commissionSheet;
	private OriginalRecord originalRecord;
	private String code;
	private Integer version;
	private String pdf;
	private String doc;
	private Timestamp lastEditTime;
	private String xml;
	private String fileName;
	private String certificateCode;

	// Constructors

	/** default constructor */
	public OriginalRecordExcel() {
	}

	/** minimal constructor */
	public OriginalRecordExcel(CommissionSheet commissionSheet,
			OriginalRecord originalRecord, String code, Integer version,
			String doc, Timestamp lastEditTime, String xml, String fileName) {
		this.commissionSheet = commissionSheet;
		this.originalRecord = originalRecord;
		this.code = code;
		this.version = version;
		this.doc = doc;
		this.lastEditTime = lastEditTime;
		this.xml = xml;
		this.fileName = fileName;
	}

	/** full constructor */
	public OriginalRecordExcel(CommissionSheet commissionSheet,
			OriginalRecord originalRecord, String code, Integer version,
			String pdf, String doc, Timestamp lastEditTime, String xml,
			String fileName, String certificateCode) {
		this.commissionSheet = commissionSheet;
		this.originalRecord = originalRecord;
		this.code = code;
		this.version = version;
		this.pdf = pdf;
		this.doc = doc;
		this.lastEditTime = lastEditTime;
		this.xml = xml;
		this.fileName = fileName;
		this.certificateCode = certificateCode;
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

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public String getXml() {
		return this.xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCertificateCode() {
		return this.certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}
}