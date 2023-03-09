package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Position implements Serializable {

	@SerializedName("x")
	private Object x;

	@SerializedName("y")
	private Object y;

	@SerializedName("z")
	private Object z;

	public void setX(Object x){
		this.x = x;
	}

	public Object getX(){
		return x;
	}

	public void setY(Object y){
		this.y = y;
	}

	public Object getY(){
		return y;
	}

	public void setZ(Object z){
		this.z = z;
	}

	public Object getZ(){
		return z;
	}
}