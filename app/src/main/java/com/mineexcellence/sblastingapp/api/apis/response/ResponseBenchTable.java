package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseBenchTable implements Serializable {

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("UserID")
	private Object userID;

	@SerializedName("ZoneCode")
	private int zoneCode;

	@SerializedName("BenchCode")
	private int benchCode;

	@SerializedName("Name")
	private String name;

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

	public void setUserID(Object userID){
		this.userID = userID;
	}

	public Object getUserID(){
		return userID;
	}

	public void setZoneCode(int zoneCode){
		this.zoneCode = zoneCode;
	}

	public int getZoneCode(){
		return zoneCode;
	}

	public void setBenchCode(int benchCode){
		this.benchCode = benchCode;
	}

	public int getBenchCode(){
		return benchCode;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}