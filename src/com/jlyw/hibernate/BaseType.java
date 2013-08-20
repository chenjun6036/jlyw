package com.jlyw.hibernate;

/**
 * BaseType entity. @author MyEclipse Persistence Tools
 */

public class BaseType implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String brief;
	private Integer sequence;
	private Integer type;
	private Integer status;
	private String remark;

	// Constructors

	/** default constructor */
	public BaseType() {
	}

	/** minimal constructor */
	public BaseType(String name, String brief, Integer sequence, Integer type,
			Integer status) {
		this.name = name;
		this.brief = brief;
		this.sequence = sequence;
		this.type = type;
		this.status = status;
	}

	/** full constructor */
	public BaseType(String name, String brief, Integer sequence, Integer type,
			Integer status, String remark) {
		this.name = name;
		this.brief = brief;
		this.sequence = sequence;
		this.type = type;
		this.status = status;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getSequence() {
		return this.sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}