package com.jlyw.hibernate;

/**
 * UserProfile entity. @author MyEclipse Persistence Tools
 */

public class UserProfile implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUser;
	private Integer type;
	private String f1;
	private String f2;
	private String f3;
	private String f4;

	// Constructors

	/** default constructor */
	public UserProfile() {
	}

	/** minimal constructor */
	public UserProfile(SysUser sysUser, Integer type) {
		this.sysUser = sysUser;
		this.type = type;
	}

	/** full constructor */
	public UserProfile(SysUser sysUser, Integer type, String f1, String f2,
			String f3, String f4) {
		this.sysUser = sysUser;
		this.type = type;
		this.f1 = f1;
		this.f2 = f2;
		this.f3 = f3;
		this.f4 = f4;
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

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getF1() {
		return this.f1;
	}

	public void setF1(String f1) {
		this.f1 = f1;
	}

	public String getF2() {
		return this.f2;
	}

	public void setF2(String f2) {
		this.f2 = f2;
	}

	public String getF3() {
		return this.f3;
	}

	public void setF3(String f3) {
		this.f3 = f3;
	}

	public String getF4() {
		return this.f4;
	}

	public void setF4(String f4) {
		this.f4 = f4;
	}

}