package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Information entity. @author MyEclipse Persistence Tools
 */

public class Information implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String contents;
	private Integer type;
	private Timestamp createDate;
	private String url;
	private Integer status;

	// Constructors

	/** default constructor */
	public Information() {
	}

	/** minimal constructor */
	public Information(String contents, Integer type, Timestamp createDate,
			Integer status) {
		this.contents = contents;
		this.type = type;
		this.createDate = createDate;
		this.status = status;
	}

	/** full constructor */
	public Information(SysUser sysUser, String contents, Integer type,
			Timestamp createDate, String url, Integer status) {
		this.sysUser = sysUser;
		this.contents = contents;
		this.type = type;
		this.createDate = createDate;
		this.url = url;
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

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Timestamp getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}