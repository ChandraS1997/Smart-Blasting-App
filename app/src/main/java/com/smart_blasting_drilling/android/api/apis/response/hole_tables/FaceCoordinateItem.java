package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

public class FaceCoordinateItem{

	@SerializedName("X")
	private String x;

	@SerializedName("Y")
	private String y;

	@SerializedName("Z")
	private String z;

	public void setX(String x){
		this.x = x;
	}

	public String getX(){
		return x;
	}

	public void setY(String y){
		this.y = y;
	}

	public String getY(){
		return y;
	}

	public void setZ(String z){
		this.z = z;
	}

	public String getZ(){
		return z;
	}
}