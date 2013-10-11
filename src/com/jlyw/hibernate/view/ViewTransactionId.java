package com.jlyw.hibernate.view;


/**
 * ViewTransactionId entity. @author MyEclipse Persistence Tools
 */

public class ViewTransactionId implements java.io.Serializable {

	// Fields

	private Integer tid;
	private Integer cid;

	// Constructors

	/** default constructor */
	public ViewTransactionId() {
	}

	/** full constructor */
	public ViewTransactionId(Integer tid, Integer cid) {
		this.tid = tid;
		this.cid = cid;
	}


	// Property accessors

	public Integer getTid() {
		return this.tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}
	
	public Integer getCid() {
		return this.cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

}