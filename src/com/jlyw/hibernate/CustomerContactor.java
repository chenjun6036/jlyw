package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * CustomerContactor entity. @author MyEclipse Persistence Tools
 */

public class CustomerContactor implements java.io.Serializable {

	// Fields

	private Integer id;
	private Customer customer;
	private String name;
	private String cellphone1;
	private String cellphone2;
	private String email;
	private Timestamp lastUse;
	private Integer count;
	private String remark;
	private Timestamp birthday;
	private Integer status;
	private String formerDep;
	private String formerJob;
	private String curDep;
	private String curJob;

	// Constructors

	/** default constructor */
	public CustomerContactor() {
	}

	/** minimal constructor */
	public CustomerContactor(Customer customer, String name, String cellphone1,
			Timestamp lastUse, Integer count) {
		this.customer = customer;
		this.name = name;
		this.cellphone1 = cellphone1;
		this.lastUse = lastUse;
		this.count = count;
	}

	/** full constructor */
	public CustomerContactor(Customer customer, String name, String cellphone1,
			String cellphone2, String email, Timestamp lastUse, Integer count,
			String remark, Timestamp birthday, Integer status,
			String formerDep, String formerJob, String curDep, String curJob) {
		this.customer = customer;
		this.name = name;
		this.cellphone1 = cellphone1;
		this.cellphone2 = cellphone2;
		this.email = email;
		this.lastUse = lastUse;
		this.count = count;
		this.remark = remark;
		this.birthday = birthday;
		this.status = status;
		this.formerDep = formerDep;
		this.formerJob = formerJob;
		this.curDep = curDep;
		this.curJob = curJob;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCellphone1() {
		return this.cellphone1;
	}

	public void setCellphone1(String cellphone1) {
		this.cellphone1 = cellphone1;
	}

	public String getCellphone2() {
		return this.cellphone2;
	}

	public void setCellphone2(String cellphone2) {
		this.cellphone2 = cellphone2;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Timestamp getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Timestamp birthday) {
		this.birthday = birthday;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getFormerDep() {
		return this.formerDep;
	}

	public void setFormerDep(String formerDep) {
		this.formerDep = formerDep;
	}

	public String getFormerJob() {
		return this.formerJob;
	}

	public void setFormerJob(String formerJob) {
		this.formerJob = formerJob;
	}

	public String getCurDep() {
		return this.curDep;
	}

	public void setCurDep(String curDep) {
		this.curDep = curDep;
	}

	public String getCurJob() {
		return this.curJob;
	}

	public void setCurJob(String curJob) {
		this.curJob = curJob;
	}

}