package com.jlyw.hibernate;

/**
 * VehicleFee entity. @author MyEclipse Persistence Tools
 */

public class VehicleFee implements java.io.Serializable {

	// Fields

	private Integer id;
	private DrivingVehicle drivingVehicle;
	private SysUser sysUser;
	private Double fee;
	private String remark;

	// Constructors

	/** default constructor */
	public VehicleFee() {
	}

	/** minimal constructor */
	public VehicleFee(DrivingVehicle drivingVehicle, SysUser sysUser, Double fee) {
		this.drivingVehicle = drivingVehicle;
		this.sysUser = sysUser;
		this.fee = fee;
	}

	/** full constructor */
	public VehicleFee(DrivingVehicle drivingVehicle, SysUser sysUser,
			Double fee, String remark) {
		this.drivingVehicle = drivingVehicle;
		this.sysUser = sysUser;
		this.fee = fee;
		this.remark = remark;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public DrivingVehicle getDrivingVehicle() {
		return this.drivingVehicle;
	}

	public void setDrivingVehicle(DrivingVehicle drivingVehicle) {
		this.drivingVehicle = drivingVehicle;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}