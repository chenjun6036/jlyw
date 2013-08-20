package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * RepairRecord entity. @author MyEclipse Persistence Tools
 */

public class RepairRecord implements java.io.Serializable {

	// Fields

	private Integer id;
	private OriginalRecord originalRecord;
	private Integer repairItemId;
	private Double fee;
	private String partName;
	private Double partFee;
	private Timestamp date;

	// Constructors

	/** default constructor */
	public RepairRecord() {
	}

	/** minimal constructor */
	public RepairRecord(OriginalRecord originalRecord, Integer repairItemId,
			Double fee, Timestamp date) {
		this.originalRecord = originalRecord;
		this.repairItemId = repairItemId;
		this.fee = fee;
		this.date = date;
	}

	/** full constructor */
	public RepairRecord(OriginalRecord originalRecord, Integer repairItemId,
			Double fee, String partName, Double partFee, Timestamp date) {
		this.originalRecord = originalRecord;
		this.repairItemId = repairItemId;
		this.fee = fee;
		this.partName = partName;
		this.partFee = partFee;
		this.date = date;
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

	public Integer getRepairItemId() {
		return this.repairItemId;
	}

	public void setRepairItemId(Integer repairItemId) {
		this.repairItemId = repairItemId;
	}

	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getPartName() {
		return this.partName;
	}

	public void setPartName(String partName) {
		this.partName = partName;
	}

	public Double getPartFee() {
		return this.partFee;
	}

	public void setPartFee(Double partFee) {
		this.partFee = partFee;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

}