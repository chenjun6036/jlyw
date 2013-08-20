package com.jlyw.hibernate.crm;

import com.jlyw.hibernate.Region;

/**
 * PotentialCustomer entity. @author MyEclipse Persistence Tools
 */

public class PotentialCustomer implements java.io.Serializable {

	// Fields

	private Integer id;
	private Region region;
	private String name;
	private String nameEn;
	private String brief;
	private String address;
	private Integer status;
	private Integer potentialCustomerFrom;
	private Integer cooperationIntension;
	private Integer industry;
	private String facilitatingAgency;

	// Constructors

	/** default constructor */
	public PotentialCustomer() {
	}

	/** full constructor */
	public PotentialCustomer(Region region, String name, String nameEn,
			String brief, String address, Integer status,
			Integer potentialCustomerFrom, Integer cooperationIntension,
			Integer industry,String facilitatingAgency) {
		this.region = region;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.address = address;
		this.status = status;
		this.potentialCustomerFrom = potentialCustomerFrom;
		this.cooperationIntension = cooperationIntension;
		this.industry = industry;
		this.facilitatingAgency=facilitatingAgency;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPotentialCustomerFrom() {
		return this.potentialCustomerFrom;
	}

	public void setPotentialCustomerFrom(Integer potentialCustomerFrom) {
		this.potentialCustomerFrom = potentialCustomerFrom;
	}

	public Integer getCooperationIntension() {
		return this.cooperationIntension;
	}

	public void setCooperationIntension(Integer cooperationIntension) {
		this.cooperationIntension = cooperationIntension;
	}

	public Integer getIndustry() {
		return this.industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public String getFacilitatingAgency() {
		return this.facilitatingAgency;
	}
	
	public void setFacilitatingAgency(String facilitatingAgency)
	{
		this.facilitatingAgency=facilitatingAgency;
	}
}