package com.jlyw.hibernate.view;

import java.sql.Timestamp;

/**
 * ViewTransaction entity. @author MyEclipse Persistence Tools
 */

public class ViewTransaction implements java.io.Serializable {

	// Fields
	private ViewTransactionId id;
	private Integer allotee;
	private Integer tstatus;
	private String code;
	private Timestamp commissionDate;
	private Integer customerId;
	private String customerName;
	private String applianceName;
	private Integer cstatus;
	private String headName;

	// Constructors
	public ViewTransaction(){
	}
	
	public ViewTransaction(ViewTransactionId id, Integer allotee,
			Integer tstatus, String code,
			Timestamp commissionDate, Integer customerId, String customerName, String applianceName, Integer cstatus, String headName) {
		super();
		this.id = id;
		this.allotee = allotee;
		this.tstatus = tstatus;
		this.code = code;
		this.commissionDate = commissionDate;
		this.customerId = customerId;
		this.customerName = customerName;
		this.applianceName = applianceName;
		this.cstatus = cstatus;
		this.headName = headName;
	}

	// Property accessors

	public ViewTransactionId getId() {
		return this.id;
	}

	public void setId(ViewTransactionId id) {
		this.id = id;
	}
	
	public Integer getAllotee() {
		return this.allotee;
	}

	public void setAllotee(Integer allotee) {
		this.allotee = allotee;
	}

	public Integer getTstatus() {
		return this.tstatus;
	}

	public void setTstatus(Integer tstatus) {
		this.tstatus = tstatus;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Timestamp getCommissionDate() {
		return this.commissionDate;
	}

	public void setCommissionDate(Timestamp commissionDate) {
		this.commissionDate = commissionDate;
	}

	public Integer getCustomerId() {
		return this.customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public String getApplianceName() {
		return applianceName;
	}

	public void setApplianceName(String applianceName) {
		this.applianceName = applianceName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public Integer getCstatus() {
		return this.cstatus;
	}

	public void setCstatus(Integer cstatus) {
		this.cstatus = cstatus;
	}
	
	public String getHeadName() {
		return this.headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

}