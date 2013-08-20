package com.jlyw.hibernate.view;

/**
 * 1、用于新建委托单时检验器具的选择（分类名称、标准名称、常用名称）
 * 2、用于QualificationManager（查找一个节点的父节点等）
 * 3、用于TaskAssignManager（查询一个节点的所有最末级子节点——标准名称）
 * 4、用于TargetApplianceServelt case5（查询一个节点的所有最末级子节点——标准名称）
 * Type字段的定义：0：标准名称；1：分类名称；2：常用名称
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