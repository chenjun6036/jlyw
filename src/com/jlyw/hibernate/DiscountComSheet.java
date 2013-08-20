package com.jlyw.hibernate;

/**
 * DiscountComSheet entity. @author MyEclipse Persistence Tools
 */

public class DiscountComSheet implements java.io.Serializable {

	// Fields

	private Integer id;
	private CommissionSheet commissionSheet;
	private Discount discount;
	private Double testFee;
	private Double materialFee;
	private Double repairFee;
	private Double carFee;
	private Double debugFee;
	private Double otherFee;
	private Double totalFee;
	private Double oldTestFee;
	private Double oldMaterialFee;
	private Double oldRepairFee;
	private Double oldCarFee;
	private Double oldDebugFee;
	private Double oldOtherFee;
	private Double oldTotalFee;
	private String remark;

	// Constructors

	/** default constructor */
	public DiscountComSheet() {
	}

	/** minimal constructor */
	public DiscountComSheet(CommissionSheet commissionSheet, Discount discount) {
		this.commissionSheet = commissionSheet;
		this.discount = discount;
	}

	/** full constructor */
	public DiscountComSheet(CommissionSheet commissionSheet, Discount discount,
			Double testFee, Double materialFee, Double repairFee,
			Double carFee, Double debugFee, Double otherFee, Double totalFee,
			Double oldTestFee, Double oldMaterialFee, Double oldRepairFee,
			Double oldCarFee, Double oldDebugFee, Double oldOtherFee,
			Double oldTotalFee, String remark) {
		this.commissionSheet = commissionSheet;
		this.discount = discount;
		this.testFee = testFee;
		this.materialFee = materialFee;
		this.repairFee = repairFee;
		this.carFee = carFee;
		this.debugFee = debugFee;
		this.otherFee = otherFee;
		this.totalFee = totalFee;
		this.oldTestFee = oldTestFee;
		this.oldMaterialFee = oldMaterialFee;
		this.oldRepairFee = oldRepairFee;
		this.oldCarFee = oldCarFee;
		this.oldDebugFee = oldDebugFee;
		this.oldOtherFee = oldOtherFee;
		this.oldTotalFee = oldTotalFee;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public CommissionSheet getCommissionSheet() {
		return this.commissionSheet;
	}

	public void setCommissionSheet(CommissionSheet commissionSheet) {
		this.commissionSheet = commissionSheet;
	}

	public Discount getDiscount() {
		return this.discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
	}

	public Double getTestFee() {
		return this.testFee;
	}

	public void setTestFee(Double testFee) {
		this.testFee = testFee;
	}

	public Double getMaterialFee() {
		return this.materialFee;
	}

	public void setMaterialFee(Double materialFee) {
		this.materialFee = materialFee;
	}

	public Double getRepairFee() {
		return this.repairFee;
	}

	public void setRepairFee(Double repairFee) {
		this.repairFee = repairFee;
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

	public Double getOldTestFee() {
		return this.oldTestFee;
	}

	public void setOldTestFee(Double oldTestFee) {
		this.oldTestFee = oldTestFee;
	}

	public Double getOldMaterialFee() {
		return this.oldMaterialFee;
	}

	public void setOldMaterialFee(Double oldMaterialFee) {
		this.oldMaterialFee = oldMaterialFee;
	}

	public Double getOldRepairFee() {
		return this.oldRepairFee;
	}

	public void setOldRepairFee(Double oldRepairFee) {
		this.oldRepairFee = oldRepairFee;
	}

	public Double getOldCarFee() {
		return this.oldCarFee;
	}

	public void setOldCarFee(Double oldCarFee) {
		this.oldCarFee = oldCarFee;
	}

	public Double getOldDebugFee() {
		return this.oldDebugFee;
	}

	public void setOldDebugFee(Double oldDebugFee) {
		this.oldDebugFee = oldDebugFee;
	}

	public Double getOldOtherFee() {
		return this.oldOtherFee;
	}

	public void setOldOtherFee(Double oldOtherFee) {
		this.oldOtherFee = oldOtherFee;
	}

	public Double getOldTotalFee() {
		return this.oldTotalFee;
	}

	public void setOldTotalFee(Double oldTotalFee) {
		this.oldTotalFee = oldTotalFee;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}