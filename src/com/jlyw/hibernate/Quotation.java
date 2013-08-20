package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Quotation entity. @author MyEclipse Persistence Tools
 */

public class Quotation implements java.io.Serializable {

	// Fields

	private String number;
	private Customer customer;
	private SysUser sysUser;
	private Integer version;
	private String contactor;
	private String contactorTel;
	private String customerName;
	private Double carCost;
	private Timestamp offerDate;
	private String remark;
	private Integer status;
	private String contactorEmail;

	// Constructors

	/** default constructor */
	public Quotation() {
	}

	/** minimal constructor */
	public Quotation(String number, Integer version, String contactor,
			String contactorTel, String customerName, Double carCost,
			Timestamp offerDate, Integer status) {
		this.number = number;
		this.version = version;
		this.contactor = contactor;
		this.contactorTel = contactorTel;
		this.customerName = customerName;
		this.carCost = carCost;
		this.offerDate = offerDate;
		this.status = status;
	}

	/** full constructor */
	public Quotation(String number, Customer customer, SysUser sysUser,
			Integer version, String contactor, String contactorTel,
			String customerName, Double carCost, Timestamp offerDate,
			String remark, Integer status, String contactorEmail) {
		this.number = number;
		this.customer = customer;
		this.sysUser = sysUser;
		this.version = version;
		this.contactor = contactor;
		this.contactorTel = contactorTel;
		this.customerName = customerName;
		this.carCost = carCost;
		this.offerDate = offerDate;
		this.remark = remark;
		this.status = status;
		this.contactorEmail = contactorEmail;
	}

	// Property accessors

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getContactor() {
		return this.contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getContactorTel() {
		return this.contactorTel;
	}

	public void setContactorTel(String contactorTel) {
		this.contactorTel = contactorTel;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Double getCarCost() {
		return this.carCost;
	}

	public void setCarCost(Double carCost) {
		this.carCost = carCost;
	}

	public Timestamp getOfferDate() {
		return this.offerDate;
	}

	public void setOfferDate(Timestamp offerDate) {
		this.offerDate = offerDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContactorEmail() {
		return this.contactorEmail;
	}

	public void setContactorEmail(String contactorEmail) {
		this.contactorEmail = contactorEmail;
	}

}