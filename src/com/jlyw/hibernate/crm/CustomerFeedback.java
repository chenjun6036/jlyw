package com.jlyw.hibernate.crm;

import java.sql.Timestamp;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SysUser;

/**
 * CustomerFeedback entity. @author MyEclipse Persistence Tools
 */

public class CustomerFeedback implements java.io.Serializable {

	// Fields

	private Integer id;
	private Customer customer;
	private SysUser sysUserByCreateSysUserId;
	private SysUser sysUserByHandleSysUserId;
	private Timestamp createTime;
	private Integer complainAbout;
	private String customerContactorName;
	private Integer handleLevel;
	private String feedback;
	private Integer status;
	private String method;
	private String remark;
	private Timestamp planStartTime;
	private Timestamp planEndTime;
	private Timestamp actulStartTime;
	private Timestamp actulEndTime;
	private Timestamp customerRequiredTime;
	private Integer returnVisitType;
	private String returnVisitInfo;
	private Integer mark;
	private String analysis;

	// Constructors

	/** default constructor */
	public CustomerFeedback() {
	}

	/** minimal constructor */
	public CustomerFeedback(Customer customer,
			SysUser sysUserByCreateSysUserId, Timestamp createTime,
			Integer complainAbout, String customerContactorName,
			Integer handleLevel, String feedback, Integer status) {
		this.customer = customer;
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.createTime = createTime;
		this.complainAbout = complainAbout;
		this.customerContactorName = customerContactorName;
		this.handleLevel = handleLevel;
		this.feedback = feedback;
		this.status = status;
	}

	/** full constructor */
	public CustomerFeedback(Customer customer,
			SysUser sysUserByCreateSysUserId, SysUser sysUserByHandleSysUserId,
			Timestamp createTime, Integer complainAbout,
			String customerContactorName, Integer handleLevel, String feedback,
			Integer status, String method, String remark,
			Timestamp planStartTime, Timestamp planEndTime,
			Timestamp actulStartTime, Timestamp actulEndTime,
			Timestamp customerRequiredTime, Integer returnVisitType,
			String returnVisitInfo, Integer mark, String analysis) {
		this.customer = customer;
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
		this.sysUserByHandleSysUserId = sysUserByHandleSysUserId;
		this.createTime = createTime;
		this.complainAbout = complainAbout;
		this.customerContactorName = customerContactorName;
		this.handleLevel = handleLevel;
		this.feedback = feedback;
		this.status = status;
		this.method = method;
		this.remark = remark;
		this.planStartTime = planStartTime;
		this.planEndTime = planEndTime;
		this.actulStartTime = actulStartTime;
		this.actulEndTime = actulEndTime;
		this.customerRequiredTime = customerRequiredTime;
		this.returnVisitType = returnVisitType;
		this.returnVisitInfo = returnVisitInfo;
		this.mark = mark;
		this.analysis = analysis;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public SysUser getSysUserByCreateSysUserId() {
		return this.sysUserByCreateSysUserId;
	}

	public void setSysUserByCreateSysUserId(SysUser sysUserByCreateSysUserId) {
		this.sysUserByCreateSysUserId = sysUserByCreateSysUserId;
	}

	public SysUser getSysUserByHandleSysUserId() {
		return this.sysUserByHandleSysUserId;
	}

	public void setSysUserByHandleSysUserId(SysUser sysUserByHandleSysUserId) {
		this.sysUserByHandleSysUserId = sysUserByHandleSysUserId;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getComplainAbout() {
		return this.complainAbout;
	}

	public void setComplainAbout(Integer complainAbout) {
		this.complainAbout = complainAbout;
	}

	public String getCustomerContactorName() {
		return this.customerContactorName;
	}

	public void setCustomerContactorName(String customerContactorName) {
		this.customerContactorName = customerContactorName;
	}

	public Integer getHandleLevel() {
		return this.handleLevel;
	}

	public void setHandleLevel(Integer handleLevel) {
		this.handleLevel = handleLevel;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getPlanStartTime() {
		return this.planStartTime;
	}

	public void setPlanStartTime(Timestamp planStartTime) {
		this.planStartTime = planStartTime;
	}

	public Timestamp getPlanEndTime() {
		return this.planEndTime;
	}

	public void setPlanEndTime(Timestamp planEndTime) {
		this.planEndTime = planEndTime;
	}

	public Timestamp getActulStartTime() {
		return this.actulStartTime;
	}

	public void setActulStartTime(Timestamp actulStartTime) {
		this.actulStartTime = actulStartTime;
	}

	public Timestamp getActulEndTime() {
		return this.actulEndTime;
	}

	public void setActulEndTime(Timestamp actulEndTime) {
		this.actulEndTime = actulEndTime;
	}

	public Timestamp getCustomerRequiredTime() {
		return this.customerRequiredTime;
	}

	public void setCustomerRequiredTime(Timestamp customerRequiredTime) {
		this.customerRequiredTime = customerRequiredTime;
	}

	public Integer getReturnVisitType() {
		return this.returnVisitType;
	}

	public void setReturnVisitType(Integer returnVisitType) {
		this.returnVisitType = returnVisitType;
	}

	public String getReturnVisitInfo() {
		return this.returnVisitInfo;
	}

	public void setReturnVisitInfo(String returnVisitInfo) {
		this.returnVisitInfo = returnVisitInfo;
	}

	public Integer getMark() {
		return this.mark;
	}

	public void setMark(Integer mark) {
		this.mark = mark;
	}

	public String getAnalysis() {
		return this.analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

}