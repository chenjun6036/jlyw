package com.jlyw.hibernate.crm;

import java.util.Date;

import com.jlyw.hibernate.CustomerContactor;

public class Warning {
	private String title;
	private Date validity;
	private int type;
	private int priority;
	private CustomerContactor customerContactor;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public CustomerContactor getCustomerContactor() {
		return customerContactor;
	}
	public void setCustomerContactor(CustomerContactor customerContactor) {
		this.customerContactor = customerContactor;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public Date getValidity() {
		return validity;
	}
	public void setValidity(Date validity) {
		this.validity = validity;
	}
	
}
