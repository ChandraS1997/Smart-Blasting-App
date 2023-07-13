package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseDrillMethodData{

	@SerializedName("DrillMethodId")
	private int drillMethodId;

	@SerializedName("DrillMethod")
	private String drillMethod;

	public void setDrillMethodId(int drillMethodId){
		this.drillMethodId = drillMethodId;
	}

	public int getDrillMethodId(){
		return drillMethodId;
	}

	public void setDrillMethod(String drillMethod){
		this.drillMethod = drillMethod;
	}

	public String getDrillMethod(){
		return drillMethod;
	}
}