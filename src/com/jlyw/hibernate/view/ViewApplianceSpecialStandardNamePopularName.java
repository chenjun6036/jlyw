package com.jlyw.hibernate.view;

/**
 * 1�������½�ί�е�ʱ�������ߵ�ѡ�񣨷������ơ���׼���ơ��������ƣ�
 * 2������QualificationManager������һ���ڵ�ĸ��ڵ�ȣ�
 * 3������TaskAssignManager����ѯһ���ڵ��������ĩ���ӽڵ㡪����׼���ƣ�
 * 4������TargetApplianceServelt case5����ѯһ���ڵ��������ĩ���ӽڵ㡪����׼���ƣ�
 * Type�ֶεĶ��壺0����׼���ƣ�1���������ƣ�2����������
 * By zhan
 * 2012-06-02
 */

public class ViewApplianceSpecialStandardNamePopularName implements
		java.io.Serializable {

	// Fields

	private ViewApplianceSpecialStandardNamePopularNameId id;
	private Integer parentId;
	private String name;
	private String brief;
	private Integer status;

	// Constructors

	/** default constructor */
	public ViewApplianceSpecialStandardNamePopularName() {
	}

	/** full constructor */
	

	// Property accessors

	public ViewApplianceSpecialStandardNamePopularNameId getId() {
		return this.id;
	}

	public ViewApplianceSpecialStandardNamePopularName(
			ViewApplianceSpecialStandardNamePopularNameId id, Integer parentId,
			String name, String brief, Integer status) {
		super();
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.brief = brief;
		this.status = status;
	}

	public void setId(ViewApplianceSpecialStandardNamePopularNameId id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}