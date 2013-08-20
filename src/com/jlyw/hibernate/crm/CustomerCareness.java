package com.jlyw.hibernate.crm;

import java.sql.Timestamp;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SysUser;

/**
 * CustomerCareness entity. @author MyEclipse Persistence Tools
 */

public class CustomerCareness implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByRepresentativeId;
	private SysUser sysUserByCreateSysUserId;
	private Customer customer;
	private SysUser sysUserByCareDutySysUserId;
	private String customerName;
	private Integer priority;
	private Timestamp createTime;
	private Integer status;
	private Timestamp time;
	private Integer way;
	private String careContactor;
	private String remark;
	private float fee;

	// Constructors

	/** default constructor */
	public CustomerCareness() {
	}

	/** minimal constructor */
	public CustomerCareness(SysUser sysUserByCreateSysUserId,
			Customer customer, String customerName, Integer priority,
			Timestamp createTime, Integer status, Timestamp time, Integer way,
			String careContactor,float fee) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.customer = customer;
		this.customerName = customerName;
		this.priority = priority;
		this.createTime = createTime;
		this.status = status;
		this.time = time;
		this.way = way;
		this.careContactor = careContactor;
		this.fee = fee;
	}

	/** full constructor */
	public CustomerCareness(SysUser sysUserByRepresentativeId,
			SysUser sysUserByCreateSysUserId, Customer customer,
			SysUser sysUserByCareDutySysUserId, String customerName,
			Integer priority, Timestamp createTime, Integer status,
			Timestamp time, Integer way, String careContactor, String remark,float fee) {
		this.sysUserByRepresentativeId = sysUserByRepresentativeId;
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.customer = customer;
		this.sysUserByCareDutySysUserId = sysUserByCareDutySysUserId;
		this.customerName = customerName;
		this.priority = priority;
		this.createTime = createTime;
		this.status = status;
		this.time = time;
		this.way = way;
		this.careContactor = careContactor;
		this.remark = remark;
		this.fee = fee;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByRepresentativeId() {
		return this.sysUserByRepresentativeId;
	}

	public void setSysUserByRepresentativeId(SysUser sysUserByRepresentativeId) {
		this.sysUserByRepresentativeId = sysUserByRepresentativeId;
	}

	public SysUser getSysUserByCreateSysUserId() {
		return this.sysUserByCreateSysUserId;
	}

	public void setSysUserByCreateSysUserId(SysUser sysUserByCreateSysUserId) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public SysUser getSysUserByCareDutySysUserId() {
		return this.sysUserByCareDutySysUserId;
	}

	public void setSysUserByCareDutySysUserId(SysUser sysUserByCareDutySysUserId) {
		this.sysUserByCareDutySysUserId = sysUserByCareDutySysUserId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getTime() {
		return this.time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public Integer getWay() {
		return this.way;
	}

	public void setWay(Integer way) {
		this.way = way;
	}

	public String getCareContactor() {
		return this.careContactor;
	}

	public void setCareContactor(String careContactor) {
		this.careContactor = careContactor;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public float getFee() {
		return fee;
	}

	public void setFee(float fee) {
		this.fee = fee;
	}

}