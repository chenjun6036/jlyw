package com.jlyw.hibernate.view;

/**
 * ViewApplianceSpecialStandardNamePopularNameId entity. @author MyEclipse
 * Persistence Tools
 */

public class ViewApplianceSpecialStandardNamePopularNameId implements
		java.io.Serializable {

	// Fields

	private Integer id;
	private Integer type;
	

	// Constructors

	/** default constructor */
	public ViewApplianceSpecialStandardNamePopularNameId() {
	}

	/** full constructor */
	public ViewApplianceSpecialStandardNamePopularNameId(Integer id,
			Integer type) {
		this.id = id;
		this.type = type;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}