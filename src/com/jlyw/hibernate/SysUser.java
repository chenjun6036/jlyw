package com.jlyw.hibernate;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */

public class SysUser implements java.io.Serializable {

	// Fields

	private Integer id;
	private String name;
	private String brief;
	private String userName;
	private String password;
	private Boolean gender;
	private String jobNum;
	private String birthplace;
	private Date birthday;
	private String idnum;
	private String politicsStatus;
	private String nation;
	private Date workSince;
	private Date workHereSince;
	private String education;
	private String educationDate;
	private String educationFrom;
	private String degree;
	private String degreeDate;
	private String degreeFrom;
	private String jobTitle;
	private String administrationPost;
	private String partyPost;
	private Date partyDate;
	private String specialty;
	private String homeAdd;
	private String workAdd;
	private String tel;
	private String cellphone1;
	private String cellphone2;
	private String email;
	private Integer projectTeamId;
	private Integer status;
	private String type;
	private Timestamp cancelDate;
	private String remark;
	private Integer workLocationId;
	private String signature;
	private String photograph;
	private Boolean needDongle;

	// Constructors

	/** default constructor */
	public SysUser() {
	}

	/** minimal constructor */
	public SysUser(String name, String brief, String userName, String password,
			Boolean gender, String jobNum, String birthplace, Date birthday,
			String idnum, String politicsStatus, String nation, Date workSince,
			Date workHereSince, String education, String educationDate,
			String educationFrom, String degree, String degreeDate,
			String degreeFrom, String jobTitle, String administrationPost,
			String specialty, String homeAdd, String workAdd, String tel,
			String cellphone1, String email, Integer status, String type,
			Integer workLocationId, String signature, String photograph) {
		this.name = name;
		this.brief = brief;
		this.userName = userName;
		this.password = password;
		this.gender = gender;
		this.jobNum = jobNum;
		this.birthplace = birthplace;
		this.birthday = birthday;
		this.idnum = idnum;
		this.politicsStatus = politicsStatus;
		this.nation = nation;
		this.workSince = workSince;
		this.workHereSince = workHereSince;
		this.education = education;
		this.educationDate = educationDate;
		this.educationFrom = educationFrom;
		this.degree = degree;
		this.degreeDate = degreeDate;
		this.degreeFrom = degreeFrom;
		this.jobTitle = jobTitle;
		this.administrationPost = administrationPost;
		this.specialty = specialty;
		this.homeAdd = homeAdd;
		this.workAdd = workAdd;
		this.tel = tel;
		this.cellphone1 = cellphone1;
		this.email = email;
		this.status = status;
		this.type = type;
		this.workLocationId = workLocationId;
		this.signature = signature;
		this.photograph = photograph;
	}

	/** full constructor */
	public SysUser(String name, String brief, String userName, String password,
			Boolean gender, String jobNum, String birthplace, Date birthday,
			String idnum, String politicsStatus, String nation, Date workSince,
			Date workHereSince, String education, String educationDate,
			String educationFrom, String degree, String degreeDate,
			String degreeFrom, String jobTitle, String administrationPost,
			String partyPost, Date partyDate, String specialty, String homeAdd,
			String workAdd, String tel, String cellphone1, String cellphone2,
			String email, Integer projectTeamId, Integer status, String type,
			Timestamp cancelDate, String remark, Integer workLocationId,
			String signature, String photograph, Boolean needDongle) {
		this.name = name;
		this.brief = brief;
		this.userName = userName;
		this.password = password;
		this.gender = gender;
		this.jobNum = jobNum;
		this.birthplace = birthplace;
		this.birthday = birthday;
		this.idnum = idnum;
		this.politicsStatus = politicsStatus;
		this.nation = nation;
		this.workSince = workSince;
		this.workHereSince = workHereSince;
		this.education = education;
		this.educationDate = educationDate;
		this.educationFrom = educationFrom;
		this.degree = degree;
		this.degreeDate = degreeDate;
		this.degreeFrom = degreeFrom;
		this.jobTitle = jobTitle;
		this.administrationPost = administrationPost;
		this.partyPost = partyPost;
		this.partyDate = partyDate;
		this.specialty = specialty;
		this.homeAdd = homeAdd;
		this.workAdd = workAdd;
		this.tel = tel;
		this.cellphone1 = cellphone1;
		this.cellphone2 = cellphone2;
		this.email = email;
		this.projectTeamId = projectTeamId;
		this.status = status;
		this.type = type;
		this.cancelDate = cancelDate;
		this.remark = remark;
		this.workLocationId = workLocationId;
		this.signature = signature;
		this.photograph = photograph;
		this.needDongle = needDongle;
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

	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return this.gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public String getJobNum() {
		return this.jobNum;
	}

	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}

	public String getBirthplace() {
		return this.birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdnum() {
		return this.idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getPoliticsStatus() {
		return this.politicsStatus;
	}

	public void setPoliticsStatus(String politicsStatus) {
		this.politicsStatus = politicsStatus;
	}

	public String getNation() {
		return this.nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public Date getWorkSince() {
		return this.workSince;
	}

	public void setWorkSince(Date workSince) {
		this.workSince = workSince;
	}

	public Date getWorkHereSince() {
		return this.workHereSince;
	}

	public void setWorkHereSince(Date workHereSince) {
		this.workHereSince = workHereSince;
	}

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEducationDate() {
		return this.educationDate;
	}

	public void setEducationDate(String educationDate) {
		this.educationDate = educationDate;
	}

	public String getEducationFrom() {
		return this.educationFrom;
	}

	public void setEducationFrom(String educationFrom) {
		this.educationFrom = educationFrom;
	}

	public String getDegree() {
		return this.degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getDegreeDate() {
		return this.degreeDate;
	}

	public void setDegreeDate(String degreeDate) {
		this.degreeDate = degreeDate;
	}

	public String getDegreeFrom() {
		return this.degreeFrom;
	}

	public void setDegreeFrom(String degreeFrom) {
		this.degreeFrom = degreeFrom;
	}

	public String getJobTitle() {
		return this.jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getAdministrationPost() {
		return this.administrationPost;
	}

	public void setAdministrationPost(String administrationPost) {
		this.administrationPost = administrationPost;
	}

	public String getPartyPost() {
		return this.partyPost;
	}

	public void setPartyPost(String partyPost) {
		this.partyPost = partyPost;
	}

	public Date getPartyDate() {
		return this.partyDate;
	}

	public void setPartyDate(Date partyDate) {
		this.partyDate = partyDate;
	}

	public String getSpecialty() {
		return this.specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public String getHomeAdd() {
		return this.homeAdd;
	}

	public void setHomeAdd(String homeAdd) {
		this.homeAdd = homeAdd;
	}

	public String getWorkAdd() {
		return this.workAdd;
	}

	public void setWorkAdd(String workAdd) {
		this.workAdd = workAdd;
	}

	public String getTel() {
		return this.tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCellphone1() {
		return this.cellphone1;
	}

	public void setCellphone1(String cellphone1) {
		this.cellphone1 = cellphone1;
	}

	public String getCellphone2() {
		return this.cellphone2;
	}

	public void setCellphone2(String cellphone2) {
		this.cellphone2 = cellphone2;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getProjectTeamId() {
		return this.projectTeamId;
	}

	public void setProjectTeamId(Integer projectTeamId) {
		this.projectTeamId = projectTeamId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getCancelDate() {
		return this.cancelDate;
	}

	public void setCancelDate(Timestamp cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getWorkLocationId() {
		return this.workLocationId;
	}

	public void setWorkLocationId(Integer workLocationId) {
		this.workLocationId = workLocationId;
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getPhotograph() {
		return this.photograph;
	}

	public void setPhotograph(String photograph) {
		this.photograph = photograph;
	}

	public Boolean getNeedDongle() {
		return this.needDongle;
	}

	public void setNeedDongle(Boolean needDongle) {
		this.needDongle = needDongle;
	}

}