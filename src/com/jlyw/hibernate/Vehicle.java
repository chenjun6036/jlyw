package com.jlyw.hibernate;

/**
 * Vehicle entity. @author MyEclipse Persistence Tools
 */

public class Vehicle implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private String licence;
	private Integer limit;
	private String model;
	private String brand;
	private String licenceType;
	private Double fuelFee;
	private Integer status;

	// Constructors

	/** default constructor */
	public Vehicle() {
	}

	/** minimal constructor */
	public Vehicle(String licence, Integer limit, String model, String brand,
			Double fuelFee, Integer status) {
		this.licence = licence;
		this.limit = limit;
		this.model = model;
		this.brand = brand;
		this.fuelFee = fuelFee;
		this.status = status;
	}

	/** full constructor */
	public Vehicle(SysUser sysUser, String licence, Integer limit,
			String model, String brand, String licenceType, Double fuelFee,
			Integer status) {
		this.sysUser = sysUser;
		this.licence = licence;
		this.limit = limit;
		this.model = model;
		this.brand = brand;
		this.licenceType = licenceType;
		this.fuelFee = fuelFee;
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

	public String getLicence() {
		return this.licence;
	}

	public void setLicence(String licence) {
		this.licence = licence;
	}

	public Integer getLimit() {
		return this.limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getLicenceType() {
		return this.licenceType;
	}

	public void setLicenceType(String licenceType) {
		this.licenceType = licenceType;
	}

	public Double getFuelFee() {
		return this.fuelFee;
	}

	public void setFuelFee(Double fuelFee) {
		this.fuelFee = fuelFee;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}