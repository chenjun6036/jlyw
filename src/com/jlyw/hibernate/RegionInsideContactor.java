package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * RegionInsideContactor entity. @author MyEclipse Persistence Tools
 */

public class RegionInsideContactor implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private Region region;
	private Timestamp lastEditTime;
	private Integer status;

	// Constructors

	/** default constructor */
	public RegionInsideContactor() {
	}

	/** full constructor */
	public RegionInsideContactor(SysUser sysUser, Region region,
			Timestamp lastEditTime, Integer status) {
		this.sysUser = sysUser;
		this.region = region;
		this.lastEditTime = lastEditTime;
		this.status = status;
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

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Timestamp getLastEditTime() {
		return this.lastEditTime;
	}

	public void setLastEditTime(Timestamp lastEditTime) {
		this.lastEditTime = lastEditTime;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}