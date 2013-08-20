package com.jlyw.hibernate.crm;

import java.sql.Timestamp;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SysUser;

/**
 * InsideContactorFeeAssign entity. @author MyEclipse Persistence Tools
 */

public class InsideContactorFeeAssign implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByInsideContactorId;
	private SysUser sysUserByLastEditorId;
	private Customer customer;
	private Integer year;
	private Double fee;
	private Timestamp lastEditTime;
	private String remark;

	// Constructors

	/** default constructor */
	public InsideContactorFeeAssign() {
	}

	/** minimal constructor */
	public InsideContactorFeeAssign(SysUser sysUserByInsideContactorId,
			SysUser sysUserByLastEditorId, Customer customer, Integer year,
			Double fee, Timestamp lastEditTime) {
		this.sysUserByInsideContactorId = sysUserByInsideContactorId;
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.customer = customer;
		this.year = year;
		this.fee = fee;
		this.lastEditTime = lastEditTime;
	}

	/** full constructor */
	public InsideContactorFeeAssign(SysUser sysUserByInsideContactorId,
			SysUser sysUserByLastEditorId, Customer customer, Integer year,
			Double fee, Timestamp lastEditTime, String remark) {
		this.sysUserByInsideContactorId = sysUserByInsideContactorId;
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.customer = customer;
		this.year = year;
		this.fee = fee;
		this.lastEditTime = lastEditTime;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByInsideContactorId() {
		return this.sysUserByInsideContactorId;
	}

	public void setSysUserByInsideContactorId(SysUser sysUserByInsideContactorId) {
		this.sysUserByInsideContactorId = sysUserByInsideContactorId;
	}

	public SysUser getSysUserByLastEditorId() {
		return this.sysUserByLastEditorId;
	}

	public void setSysUserByLastEditorId(SysUser sysUserByLastEditorId) {
		this.sysUserByLastEditorId = sysUserByLastEditorId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getYear() {
		return this.year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
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

}