package com.jlyw.hibernate.crm;

import java.sql.Timestamp;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SysUser;

/**
 * CustomerServiceFee entity. @author MyEclipse Persistence Tools
 */

public class CustomerServiceFee implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByCreateSysUserId;
	private SysUser sysUserByPaidSysUserId;
	private Customer customer;
	private String billNum;
	private Timestamp createTime;
	private Timestamp paidTime;
	private Integer paidVia;
	private String paidSubject;
	private Double money;
	private String remark;
	private Integer status;
	private String attachment;

	// Constructors

	/** default constructor */
	public CustomerServiceFee() {
	}

	/** minimal constructor */
	public CustomerServiceFee(SysUser sysUserByCreateSysUserId,
			SysUser sysUserByPaidSysUserId, Customer customer, String billNum,
			Timestamp createTime, Timestamp paidTime, Integer paidVia,
			String paidSubject, Double money, Integer status) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.sysUserByPaidSysUserId = sysUserByPaidSysUserId;
		this.customer = customer;
		this.billNum = billNum;
		this.createTime = createTime;
		this.paidTime = paidTime;
		this.paidVia = paidVia;
		this.paidSubject = paidSubject;
		this.money = money;
		this.status = status;
	}

	/** full constructor */
	public CustomerServiceFee(SysUser sysUserByCreateSysUserId,
			SysUser sysUserByPaidSysUserId, Customer customer, String billNum,
			Timestamp createTime, Timestamp paidTime, Integer paidVia,
			String paidSubject, Double money, String remark, Integer status,
			String attachment) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.sysUserByPaidSysUserId = sysUserByPaidSysUserId;
		this.customer = customer;
		this.billNum = billNum;
		this.createTime = createTime;
		this.paidTime = paidTime;
		this.paidVia = paidVia;
		this.paidSubject = paidSubject;
		this.money = money;
		this.remark = remark;
		this.status = status;
		this.attachment = attachment;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByCreateSysUserId() {
		return this.sysUserByCreateSysUserId;
	}

	public void setSysUserByCreateSysUserId(SysUser sysUserByCreateSysUserId) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
	}

	public SysUser getSysUserByPaidSysUserId() {
		return this.sysUserByPaidSysUserId;
	}

	public void setSysUserByPaidSysUserId(SysUser sysUserByPaidSysUserId) {
		this.sysUserByPaidSysUserId = sysUserByPaidSysUserId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getBillNum() {
		return this.billNum;
	}

	public void setBillNum(String billNum) {
		this.billNum = billNum;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Timestamp getPaidTime() {
		return this.paidTime;
	}

	public void setPaidTime(Timestamp paidTime) {
		this.paidTime = paidTime;
	}

	public Integer getPaidVia() {
		return this.paidVia;
	}

	public void setPaidVia(Integer paidVia) {
		this.paidVia = paidVia;
	}

	public String getPaidSubject() {
		return this.paidSubject;
	}

	public void setPaidSubject(String paidSubject) {
		this.paidSubject = paidSubject;
	}

	public Double getMoney() {
		return this.money;
	}

	public void setMoney(Double money) {
		this.money = money;
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

	public String getAttachment() {
		return this.attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

}