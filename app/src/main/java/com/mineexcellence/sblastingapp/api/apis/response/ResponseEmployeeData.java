package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseEmployeeData{

	@SerializedName("CostTypeId")
	private int costTypeId;

	@SerializedName("IsDeleted")
	private boolean isDeleted;

	@SerializedName("Email")
	private String email;

	@SerializedName("U_UserId")
	private String uUserId;

	@SerializedName("EmployeeCode")
	private int employeeCode;

	@SerializedName("DesignationCode")
	private int designationCode;

	@SerializedName("CompanyId")
	private String companyId;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("DesignationName")
	private String designationName;

	@SerializedName("CostTypeName")
	private String costTypeName;

	@SerializedName("Name")
	private String name;

	@SerializedName("CostH")
	private Object costH;

	public void setCostTypeId(int costTypeId){
		this.costTypeId = costTypeId;
	}

	public int getCostTypeId(){
		return costTypeId;
	}

	public void setIsDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUUserId(String uUserId){
		this.uUserId = uUserId;
	}

	public String getUUserId(){
		return uUserId;
	}

	public void setEmployeeCode(int employeeCode){
		this.employeeCode = employeeCode;
	}

	public int getEmployeeCode(){
		return employeeCode;
	}

	public void setDesignationCode(int designationCode){
		this.designationCode = designationCode;
	}

	public int getDesignationCode(){
		return designationCode;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setDesignationName(String designationName){
		this.designationName = designationName;
	}

	public String getDesignationName(){
		return designationName;
	}

	public void setCostTypeName(String costTypeName){
		this.costTypeName = costTypeName;
	}

	public String getCostTypeName(){
		return costTypeName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setCostH(Object costH){
		this.costH = costH;
	}

	public Object getCostH(){
		return costH;
	}
}