package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseDrillMaterialData{

	@SerializedName("MatTypeName")
	private String matTypeName;

	@SerializedName("MatTypeId")
	private int matTypeId;

	public void setMatTypeName(String matTypeName){
		this.matTypeName = matTypeName;
	}

	public String getMatTypeName(){
		return matTypeName;
	}

	public void setMatTypeId(int matTypeId){
		this.matTypeId = matTypeId;
	}

	public int getMatTypeId(){
		return matTypeId;
	}
}