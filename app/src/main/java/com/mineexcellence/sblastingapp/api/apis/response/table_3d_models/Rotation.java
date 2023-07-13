package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

public class Rotation{

	@SerializedName("_order")
	private String order;

	@SerializedName("_x")
	private int x;

	@SerializedName("_y")
	private int y;

	@SerializedName("_z")
	private Object z;

	public void setOrder(String order){
		this.order = order;
	}

	public String getOrder(){
		return order;
	}

	public void setX(int x){
		this.x = x;
	}

	public int getX(){
		return x;
	}

	public void setY(int y){
		this.y = y;
	}

	public int getY(){
		return y;
	}

	public void setZ(Object z){
		this.z = z;
	}

	public Object getZ(){
		return z;
	}
}