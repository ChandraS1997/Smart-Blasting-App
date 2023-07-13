package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseZoneTable implements Serializable {

	@SerializedName("GeoStru")
	private String geoStru;

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("UnitBlastingCost")
	private Object unitBlastingCost;

	@SerializedName("Undersize")
	private Object undersize;

	@SerializedName("MuckForwardDistance")
	private Object muckForwardDistance;

	@SerializedName("BlastingQuality")
	private Object blastingQuality;

	@SerializedName("Density")
	private Object density;

	@SerializedName("Name")
	private String name;

	@SerializedName("GroundVibration")
	private Object groundVibration;

	@SerializedName("BoulderYield")
	private Object boulderYield;

	@SerializedName("Isvisible")
	private int isvisible;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("BlastingSafety")
	private Object blastingSafety;

	@SerializedName("StdDev")
	private Object stdDev;

	@SerializedName("DesiredFragmentationPercentage")
	private Object desiredFragmentationPercentage;

	@SerializedName("b")
	private Object b;

	@SerializedName("FloorCondition")
	private Object floorCondition;

	@SerializedName("AirVibration")
	private Object airVibration;

	@SerializedName("Flyrock")
	private Object flyrock;

	@SerializedName("BlastingCost")
	private Object blastingCost;

	@SerializedName("Backbreak")
	private Object backbreak;

	@SerializedName("PitCode")
	private int pitCode;

	@SerializedName("PowderFactor")
	private Object powderFactor;

	@SerializedName("k")
	private Object k;

	@SerializedName("CofCor")
	private Object cofCor;

	@SerializedName("MuckpileHeight")
	private Object muckpileHeight;

	@SerializedName("DrillFactor")
	private Object drillFactor;

	@SerializedName("ZoneCode")
	private int zoneCode;

	@SerializedName("Bedding")
	private String bedding;

	@SerializedName("Location")
	private String location;

	public void setGeoStru(String geoStru){
		this.geoStru = geoStru;
	}

	public String getGeoStru(){
		return geoStru;
	}

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setUnitBlastingCost(Object unitBlastingCost){
		this.unitBlastingCost = unitBlastingCost;
	}

	public Object getUnitBlastingCost(){
		return unitBlastingCost;
	}

	public void setUndersize(Object undersize){
		this.undersize = undersize;
	}

	public Object getUndersize(){
		return undersize;
	}

	public void setMuckForwardDistance(Object muckForwardDistance){
		this.muckForwardDistance = muckForwardDistance;
	}

	public Object getMuckForwardDistance(){
		return muckForwardDistance;
	}

	public void setBlastingQuality(Object blastingQuality){
		this.blastingQuality = blastingQuality;
	}

	public Object getBlastingQuality(){
		return blastingQuality;
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

	public void setGroundVibration(Object groundVibration){
		this.groundVibration = groundVibration;
	}

	public Object getGroundVibration(){
		return groundVibration;
	}

	public void setBoulderYield(Object boulderYield){
		this.boulderYield = boulderYield;
	}

	public Object getBoulderYield(){
		return boulderYield;
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

	public void setBlastingSafety(Object blastingSafety){
		this.blastingSafety = blastingSafety;
	}

	public Object getBlastingSafety(){
		return blastingSafety;
	}

	public void setStdDev(Object stdDev){
		this.stdDev = stdDev;
	}

	public Object getStdDev(){
		return stdDev;
	}

	public void setDesiredFragmentationPercentage(Object desiredFragmentationPercentage){
		this.desiredFragmentationPercentage = desiredFragmentationPercentage;
	}

	public Object getDesiredFragmentationPercentage(){
		return desiredFragmentationPercentage;
	}

	public void setB(Object b){
		this.b = b;
	}

	public Object getB(){
		return b;
	}

	public void setFloorCondition(Object floorCondition){
		this.floorCondition = floorCondition;
	}

	public Object getFloorCondition(){
		return floorCondition;
	}

	public void setAirVibration(Object airVibration){
		this.airVibration = airVibration;
	}

	public Object getAirVibration(){
		return airVibration;
	}

	public void setFlyrock(Object flyrock){
		this.flyrock = flyrock;
	}

	public Object getFlyrock(){
		return flyrock;
	}

	public void setBlastingCost(Object blastingCost){
		this.blastingCost = blastingCost;
	}

	public Object getBlastingCost(){
		return blastingCost;
	}

	public void setBackbreak(Object backbreak){
		this.backbreak = backbreak;
	}

	public Object getBackbreak(){
		return backbreak;
	}

	public void setPitCode(int pitCode){
		this.pitCode = pitCode;
	}

	public int getPitCode(){
		return pitCode;
	}

	public void setPowderFactor(Object powderFactor){
		this.powderFactor = powderFactor;
	}

	public Object getPowderFactor(){
		return powderFactor;
	}

	public void setK(Object k){
		this.k = k;
	}

	public Object getK(){
		return k;
	}

	public void setCofCor(Object cofCor){
		this.cofCor = cofCor;
	}

	public Object getCofCor(){
		return cofCor;
	}

	public void setMuckpileHeight(Object muckpileHeight){
		this.muckpileHeight = muckpileHeight;
	}

	public Object getMuckpileHeight(){
		return muckpileHeight;
	}

	public void setDrillFactor(Object drillFactor){
		this.drillFactor = drillFactor;
	}

	public Object getDrillFactor(){
		return drillFactor;
	}

	public void setZoneCode(int zoneCode){
		this.zoneCode = zoneCode;
	}

	public int getZoneCode(){
		return zoneCode;
	}

	public void setBedding(String bedding){
		this.bedding = bedding;
	}

	public String getBedding(){
		return bedding;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}
}