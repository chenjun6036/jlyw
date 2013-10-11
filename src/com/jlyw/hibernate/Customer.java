package com.jlyw.hibernate;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

/**
 * Customer entity. @author MyEclipse Persistence Tools
 */

public class Customer implements java.io.Serializable {

	// Fields

	private Integer id;
	private SysUser sysUserByInsideContactorId;
	private Region region;
	private SysUser sysUserByModificatorId;
	private Reason reason;
	private String name;
	private String nameEn;
	private String brief;
	private Integer customerType;
	private String code;
	private String address;
	private String addressEn;
	private String tel;
	private String fax;
	private String zipCode;
	private String remark;
	private Integer status;
	private Timestamp cancelDate;
	private Double balance;
	private String account;
	private String classification;
	private String fieldDemands;
	private String certificateDemands;
	private String specialDemands;
	private Double creditAmount;
	private Timestamp modifyDate;
	private String accountBank;
	private Set contactors = new HashSet(0);
	private Integer payVia;
	private Integer payType;
	private Integer accountCycle;
	private Integer customerValueLevel;
	private Integer customerLevel;
	private Integer trendency;
	private Double output;
	private Double outputExpectation;
	private Double serviceFeeLimitation;
	private Integer industry;
	private Integer loyalty;
	private Integer satisfaction;

	// Constructors

	/** default constructor */
	public Customer() {
	}

	/** minimal constructor */
	public Customer(Region region, SysUser sysUserByModificatorId, String name,
			String brief, Integer customerType, String code, String address,
			String tel, String zipCode, Integer status, Double balance,
			String account, String classification, Double creditAmount,
			Timestamp modifyDate, String accountBank) {
		this.region = region;
		this.sysUserByModificatorId = sysUserByModificatorId;
		this.name = name;
		this.brief = brief;
		this.customerType = customerType;
		this.code = code;
		this.address = address;
		this.tel = tel;
		this.zipCode = zipCode;
		this.status = status;
		this.balance = balance;
		this.account = account;
		this.classification = classification;
		this.creditAmount = creditAmount;
		this.modifyDate = modifyDate;
		this.accountBank = accountBank;
	}

	/** full constructor */
	public Customer(SysUser sysUserByInsideContactorId, Region region,
			SysUser sysUserByModificatorId, Reason reason, String name,
			String nameEn, String brief, Integer customerType, String code,
			String address, String addressEn, String tel, String fax,
			String zipCode, String remark, Integer status,
			Timestamp cancelDate, Double balance, String account,
			String classification, String fieldDemands,
			String certificateDemands, String specialDemands,
			Double creditAmount, Timestamp modifyDate, String accountBank,
			Integer payVia, Integer payType, Integer accountCycle,
			Integer customerValueLevel, Integer customerLevel,
			Integer trendency, Double output, Double outputExpectation,
			Double serviceFeeLimitation, Integer industry,Integer loyalty,Integer satisfaction) {
		this.sysUserByInsideContactorId = sysUserByInsideContactorId;
		this.region = region;
		this.sysUserByModificatorId = sysUserByModificatorId;
		this.reason = reason;
		this.name = name;
		this.nameEn = nameEn;
		this.brief = brief;
		this.customerType = customerType;
		this.code = code;
		this.address = address;
		this.addressEn = addressEn;
		this.tel = tel;
		this.fax = fax;
		this.zipCode = zipCode;
		this.remark = remark;
		this.status = status;
		this.cancelDate = cancelDate;
		this.balance = balance;
		this.account = account;
		this.classification = classification;
		this.fieldDemands = fieldDemands;
		this.certificateDemands = certificateDemands;
		this.specialDemands = specialDemands;
		this.creditAmount = creditAmount;
		this.modifyDate = modifyDate;
		this.accountBank = accountBank;
		this.payVia = payVia;
		this.payType = payType;
		this.accountCycle = accountCycle;
		this.customerValueLevel = customerValueLevel;
		this.customerLevel = customerLevel;
		this.trendency = trendency;
		this.output = output;
		this.outputExpectation = outputExpectation;
		this.serviceFeeLimitation = serviceFeeLimitation;
		this.industry = industry;
		this.loyalty = loyalty;
		this.satisfaction = satisfaction;
	}

	// Property accessors

	public Set getContactors() {
		return contactors;
	}

	public void setContactors(Set contactors) {
		this.contactors = contactors;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SysUser getSysUserByInsideContactorId() {
		return this.sysUserByInsideContactorId;
	}

	public void setSysUserByInsideContactorId(SysUser sysUserByInsideContactorId) {
		this.sysUserByInsideContactorId = sysUserByInsideContactorId;
	}

	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public SysUser getSysUserByModificatorId() {
		return this.sysUserByModificatorId;
	}

	public void setSysUserByModificatorId(SysUser sysUserByModificatorId) {
		this.sysUserByModificatorId = sysUserByModificatorId;
	}

	public Reason getReason() {
		return this.reason;
	}

	public void setReason(Reason reason) {
		this.reason = reason;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNameEn() {
		return this.nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddressEn() {
		return this.addressEn;
	}

	public void setAddressEn(String addressEn) {
		this.addressEn = addressEn;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Timestamp getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public Double getBalance() {
		return this.balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getClassification() {
		return this.classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getFieldDemands() {
		return this.fieldDemands;
	}

	public void setFieldDemands(String fieldDemands) {
		this.fieldDemands = fieldDemands;
	}

	public String getCertificateDemands() {
		return this.certificateDemands;
	}

	public void setCertificateDemands(String certificateDemands) {
		this.certificateDemands = certificateDemands;
	}

	public String getSpecialDemands() {
		return this.specialDemands;
	}

	public void setSpecialDemands(String specialDemands) {
		this.specialDemands = specialDemands;
	}

	public Double getCreditAmount() {
		return this.creditAmount;
	}

	public void setCreditAmount(Double creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Timestamp getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Timestamp modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getAccountBank() {
		return this.accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public Integer getPayVia() {
		return this.payVia;
	}

	public void setPayVia(Integer payVia) {
		this.payVia = payVia;
	}

	public Integer getPayType() {
		return this.payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getAccountCycle() {
		return this.accountCycle;
	}

	public void setAccountCycle(Integer accountCycle) {
		this.accountCycle = accountCycle;
	}

	public Integer getCustomerValueLevel() {
		return this.customerValueLevel;
	}

	public void setCustomerValueLevel(Integer customerValueLevel) {
		this.customerValueLevel = customerValueLevel;
	}

	public Integer getCustomerLevel() {
		return this.customerLevel;
	}

	public void setCustomerLevel(Integer customerLevel) {
		this.customerLevel = customerLevel;
	}

	public Integer getTrendency() {
		return this.trendency;
	}

	public void setTrendency(Integer trendency) {
		this.trendency = trendency;
	}

	public Double getOutput() {
		return this.output;
	}

	public void setOutput(Double output) {
		this.output = output;
	}

	public Double getOutputExpectation() {
		return this.outputExpectation;
	}

	public void setOutputExpectation(Double outputExpectation) {
		this.outputExpectation = outputExpectation;
	}

	public Double getServiceFeeLimitation() {
		return this.serviceFeeLimitation;
	}

	public void setServiceFeeLimitation(Double serviceFeeLimitation) {
		this.serviceFeeLimitation = serviceFeeLimitation;
	}

	public Integer getIndustry() {
		return this.industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}
	public Integer getLoyalty()
	{
		return this.loyalty;
	}
	
	public void setLoyalty(Integer loyalty)
	{
		this.loyalty=loyalty;
	}
	
	public Integer getSatisfaction()
	{
		return this.satisfaction;
	}
	
	public void setSatisfaction(Integer satisfaction)
	{
		this.satisfaction=satisfaction;
	}

}