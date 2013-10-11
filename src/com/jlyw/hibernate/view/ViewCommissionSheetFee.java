package com.jlyw.hibernate.view;

/**
 * ViewCommissionSheetFee entity. @author MyEclipse Persistence Tools
 */

public class ViewCommissionSheetFee implements java.io.Serializable {

	// Fields

	private ViewCommissionSheetFeeId id;
	private Double testFee;
	private Double repairFee;
	private Double materialFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;

	// Constructors

	/** default constructor */
	public ViewCommissionSheetFee() {
	}

	/** full constructor */
	public ViewCommissionSheetFee(ViewCommissionSheetFeeId id) {
		this.id = id;
	}

	// Property accessors

	public ViewCommissionSheetFeeId getId() {
		return this.id;
	}

	public void setId(ViewCommissionSheetFeeId id) {
		this.id = id;
	}


	public Double getTestFee() {
		return this.testFee;
	}

	public void setTestFee(Double testFee) {
		this.testFee = testFee;
	}

	public Double getRepairFee() {
		return this.repairFee;
	}

	public void setRepairFee(Double repairFee) {
		this.repairFee = repairFee;
	}

	public Double getMaterialFee() {
		return this.materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public Double getCarFee() {
		return this.carFee;
	}

	public void setCarFee(Double carFee) {
		this.carFee = carFee;
	}

	public Double getDebugFee() {
		return this.debugFee;
	}

	public void setDebugFee(Double debugFee) {
		this.debugFee = debugFee;
	}

	public Double getOtherFee() {
		return this.otherFee;
	}

	public void setOtherFee(Double otherFee) {
		this.otherFee = otherFee;
	}

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
}