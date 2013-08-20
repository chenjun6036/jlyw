package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.Customer;
import com.jlyw.hibernate.SysUser;

/**
 * InsideContactor entity. @author MyEclipse Persistence Tools
 */

public class InsideContactor implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private Customer customer;
	private Integer role;

	// Constructors

	/** default constructor */
	public InsideContactor() {
	}

	/** minimal constructor */
	public InsideContactor(SysUser sysUser, Customer customer) {
		this.sysUser = sysUser;
		this.customer = customer;
	}

	/** full constructor */
	public InsideContactor(SysUser sysUser, Customer customer, Integer role) {
		this.sysUser = sysUser;
		this.customer = customer;
		this.role = role;
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

	public Customer getCustomer() {
		return this.customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getRole() {
		return this.role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

}