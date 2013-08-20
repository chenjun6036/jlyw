package com.jlyw.hibernate;

/**
 * TgtAppSpec entity. @author MyEclipse Persistence Tools
 */

public class TgtAppSpec implements java.io.Serializable {

	// Fields

	private Integer id;
	private Specification specification;
	private TargetAppliance targetAppliance;

	// Constructors

	/** default constructor */
	public TgtAppSpec() {
	}

	/** full constructor */
	public TgtAppSpec(Specification specification,
			TargetAppliance targetAppliance) {
		this.specification = specification;
		this.targetAppliance = targetAppliance;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Specification getSpecification() {
		return this.specification;
	}

	public void setSpecification(Specification specification) {
		this.specification = specification;
	}

	public TargetAppliance getTargetAppliance() {
		return this.targetAppliance;
	}

	public void setTargetAppliance(TargetAppliance targetAppliance) {
		this.targetAppliance = targetAppliance;
	}

}