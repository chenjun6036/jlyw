package com.jlyw.hibernate;

import java.sql.Date;

/**
 * UserQual entity. @author MyEclipse Persistence Tools
 */

public class UserQual implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String type;
	private String qualNum;
	private String authItems;
	private Date authDate;
	private Date expiration;
	private String authDept;
	private String remark;
	private Integer warnSlot;

	// Constructors

	/** default constructor */
	public UserQual() {
	}

	/** minimal constructor */
	public UserQual(SysUser sysUser, String type) {
		this.sysUser = sysUser;
		this.type = type;
	}

	/** full constructor */
	public UserQual(SysUser sysUser, String type, String qualNum,
			String authItems, Date authDate, Date expiration, String authDept,
			String remark, Integer warnSlot) {
		this.sysUser = sysUser;
		this.type = type;
		this.qualNum = qualNum;
		this.authItems = authItems;
		this.authDate = authDate;
		this.expiration = expiration;
		this.authDept = authDept;
		this.remark = remark;
		this.warnSlot = warnSlot;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQualNum() {
		return this.qualNum;
	}

	public void setQualNum(String qualNum) {
		this.qualNum = qualNum;
	}

	public String getAuthItems() {
		return this.authItems;
	}

	public void setAuthItems(String authItems) {
		this.authItems = authItems;
	}

	public Date getAuthDate() {
		return this.authDate;
	}

	public void setAuthDate(Date authDate) {
		this.authDate = authDate;
	}

	public Date getExpiration() {
		return this.expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public String getAuthDept() {
		return this.authDept;
	}

	public void setAuthDept(String authDept) {
		this.authDept = authDept;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getWarnSlot() {
		return this.warnSlot;
	}

	public void setWarnSlot(Integer warnSlot) {
		this.warnSlot = warnSlot;
	}

}