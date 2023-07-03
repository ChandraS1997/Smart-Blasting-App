package com.smart_blasting_drilling.android.api.apis.response.table_3d_models.pre_spilit_table;

import com.google.gson.annotations.SerializedName;

public class LinePointsItem{

	@SerializedName("x")
	private Object x;

	@SerializedName("y")
	private Object y;

	@SerializedName("z")
	private int z;

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

	public void setZ(int z){
		this.z = z;
	}

	public int getZ(){
		return z;
	}
}