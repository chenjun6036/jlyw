package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * SharingFolder entity. @author MyEclipse Persistence Tools
 */

public class SharingFolder implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private SharingFolder sharingFolder;   //¸¸ÎÄ¼þ¼Ð
	private String name;
	private Timestamp createTime;
	private String filesetName;
	private Integer status;

	// Constructors

	/** default constructor */
	public SharingFolder() {
	}

	/** minimal constructor */
	public SharingFolder(String name, Timestamp createTime, String filesetName,
			Integer status) {
		this.name = name;
		this.createTime = createTime;
		this.filesetName = filesetName;
		this.status = status;
	}

	/** full constructor */
	public SharingFolder(SysUser sysUser, SharingFolder sharingFolder,
			String name, Timestamp createTime, String filesetName,
			Integer status) {
		this.sysUser = sysUser;
		this.sharingFolder = sharingFolder;
		this.name = name;
		this.createTime = createTime;
		this.filesetName = filesetName;
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

	public SharingFolder getSharingFolder() {
		return this.sharingFolder;
	}

	public void setSharingFolder(SharingFolder sharingFolder) {
		this.sharingFolder = sharingFolder;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getFilesetName() {
		return this.filesetName;
	}

	public void setFilesetName(String filesetName) {
		this.filesetName = filesetName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}