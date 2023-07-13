package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table6Item implements Serializable {

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("ContourNo")
	private int contourNo;

	@SerializedName("Constant")
	private int constant;

	@SerializedName("Increment")
	private int increment;

	@SerializedName("PPV")
	private Object pPV;

	@SerializedName("Latitude")
	private Object latitude;

	@SerializedName("Exp")
	private Object exp;

	@SerializedName("ChrgPerDelay")
	private int chrgPerDelay;

	@SerializedName("Longitude")
	private Object longitude;

	@SerializedName("Distance")
	private int distance;

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setContourNo(int contourNo){
		this.contourNo = contourNo;
	}

	public int getContourNo(){
		return contourNo;
	}

	public void setConstant(int constant){
		this.constant = constant;
	}

	public int getConstant(){
		return constant;
	}

	public void setIncrement(int increment){
		this.increment = increment;
	}

	public int getIncrement(){
		return increment;
	}

	public void setPPV(Object pPV){
		this.pPV = pPV;
	}

	public Object getPPV(){
		return pPV;
	}

	public void setLatitude(Object latitude){
		this.latitude = latitude;
	}

	public Object getLatitude(){
		return latitude;
	}

	public void setExp(Object exp){
		this.exp = exp;
	}

	public Object getExp(){
		return exp;
	}

	public void setChrgPerDelay(int chrgPerDelay){
		this.chrgPerDelay = chrgPerDelay;
	}

	public int getChrgPerDelay(){
		return chrgPerDelay;
	}

	public void setLongitude(Object longitude){
		this.longitude = longitude;
	}

	public Object getLongitude(){
		return longitude;
	}

	public void setDistance(int distance){
		this.distance = distance;
	}

	public int getDistance(){
		return distance;
	}
}