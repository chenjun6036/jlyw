package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * LocaleMission entity. @author MyEclipse Persistence Tools
 */

public class LocaleMission implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByCreatorId;
	private SysUser sysUserBySiteManagerId;
	private Region region;
	private SysUser sysUserByModificatorId;
	private Customer customer;
	private String customerName;
	private String brief;
	private String address;
	private String zipCode;
	private String contactor;
	private String tel;
	private String contactorTel;
	private String department;
	private String staffs;
	private Timestamp tentativeDate;
	private Timestamp exactTime;
	private Timestamp modificatorDate;
	private Integer status;
	private String remark;
	private String feedback;
	private Timestamp checkDate;
	private Timestamp createTime;
	private String vehicleLisences;
	private String code;

	// Constructors

	/** default constructor */
	public LocaleMission() {
	}

	/** minimal constructor */
	public LocaleMission(SysUser sysUserByCreatorId, Region region,
			SysUser sysUserByModificatorId, Customer customer,
			String customerName, String brief, String address, String zipCode,
			String contactor, String tel, String contactorTel,
			String department, Timestamp tentativeDate,
			Timestamp modificatorDate, Integer status, Timestamp createTime,
			String code) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.region = region;
		this.sysUserByModificatorId = sysUserByModificatorId;
		this.customer = customer;
		this.customerName = customerName;
		this.brief = brief;
		this.address = address;
		this.zipCode = zipCode;
		this.contactor = contactor;
		this.tel = tel;
		this.contactorTel = contactorTel;
		this.department = department;
		this.tentativeDate = tentativeDate;
		this.modificatorDate = modificatorDate;
		this.status = status;
		this.createTime = createTime;
		this.code = code;
	}

	/** full constructor */
	public LocaleMission(SysUser sysUserByCreatorId,
			SysUser sysUserBySiteManagerId, Region region,
			SysUser sysUserByModificatorId, Customer customer,
			String customerName, String brief, String address, String zipCode,
			String contactor, String tel, String contactorTel,
			String department, String staffs, Timestamp tentativeDate,
			Timestamp exactTime, Timestamp modificatorDate, Integer status,
			String remark, String feedback, Timestamp checkDate,
			Timestamp createTime, String vehicleLisences, String code) {
		this.sysUserByCreatorId = sysUserByCreatorId;
		this.sysUserBySiteManagerId = sysUserBySiteManagerId;
		this.region = region;
		this.sysUserByModificatorId = sysUserByModificatorId;
		this.customer = customer;
		this.customerName = customerName;
		this.brief = brief;
		this.address = address;
		this.zipCode = zipCode;
		this.contactor = contactor;
		this.tel = tel;
		this.contactorTel = contactorTel;
		this.department = department;
		this.staffs = staffs;
		this.tentativeDate = tentativeDate;
		this.exactTime = exactTime;
		this.modificatorDate = modificatorDate;
		this.status = status;
		this.remark = remark;
		this.feedback = feedback;
		this.checkDate = checkDate;
		this.createTime = createTime;
		this.vehicleLisences = vehicleLisences;
		this.code = code;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByCreatorId() {
		return this.sysUserByCreatorId;
	}

	public void setSysUserByCreatorId(SysUser sysUserByCreatorId) {
		this.sysUserByCreatorId = sysUserByCreatorId;
	}

	public SysUser getSysUserBySiteManagerId() {
		return this.sysUserBySiteManagerId;
	}

	public void setSysUserBySiteManagerId(SysUser sysUserBySiteManagerId) {
		this.sysUserBySiteManagerId = sysUserBySiteManagerId;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public SysUser getSysUserByModificatorId() {
		return this.sysUserByModificatorId;
	}

	public void setSysUserByModificatorId(SysUser sysUserByModificatorId) {
		this.sysUserByModificatorId = sysUserByModificatorId;
	}

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getContactor() {
		return this.contactor;
	}

	public void setContactor(String contactor) {
		this.contactor = contactor;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getContactorTel() {
		return this.contactorTel;
	}

	public void setContactorTel(String contactorTel) {
		this.contactorTel = contactorTel;
	}

	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getStaffs() {
		return this.staffs;
	}

	public void setStaffs(String staffs) {
		this.staffs = staffs;
	}

	public Timestamp getTentativeDate() {
		return this.tentativeDate;
	}

	public void setTentativeDate(Timestamp tentativeDate) {
		this.tentativeDate = tentativeDate;
	}

	public Timestamp getExactTime() {
		return this.exactTime;
	}

	public void setExactTime(Timestamp exactTime) {
		this.exactTime = exactTime;
	}

	public Timestamp getModificatorDate() {
		return this.modificatorDate;
	}

	public void setModificatorDate(Timestamp modificatorDate) {
		this.modificatorDate = modificatorDate;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Timestamp getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Timestamp checkDate) {
		this.checkDate = checkDate;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getVehicleLisences() {
		return this.vehicleLisences;
	}

	public void setVehicleLisences(String vehicleLisences) {
		this.vehicleLisences = vehicleLisences;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}