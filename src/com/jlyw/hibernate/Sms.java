package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * Sms entity. @author MyEclipse Persistence Tools
 */

public class Sms implements java.io.Serializable {

	// Fields

	private Integer id;
	private String contents;
	private String number;
	private Integer type;
	private Timestamp createDate;
	private Timestamp sendDate;
	private String receiver;
	private Integer status;

	// Constructors

	/** default constructor */
	public Sms() {
	}

	/** minimal constructor */
	public Sms(String contents, String number, Integer type,
			Timestamp createDate, Integer status) {
		this.contents = contents;
		this.number = number;
		this.type = type;
		this.createDate = createDate;
		this.status = status;
	}

	/** full constructor */
	public Sms(String contents, String number, Integer type,
			Timestamp createDate, Timestamp sendDate, String receiver,
			Integer status) {
		this.contents = contents;
		this.number = number;
		this.type = type;
		this.createDate = createDate;
		this.sendDate = sendDate;
		this.receiver = receiver;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getContents() {
		return this.contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
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

	public Timestamp getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Timestamp sendDate) {
		this.sendDate = sendDate;
	}

	public String getReceiver() {
		return this.receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}