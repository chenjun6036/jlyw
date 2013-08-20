package com.jlyw.hibernate;

/**
 * VehicleMission entity. @author MyEclipse Persistence Tools
 */

public class VehicleMission implements java.io.Serializable {

	// Fields

	private Integer id;
	private DrivingVehicle drivingVehicle;
	private LocaleMission localeMission;

	// Constructors

	/** default constructor */
	public VehicleMission() {
	}

	/** full constructor */
	public VehicleMission(DrivingVehicle drivingVehicle,
			LocaleMission localeMission) {
		this.drivingVehicle = drivingVehicle;
		this.localeMission = localeMission;
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

	public LocaleMission getLocaleMission() {
		return this.localeMission;
	}

	public void setLocaleMission(LocaleMission localeMission) {
		this.localeMission = localeMission;
	}

}