package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseInitiatingData{

	@SerializedName("Comment")
	private String comment;

	@SerializedName("Mag2Unit")
	private int mag2Unit;

	@SerializedName("CapIniMag1")
	private int capIniMag1;

	@SerializedName("CapIniMag2")
	private int capIniMag2;

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("Mag2Class")
	private int mag2Class;

	@SerializedName("Make")
	private String make;

	@SerializedName("Delay")
	private Object delay;

	@SerializedName("Name")
	private String name;

	@SerializedName("UnitCost")
	private Object unitCost;

	@SerializedName("IniCode")
	private int iniCode;

	@SerializedName("Mag1Class")
	private int mag1Class;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("IniType")
	private int iniType;

	@SerializedName("Length")
	private Object length;

	@SerializedName("Mag1Unit")
	private int mag1Unit;

	@SerializedName("UserID")
	private String userID;

	public void setComment(String comment){
		this.comment = comment;
	}

	public String getComment(){
		return comment;
	}

	public void setMag2Unit(int mag2Unit){
		this.mag2Unit = mag2Unit;
	}

	public int getMag2Unit(){
		return mag2Unit;
	}

	public void setCapIniMag1(int capIniMag1){
		this.capIniMag1 = capIniMag1;
	}

	public int getCapIniMag1(){
		return capIniMag1;
	}

	public void setCapIniMag2(int capIniMag2){
		this.capIniMag2 = capIniMag2;
	}

	public int getCapIniMag2(){
		return capIniMag2;
	}

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setMag2Class(int mag2Class){
		this.mag2Class = mag2Class;
	}

	public int getMag2Class(){
		return mag2Class;
	}

	public void setMake(String make){
		this.make = make;
	}

	public String getMake(){
		return make;
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

	public void setUnitCost(Object unitCost){
		this.unitCost = unitCost;
	}

	public Object getUnitCost(){
		return unitCost;
	}

	public void setIniCode(int iniCode){
		this.iniCode = iniCode;
	}

	public int getIniCode(){
		return iniCode;
	}

	public void setMag1Class(int mag1Class){
		this.mag1Class = mag1Class;
	}

	public int getMag1Class(){
		return mag1Class;
	}

	public void setIsvisible(int isvisible){
		this.isvisible = isvisible;
	}

	public int getIsvisible(){
		return isvisible;
	}

	public void setIniType(int iniType){
		this.iniType = iniType;
	}

	public int getIniType(){
		return iniType;
	}

	public void setLength(Object length){
		this.length = length;
	}

	public Object getLength(){
		return length;
	}

	public void setMag1Unit(int mag1Unit){
		this.mag1Unit = mag1Unit;
	}

	public int getMag1Unit(){
		return mag1Unit;
	}

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}
}