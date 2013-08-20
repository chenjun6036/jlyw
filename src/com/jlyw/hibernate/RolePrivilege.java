package com.jlyw.hibernate;

/**
 * RolePrivilege entity. @author MyEclipse Persistence Tools
 */

public class RolePrivilege implements java.io.Serializable {

	// Fields

	private Integer id;
	private Role role;
	private Privilege privilege;
	private Integer status;

	// Constructors

	/** default constructor */
	public RolePrivilege() {
	}

	/** full constructor */
	public RolePrivilege(Role role, Privilege privilege, Integer status) {
		this.role = role;
		this.privilege = privilege;
		this.status = status;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Privilege getPrivilege() {
		return this.privilege;
	}

	public void setPrivilege(Privilege privilege) {
		this.privilege = privilege;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}