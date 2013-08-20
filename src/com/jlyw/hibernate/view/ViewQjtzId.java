package com.jlyw.hibernate.view;

import java.sql.Timestamp;

/**
 * ViewQjtzId entity. @author MyEclipse Persistence Tools
 */

public class ViewQjtzId implements java.io.Serializable {

	// Fields


	private Integer id;


	// Constructors

	/** default constructor */
	public ViewQjtzId() {
	}

	
	public ViewQjtzId(Integer id) {
		super();
		this.id = id;
	}


	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}