package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * AuthBackgroundRuning entity. @author MyEclipse Persistence Tools
 */

public class AuthBackgroundRuning implements java.io.Serializable {

	// Fields

	private Integer id;
	private VerifyAndAuthorize verifyAndAuthorize;
	private Timestamp createTime;

	// Constructors

	/** default constructor */
	public AuthBackgroundRuning() {
	}

	/** full constructor */
	public AuthBackgroundRuning(VerifyAndAuthorize verifyAndAuthorize,
			Timestamp createTime) {
		this.verifyAndAuthorize = verifyAndAuthorize;
		this.createTime = createTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VerifyAndAuthorize getVerifyAndAuthorize() {
		return this.verifyAndAuthorize;
	}

	public void setVerifyAndAuthorize(VerifyAndAuthorize verifyAndAuthorize) {
		this.verifyAndAuthorize = verifyAndAuthorize;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}