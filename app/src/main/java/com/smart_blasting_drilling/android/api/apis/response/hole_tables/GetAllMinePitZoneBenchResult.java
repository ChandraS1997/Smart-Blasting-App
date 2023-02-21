package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetAllMinePitZoneBenchResult implements Serializable {

	@SerializedName("GetAllMinePitZoneBenchResult")
	private String getAllMinePitZoneBenchResult;

	public void setGetAllMinePitZoneBenchResult(String getAllMinePitZoneBenchResult){
		this.getAllMinePitZoneBenchResult = getAllMinePitZoneBenchResult;
	}

	public String getGetAllMinePitZoneBenchResult(){
		return getAllMinePitZoneBenchResult;
	}
}