package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseExplosiveData{

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("ExplosiveDia")
	private int explosiveDia;

	@SerializedName("Magazine2")
	private String magazine2;

	@SerializedName("Magazine1")
	private String magazine1;

	@SerializedName("Density")
	private Object density;

	@SerializedName("Name")
	private String name;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("Mag1Unit")
	private int mag1Unit;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("ExpCode")
	private int expCode;

	@SerializedName("LicenseMagzine2")
	private String licenseMagzine2;

	@SerializedName("CostUnit")
	private int costUnit;

	@SerializedName("LicenseMagzine1")
	private String licenseMagzine1;

	@SerializedName("Comment")
	private String comment;

	@SerializedName("Mag2Unit")
	private int mag2Unit;

	@SerializedName("Color")
	private String color;

	@SerializedName("Mag2Class")
	private int mag2Class;

	@SerializedName("Make")
	private String make;

	@SerializedName("VOD")
	private Object vOD;

	@SerializedName("UnitCost")
	private Object unitCost;

	@SerializedName("ExpType")
	private int expType;

	@SerializedName("Type")
	private int type;

	@SerializedName("Mag1Class")
	private int mag1Class;

	@SerializedName("Ree")
	private Object ree;

	@SerializedName("Class")
	private String classStr;

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setExplosiveDia(int explosiveDia){
		this.explosiveDia = explosiveDia;
	}

	public int getExplosiveDia(){
		return explosiveDia;
	}

	public void setMagazine2(String magazine2){
		this.magazine2 = magazine2;
	}

	public String getMagazine2(){
		return magazine2;
	}

	public void setMagazine1(String magazine1){
		this.magazine1 = magazine1;
	}

	public String getMagazine1(){
		return magazine1;
	}

	public void setDensity(Object density){
		this.density = density;
	}

	public Object getDensity(){
		return density;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIsvisible(int isvisible){
		this.isvisible = isvisible;
	}

	public int getIsvisible(){
		return isvisible;
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

	public void setExpCode(int expCode){
		this.expCode = expCode;
	}

	public int getExpCode(){
		return expCode;
	}

	public void setLicenseMagzine2(String licenseMagzine2){
		this.licenseMagzine2 = licenseMagzine2;
	}

	public String getLicenseMagzine2(){
		return licenseMagzine2;
	}

	public void setCostUnit(int costUnit){
		this.costUnit = costUnit;
	}

	public int getCostUnit(){
		return costUnit;
	}

	public void setLicenseMagzine1(String licenseMagzine1){
		this.licenseMagzine1 = licenseMagzine1;
	}

	public String getLicenseMagzine1(){
		return licenseMagzine1;
	}

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

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
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

	public void setVOD(Object vOD){
		this.vOD = vOD;
	}

	public Object getVOD(){
		return vOD;
	}

	public void setUnitCost(Object unitCost){
		this.unitCost = unitCost;
	}

	public Object getUnitCost(){
		return unitCost;
	}

	public void setExpType(int expType){
		this.expType = expType;
	}

	public int getExpType(){
		return expType;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setMag1Class(int mag1Class){
		this.mag1Class = mag1Class;
	}

	public int getMag1Class(){
		return mag1Class;
	}

	public void setRee(Object ree){
		this.ree = ree;
	}

	public Object getRee(){
		return ree;
	}

	public void setClassStr(String classStr){
		this.classStr = classStr;
	}

	public String getClassStr(){
		return classStr;
	}
}