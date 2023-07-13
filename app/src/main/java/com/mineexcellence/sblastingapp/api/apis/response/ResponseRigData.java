package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseRigData{

	@SerializedName("RigManufacturer")
	private String rigManufacturer;

	@SerializedName("IsDeleted")
	private int isDeleted;

	@SerializedName("FailureToDepreciate")
	private Object failureToDepreciate;

	@SerializedName("CompanyId")
	private String companyId;

	@SerializedName("EngineHours")
	private Object engineHours;

	@SerializedName("SiteCode")
	private int siteCode;

	@SerializedName("Comments")
	private String comments;

	@SerializedName("RigCode")
	private int rigCode;

	@SerializedName("DepreciationRatePerMonth")
	private Object depreciationRatePerMonth;

	@SerializedName("SaleDate")
	private String saleDate;

	@SerializedName("Code")
	private String code;

	@SerializedName("Weight")
	private Object weight;

	@SerializedName("HeadHours")
	private Object headHours;

	@SerializedName("Name")
	private String name;

	@SerializedName("DepreciationPercent")
	private Object depreciationPercent;

	@SerializedName("Length")
	private Object length;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("AcquisitionDate")
	private String acquisitionDate;

	@SerializedName("Note")
	private String note;

	@SerializedName("Value")
	private Object value;

	@SerializedName("Height")
	private Object height;

	@SerializedName("RigModel")
	private String rigModel;

	@SerializedName("Width")
	private Object width;

	@SerializedName("EndDateDepreciation")
	private String endDateDepreciation;

	public void setRigManufacturer(String rigManufacturer){
		this.rigManufacturer = rigManufacturer;
	}

	public String getRigManufacturer(){
		return rigManufacturer;
	}

	public void setIsDeleted(int isDeleted){
		this.isDeleted = isDeleted;
	}

	public int getIsDeleted(){
		return isDeleted;
	}

	public void setFailureToDepreciate(Object failureToDepreciate){
		this.failureToDepreciate = failureToDepreciate;
	}

	public Object getFailureToDepreciate(){
		return failureToDepreciate;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setEngineHours(Object engineHours){
		this.engineHours = engineHours;
	}

	public Object getEngineHours(){
		return engineHours;
	}

	public void setSiteCode(int siteCode){
		this.siteCode = siteCode;
	}

	public int getSiteCode(){
		return siteCode;
	}

	public void setComments(String comments){
		this.comments = comments;
	}

	public String getComments(){
		return comments;
	}

	public void setRigCode(int rigCode){
		this.rigCode = rigCode;
	}

	public int getRigCode(){
		return rigCode;
	}

	public void setDepreciationRatePerMonth(Object depreciationRatePerMonth){
		this.depreciationRatePerMonth = depreciationRatePerMonth;
	}

	public Object getDepreciationRatePerMonth(){
		return depreciationRatePerMonth;
	}

	public void setSaleDate(String saleDate){
		this.saleDate = saleDate;
	}

	public String getSaleDate(){
		return saleDate;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getCode(){
		return code;
	}

	public void setWeight(Object weight){
		this.weight = weight;
	}

	public Object getWeight(){
		return weight;
	}

	public void setHeadHours(Object headHours){
		this.headHours = headHours;
	}

	public Object getHeadHours(){
		return headHours;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDepreciationPercent(Object depreciationPercent){
		this.depreciationPercent = depreciationPercent;
	}

	public Object getDepreciationPercent(){
		return depreciationPercent;
	}

	public void setLength(Object length){
		this.length = length;
	}

	public Object getLength(){
		return length;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setAcquisitionDate(String acquisitionDate){
		this.acquisitionDate = acquisitionDate;
	}

	public String getAcquisitionDate(){
		return acquisitionDate;
	}

	public void setNote(String note){
		this.note = note;
	}

	public String getNote(){
		return note;
	}

	public void setValue(Object value){
		this.value = value;
	}

	public Object getValue(){
		return value;
	}

	public void setHeight(Object height){
		this.height = height;
	}

	public Object getHeight(){
		return height;
	}

	public void setRigModel(String rigModel){
		this.rigModel = rigModel;
	}

	public String getRigModel(){
		return rigModel;
	}

	public void setWidth(Object width){
		this.width = width;
	}

	public Object getWidth(){
		return width;
	}

	public void setEndDateDepreciation(String endDateDepreciation){
		this.endDateDepreciation = endDateDepreciation;
	}

	public String getEndDateDepreciation(){
		return endDateDepreciation;
	}
}