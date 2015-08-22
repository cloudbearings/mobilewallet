package com.margaret.parking.pojo;

public class PlateCategories {

	private int plateCategoryId;
	private int plateSourceId;
	private String name;
	private String categoryDesc;
	private String isActive;
	private String createdDate;
	private int createdBy;
	private String modifiedDate;
	private int modifiedBy;

	public int getPlateCategoryId() {
		return plateCategoryId;
	}

	public void setPlateCategoryId(int plateCategoryId) {
		this.plateCategoryId = plateCategoryId;
	}

	public int getPlateSourceId() {
		return plateSourceId;
	}

	public void setPlateSourceId(int plateSourceId) {
		this.plateSourceId = plateSourceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryDesc() {
		return categoryDesc;
	}

	public void setCategoryDesc(String categoryDesc) {
		this.categoryDesc = categoryDesc;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public int getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(int modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

}
