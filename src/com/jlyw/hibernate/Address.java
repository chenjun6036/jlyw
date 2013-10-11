package com.jlyw.hibernate;

/**
 * Address entity. @author MyEclipse Persistence Tools
 */

public class Address implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String address;
	private String brief;
	private Integer status;
	private String tel;
	private String headName;
	private String headNameEn;
	private String fax;
	private String zipCode;
	private String addressEn;
	private String complainTel;
	private String webSite;
	private String authorizationStatement;
	private String authorizationStatementEn;
	private String cnasstatement;
	private String cnasstatementEn;
	private String standardStatement;
	private String standardStatementEn;

	// Constructors

	/** default constructor */
	public Address() {
	}

	/** minimal constructor */
	public Address(String name, String address, String brief, Integer status,
			String headName) {
		this.name = name;
		this.address = address;
		this.brief = brief;
		this.status = status;
		this.headName = headName;
	}

	/** full constructor */
	public Address(String name, String address, String brief, Integer status,
			String tel, String headName, String headNameEn, String fax,
			String zipCode, String addressEn, String complainTel,
			String webSite, String authorizationStatement,
			String authorizationStatementEn, String cnasstatement,
			String cnasstatementEn, String standardStatement,
			String standardStatementEn) {
		this.name = name;
		this.address = address;
		this.brief = brief;
		this.status = status;
		this.tel = tel;
		this.headName = headName;
		this.headNameEn = headNameEn;
		this.fax = fax;
		this.zipCode = zipCode;
		this.addressEn = addressEn;
		this.complainTel = complainTel;
		this.webSite = webSite;
		this.authorizationStatement = authorizationStatement;
		this.authorizationStatementEn = authorizationStatementEn;
		this.cnasstatement = cnasstatement;
		this.cnasstatementEn = cnasstatementEn;
		this.standardStatement = standardStatement;
		this.standardStatementEn = standardStatementEn;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getHeadName() {
		return this.headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	public String getHeadNameEn() {
		return this.headNameEn;
	}

	public void setHeadNameEn(String headNameEn) {
		this.headNameEn = headNameEn;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getAddressEn() {
		return this.addressEn;
	}

	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
	}

	public String getComplainTel() {
		return this.complainTel;
	}

	public void setComplainTel(String complainTel) {
		this.complainTel = complainTel;
	}

	public String getWebSite() {
		return this.webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getAuthorizationStatement() {
		return this.authorizationStatement;
	}

	public void setAuthorizationStatement(String authorizationStatement) {
		this.authorizationStatement = authorizationStatement;
	}

	public String getAuthorizationStatementEn() {
		return this.authorizationStatementEn;
	}

	public void setAuthorizationStatementEn(String authorizationStatementEn) {
		this.authorizationStatementEn = authorizationStatementEn;
	}

	public String getCnasstatement() {
		return this.cnasstatement;
	}

	public void setCnasstatement(String cnasstatement) {
		this.cnasstatement = cnasstatement;
	}

	public String getCnasstatementEn() {
		return this.cnasstatementEn;
	}

	public void setCnasstatementEn(String cnasstatementEn) {
		this.cnasstatementEn = cnasstatementEn;
	}

	public String getStandardStatement() {
		return this.standardStatement;
	}

	public void setStandardStatement(String standardStatement) {
		this.standardStatement = standardStatement;
	}

	public String getStandardStatementEn() {
		return this.standardStatementEn;
	}

	public void setStandardStatementEn(String standardStatementEn) {
		this.standardStatementEn = standardStatementEn;
	}

}