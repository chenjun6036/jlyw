package com.jlyw.hibernate;

/**
 * ProjectTeam entity. @author MyEclipse Persistence Tools
 */

public class ProjectTeam implements java.io.Serializable {

	// Fields

	private Integer id;
	private Department department;
	private String name;
	private String brief;
	private Integer status;
	private String proTeamCode;

	// Constructors

	/** default constructor */
	public ProjectTeam() {
	}

	/** minimal constructor */
	public ProjectTeam(Department department, String name, String brief,
			Integer status) {
		this.department = department;
		this.name = name;
		this.brief = brief;
		this.status = status;
	}

	/** full constructor */
	public ProjectTeam(Department department, String name, String brief,
			Integer status, String proTeamCode) {
		this.department = department;
		this.name = name;
		this.brief = brief;
		this.status = status;
		this.proTeamCode = proTeamCode;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getProTeamCode() {
		return this.proTeamCode;
	}

	public void setProTeamCode(String proTeamCode) {
		this.proTeamCode = proTeamCode;
	}

}