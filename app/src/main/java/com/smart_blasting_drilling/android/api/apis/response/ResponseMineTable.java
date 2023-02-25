package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseMineTable implements Serializable {

	@SerializedName("PitName")
	private String owner;

	@SerializedName("Email")
	private String email;

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("UnitSystem")
	private int unitSystem;

	@SerializedName("MineCode")
	private int mineCode;

	@SerializedName("LogoFileName")
	private String logoFileName;

	@SerializedName("FragOverSize")
	private Object fragOverSize;

	@SerializedName("long")
	private String jsonMemberLong;

	@SerializedName("Name")
	private String name;

	@SerializedName("Add1")
	private String add1;

	@SerializedName("FlyRockLimit")
	private Object flyRockLimit;

	@SerializedName("GroundVibLimit")
	private Object groundVibLimit;

	@SerializedName("FragUnderSize")
	private Object fragUnderSize;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("Production")
	private int production;

	@SerializedName("Mineral")
	private String mineral;

	@SerializedName("MineCode1")
	private int mineCode1;

	@SerializedName("LicenceMag1")
	private String licenceMag1;

	@SerializedName("digitalmappath")
	private String digitalmappath;

	@SerializedName("AirVibLimit")
	private Object airVibLimit;

	@SerializedName("LicenceMag2")
	private String licenceMag2;

	@SerializedName("circle_radious")
	private String circleRadious;

	@SerializedName("FragInRange")
	private Object fragInRange;

	@SerializedName("Tel")
	private String tel;

	@SerializedName("Fax")
	private String fax;

	@SerializedName("Lat")
	private String lat;

	@SerializedName("DronefilePath")
	private String dronefilePath;

	public void setOwner(String owner){
		this.owner = owner;
	}

	public String getOwner(){
		return owner;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setUnitSystem(int unitSystem){
		this.unitSystem = unitSystem;
	}

	public int getUnitSystem(){
		return unitSystem;
	}

	public void setMineCode(int mineCode){
		this.mineCode = mineCode;
	}

	public int getMineCode(){
		return mineCode;
	}

	public void setLogoFileName(String logoFileName){
		this.logoFileName = logoFileName;
	}

	public String getLogoFileName(){
		return logoFileName;
	}

	public void setFragOverSize(Object fragOverSize){
		this.fragOverSize = fragOverSize;
	}

	public Object getFragOverSize(){
		return fragOverSize;
	}

	public void setJsonMemberLong(String jsonMemberLong){
		this.jsonMemberLong = jsonMemberLong;
	}

	public String getJsonMemberLong(){
		return jsonMemberLong;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setAdd1(String add1){
		this.add1 = add1;
	}

	public String getAdd1(){
		return add1;
	}

	public void setFlyRockLimit(Object flyRockLimit){
		this.flyRockLimit = flyRockLimit;
	}

	public Object getFlyRockLimit(){
		return flyRockLimit;
	}

	public void setGroundVibLimit(Object groundVibLimit){
		this.groundVibLimit = groundVibLimit;
	}

	public Object getGroundVibLimit(){
		return groundVibLimit;
	}

	public void setFragUnderSize(Object fragUnderSize){
		this.fragUnderSize = fragUnderSize;
	}

	public Object getFragUnderSize(){
		return fragUnderSize;
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

	public void setProduction(int production){
		this.production = production;
	}

	public int getProduction(){
		return production;
	}

	public void setMineral(String mineral){
		this.mineral = mineral;
	}

	public String getMineral(){
		return mineral;
	}

	public void setMineCode1(int mineCode1){
		this.mineCode1 = mineCode1;
	}

	public int getMineCode1(){
		return mineCode1;
	}

	public void setLicenceMag1(String licenceMag1){
		this.licenceMag1 = licenceMag1;
	}

	public String getLicenceMag1(){
		return licenceMag1;
	}

	public void setDigitalmappath(String digitalmappath){
		this.digitalmappath = digitalmappath;
	}

	public String getDigitalmappath(){
		return digitalmappath;
	}

	public void setAirVibLimit(Object airVibLimit){
		this.airVibLimit = airVibLimit;
	}

	public Object getAirVibLimit(){
		return airVibLimit;
	}

	public void setLicenceMag2(String licenceMag2){
		this.licenceMag2 = licenceMag2;
	}

	public String getLicenceMag2(){
		return licenceMag2;
	}

	public void setCircleRadious(String circleRadious){
		this.circleRadious = circleRadious;
	}

	public String getCircleRadious(){
		return circleRadious;
	}

	public void setFragInRange(Object fragInRange){
		this.fragInRange = fragInRange;
	}

	public Object getFragInRange(){
		return fragInRange;
	}

	public void setTel(String tel){
		this.tel = tel;
	}

	public String getTel(){
		return tel;
	}

	public void setFax(String fax){
		this.fax = fax;
	}

	public String getFax(){
		return fax;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	public void setDronefilePath(String dronefilePath){
		this.dronefilePath = dronefilePath;
	}

	public String getDronefilePath(){
		return dronefilePath;
	}
}