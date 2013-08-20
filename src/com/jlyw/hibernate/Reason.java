package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Reason entity. @author MyEclipse Persistence Tools
 */

public class Reason implements java.io.Serializable {

	// Fields

	private Integer id;
	private String reason;
	private Integer type;
	private Timestamp lastUse;
	private Integer count;
	private Integer status;

	// Constructors

	/** default constructor */
	public Reason() {
	}

	/** full constructor */
	public Reason(String reason, Integer type, Timestamp lastUse,
			Integer count, Integer status) {
		this.reason = reason;
		this.type = type;
		this.lastUse = lastUse;
		this.count = count;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Timestamp getLastUse() {
		return this.lastUse;
	}

	public void setLastUse(Timestamp lastUse) {
		this.lastUse = lastUse;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}