package com.jlyw.hibernate.view;

/**
 * ViewCommissionSheetFeeId entity. @author MyEclipse Persistence Tools
 */

public class ViewCommissionSheetFeeId implements java.io.Serializable {

	// Fields

	private Integer commissionSheetId;

	// Constructors

	/** default constructor */
	public ViewCommissionSheetFeeId() {
	}

	/** minimal constructor */
	public ViewCommissionSheetFeeId(Integer commissionSheetId) {
		this.commissionSheetId = commissionSheetId;
	}

	// Property accessors

	public Integer getCommissionSheetId() {
		return this.commissionSheetId;
	}

	public void setCommissionSheetId(Integer commissionSheetId) {
		this.commissionSheetId = commissionSheetId;
	}
}