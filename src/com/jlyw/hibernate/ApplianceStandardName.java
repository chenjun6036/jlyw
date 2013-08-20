package com.jlyw.hibernate;

/**
 * ApplianceStandardName entity. @author MyEclipse Persistence Tools
 */

public class ApplianceStandardName implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceSpecies applianceSpecies;
	private String name;
	private String nameEn;
	private String brief;
	private Boolean holdMany;
	private Integer status;
	private String remark1;
	private String remark2;
	private String remark3;
	private String filesetName;

	// Constructors

	/** default constructor */
	public ApplianceStandardName() {
	}

	/** minimal constructor */
	public ApplianceStandardName(ApplianceSpecies applianceSpecies,
			String name, String nameEn, String brief, Boolean holdMany,
			Integer status, String filesetName) {
		this.applianceSpecies = applianceSpecies;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.holdMany = holdMany;
		this.status = status;
		this.filesetName = filesetName;
	}

	/** full constructor */
	public ApplianceStandardName(ApplianceSpecies applianceSpecies,
			String name, String nameEn, String brief, Boolean holdMany,
			Integer status, String remark1, String remark2, String remark3,
			String filesetName) {
		this.applianceSpecies = applianceSpecies;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.holdMany = holdMany;
		this.status = status;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.remark3 = remark3;
		this.filesetName = filesetName;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplianceSpecies getApplianceSpecies() {
		return this.applianceSpecies;
	}

	public void setApplianceSpecies(ApplianceSpecies applianceSpecies) {
		this.applianceSpecies = applianceSpecies;
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

	public Boolean getHoldMany() {
		return this.holdMany;
	}

	public void setHoldMany(Boolean holdMany) {
		this.holdMany = holdMany;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark1() {
		return this.remark1;
	}

	public void setRemark1(String remark1) {
		this.remark1 = remark1;
	}

	public String getRemark2() {
		return this.remark2;
	}

	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}

	public String getRemark3() {
		return this.remark3;
	}

	public void setRemark3(String remark3) {
		this.remark3 = remark3;
	}

	public String getFilesetName() {
		return this.filesetName;
	}

	public void setFilesetName(String filesetName) {
		this.filesetName = filesetName;
	}

}