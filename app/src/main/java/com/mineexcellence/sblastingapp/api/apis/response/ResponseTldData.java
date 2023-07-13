package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseTldData{

	@SerializedName("TldCode")
	private int tldCode;

	@SerializedName("UnitCost")
	private Object unitCost;

	@SerializedName("Comment")
	private Object comment;

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("length")
	private Object length;

	@SerializedName("Delay")
	private Object delay;

	@SerializedName("Name")
	private String name;

	public void setTldCode(int tldCode){
		this.tldCode = tldCode;
	}

	public int getTldCode(){
		return tldCode;
	}

	public void setUnitCost(Object unitCost){
		this.unitCost = unitCost;
	}

	public Object getUnitCost(){
		return unitCost;
	}

	public void setComment(Object comment){
		this.comment = comment;
	}

	public Object getComment(){
		return comment;
	}

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setIsvisible(int isvisible){
		this.isvisible = isvisible;
	}

	public int getIsvisible(){
		return isvisible;
	}

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}

	public void setLength(Object length){
		this.length = length;
	}

	public Object getLength(){
		return length;
	}

	public void setDelay(Object delay){
		this.delay = delay;
	}

	public Object getDelay(){
		return delay;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}