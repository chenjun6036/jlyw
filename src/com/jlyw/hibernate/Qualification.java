package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Qualification entity. @author MyEclipse Persistence Tools
 */

public class Qualification implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByLastEditorId;
	private SysUser sysUserByUserId;
	private Integer type;
	private Integer authItemId;
	private Integer authItemType;
	private Timestamp lastEditorTime;
	private Integer status;

	// Constructors

	/** default constructor */
	public Qualification() {
	}

	/** minimal constructor */
	public Qualification(SysUser sysUserByUserId, Integer type,
			Integer authItemId, Integer authItemType, Timestamp lastEditorTime,
			Integer status) {
		this.sysUserByUserId = sysUserByUserId;
		this.type = type;
		this.authItemId = authItemId;
		this.authItemType = authItemType;
		this.lastEditorTime = lastEditorTime;
		this.status = status;
	}

	/** full constructor */
	public Qualification(SysUser sysUserByLastEditorId,
			SysUser sysUserByUserId, Integer type, Integer authItemId,
			Integer authItemType, Timestamp lastEditorTime, Integer status) {
		this.sysUserByLastEditorId = sysUserByLastEditorId;
		this.sysUserByUserId = sysUserByUserId;
		this.type = type;
		this.authItemId = authItemId;
		this.authItemType = authItemType;
		this.lastEditorTime = lastEditorTime;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByLastEditorId() {
		return this.sysUserByLastEditorId;
	}

	public void setSysUserByLastEditorId(SysUser sysUserByLastEditorId) {
		this.sysUserByLastEditorId = sysUserByLastEditorId;
	}

	public SysUser getSysUserByUserId() {
		return this.sysUserByUserId;
	}

	public void setSysUserByUserId(SysUser sysUserByUserId) {
		this.sysUserByUserId = sysUserByUserId;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getAuthItemId() {
		return this.authItemId;
	}

	public void setAuthItemId(Integer authItemId) {
		this.authItemId = authItemId;
	}

	public Integer getAuthItemType() {
		return this.authItemType;
	}

	public void setAuthItemType(Integer authItemType) {
		this.authItemType = authItemType;
	}

	public Timestamp getLastEditorTime() {
		return this.lastEditorTime;
	}

	public void setLastEditorTime(Timestamp lastEditorTime) {
		this.lastEditorTime = lastEditorTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}