package com.jlyw.hibernate;

import java.sql.Timestamp;

/**
 * DrivingVehicle entity. @author MyEclipse Persistence Tools
 */

public class DrivingVehicle implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserBySettlementId;
	private Vehicle vehicle;
	private SysUser sysUserByDriverId;
	private Double kilometers;
	private Timestamp beginDate;
	private Timestamp endDate;
	private String description;
	private String people;
	private String assemblingPlace;
	private Double totalFee;
	private Integer status;
	private Timestamp settlementTime;

	// Constructors

	/** default constructor */
	public DrivingVehicle() {
	}

	/** minimal constructor */
	public DrivingVehicle(Vehicle vehicle, SysUser sysUserByDriverId,
			Timestamp beginDate, Timestamp endDate, Integer status) {
		this.vehicle = vehicle;
		this.sysUserByDriverId = sysUserByDriverId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.status = status;
	}

	/** full constructor */
	public DrivingVehicle(SysUser sysUserBySettlementId, Vehicle vehicle,
			SysUser sysUserByDriverId, Double kilometers, Timestamp beginDate,
			Timestamp endDate, String description, String people,
			String assemblingPlace, Double totalFee, Integer status,
			Timestamp settlementTime) {
		this.sysUserBySettlementId = sysUserBySettlementId;
		this.vehicle = vehicle;
		this.sysUserByDriverId = sysUserByDriverId;
		this.kilometers = kilometers;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.description = description;
		this.people = people;
		this.assemblingPlace = assemblingPlace;
		this.totalFee = totalFee;
		this.status = status;
		this.settlementTime = settlementTime;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserBySettlementId() {
		return this.sysUserBySettlementId;
	}

	public void setSysUserBySettlementId(SysUser sysUserBySettlementId) {
		this.sysUserBySettlementId = sysUserBySettlementId;
	}

	public Vehicle getVehicle() {
		return this.vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public SysUser getSysUserByDriverId() {
		return this.sysUserByDriverId;
	}

	public void setSysUserByDriverId(SysUser sysUserByDriverId) {
		this.sysUserByDriverId = sysUserByDriverId;
	}

	public Double getKilometers() {
		return this.kilometers;
	}

	public void setKilometers(Double kilometers) {
		this.kilometers = kilometers;
	}

	public Timestamp getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Timestamp beginDate) {
		this.beginDate = beginDate;
	}

	public Timestamp getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPeople() {
		return this.people;
	}

	public void setPeople(String people) {
		this.people = people;
	}

	public String getAssemblingPlace() {
		return this.assemblingPlace;
	}

	public void setAssemblingPlace(String assemblingPlace) {
		this.assemblingPlace = assemblingPlace;
	}

	public Double getTotalFee() {
		return this.totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getSettlementTime() {
		return this.settlementTime;
	}

	public void setSettlementTime(Timestamp settlementTime) {
		this.settlementTime = settlementTime;
	}

}