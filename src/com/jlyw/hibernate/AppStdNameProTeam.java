package com.jlyw.hibernate;

/**
 * AppStdNameProTeam entity. @author MyEclipse Persistence Tools
 */

public class AppStdNameProTeam implements java.io.Serializable {

	// Fields

	private Integer id;
	private ApplianceStandardName applianceStandardName;
	private ProjectTeam projectTeam;
	private String projectName;
	private Integer status;

	// Constructors

	/** default constructor */
	public AppStdNameProTeam() {
	}

	/** minimal constructor */
	public AppStdNameProTeam(ApplianceStandardName applianceStandardName,
			Integer status) {
		this.applianceStandardName = applianceStandardName;
		this.status = status;
	}

	/** full constructor */
	public AppStdNameProTeam(ApplianceStandardName applianceStandardName,
			ProjectTeam projectTeam, String projectName, Integer status) {
		this.applianceStandardName = applianceStandardName;
		this.projectTeam = projectTeam;
		this.projectName = projectName;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ApplianceStandardName getApplianceStandardName() {
		return this.applianceStandardName;
	}

	public void setApplianceStandardName(
			ApplianceStandardName applianceStandardName) {
		this.applianceStandardName = applianceStandardName;
	}

	public ProjectTeam getProjectTeam() {
		return this.projectTeam;
	}

	public void setProjectTeam(ProjectTeam projectTeam) {
		this.projectTeam = projectTeam;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}