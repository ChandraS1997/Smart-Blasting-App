package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table3Item implements Serializable {

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("PitCode")
	private int pitCode;

	@SerializedName("MineCode")
	private int mineCode;

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

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}

	public void setPitCode(int pitCode){
		this.pitCode = pitCode;
	}

	public int getPitCode(){
		return pitCode;
	}

	public void setMineCode(int mineCode){
		this.mineCode = mineCode;
	}

	public int getMineCode(){
		return mineCode;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}