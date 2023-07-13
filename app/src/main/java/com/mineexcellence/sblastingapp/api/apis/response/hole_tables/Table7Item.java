package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table7Item implements Serializable {

	@SerializedName("FlyrockModel")
	private Object flyrockModel;

	@SerializedName("Radius")
	private int radius;

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("Latitude")
	private Object latitude;

	@SerializedName("PositionDetails")
	private String positionDetails;

	@SerializedName("Longitude")
	private Object longitude;

	@SerializedName("FlyrockConst")
	private int flyrockConst;

	@SerializedName("BCR_PersonnelFactor")
	private Object bCRPersonnelFactor;

	@SerializedName("StemmingHeight")
	private Object stemmingHeight;

	@SerializedName("Angle")
	private int angle;

	@SerializedName("PlantSF")
	private int plantSF;

	@SerializedName("PosDetails")
	private String posDetails;

	@SerializedName("PersonalSF")
	private int personalSF;

	@SerializedName("BackThrow")
	private int backThrow;

	@SerializedName("BCR_PlantSafetyFactor")
	private Object bCRPlantSafetyFactor;

	@SerializedName("ChargeMass")
	private Object chargeMass;

	@SerializedName("FrontThrow")
	private Object frontThrow;

	@SerializedName("ScaleDepthOfBurial")
	private Object scaleDepthOfBurial;

	@SerializedName("Distance")
	private Object distance;

	public void setFlyrockModel(Object flyrockModel){
		this.flyrockModel = flyrockModel;
	}

	public Object getFlyrockModel(){
		return flyrockModel;
	}

	public void setRadius(int radius){
		this.radius = radius;
	}

	public int getRadius(){
		return radius;
	}

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setLatitude(Object latitude){
		this.latitude = latitude;
	}

	public Object getLatitude(){
		return latitude;
	}

	public void setPositionDetails(String positionDetails){
		this.positionDetails = positionDetails;
	}

	public String getPositionDetails(){
		return positionDetails;
	}

	public void setLongitude(Object longitude){
		this.longitude = longitude;
	}

	public Object getLongitude(){
		return longitude;
	}

	public void setFlyrockConst(int flyrockConst){
		this.flyrockConst = flyrockConst;
	}

	public int getFlyrockConst(){
		return flyrockConst;
	}

	public void setBCRPersonnelFactor(Object bCRPersonnelFactor){
		this.bCRPersonnelFactor = bCRPersonnelFactor;
	}

	public Object getBCRPersonnelFactor(){
		return bCRPersonnelFactor;
	}

	public void setStemmingHeight(Object stemmingHeight){
		this.stemmingHeight = stemmingHeight;
	}

	public Object getStemmingHeight(){
		return stemmingHeight;
	}

	public void setAngle(int angle){
		this.angle = angle;
	}

	public int getAngle(){
		return angle;
	}

	public void setPlantSF(int plantSF){
		this.plantSF = plantSF;
	}

	public int getPlantSF(){
		return plantSF;
	}

	public void setPosDetails(String posDetails){
		this.posDetails = posDetails;
	}

	public String getPosDetails(){
		return posDetails;
	}

	public void setPersonalSF(int personalSF){
		this.personalSF = personalSF;
	}

	public int getPersonalSF(){
		return personalSF;
	}

	public void setBackThrow(int backThrow){
		this.backThrow = backThrow;
	}

	public int getBackThrow(){
		return backThrow;
	}

	public void setBCRPlantSafetyFactor(Object bCRPlantSafetyFactor){
		this.bCRPlantSafetyFactor = bCRPlantSafetyFactor;
	}

	public Object getBCRPlantSafetyFactor(){
		return bCRPlantSafetyFactor;
	}

	public void setChargeMass(Object chargeMass){
		this.chargeMass = chargeMass;
	}

	public Object getChargeMass(){
		return chargeMass;
	}

	public void setFrontThrow(Object frontThrow){
		this.frontThrow = frontThrow;
	}

	public Object getFrontThrow(){
		return frontThrow;
	}

	public void setScaleDepthOfBurial(Object scaleDepthOfBurial){
		this.scaleDepthOfBurial = scaleDepthOfBurial;
	}

	public Object getScaleDepthOfBurial(){
		return scaleDepthOfBurial;
	}

	public void setDistance(Object distance){
		this.distance = distance;
	}

	public Object getDistance(){
		return distance;
	}
}