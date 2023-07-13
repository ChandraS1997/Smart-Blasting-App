package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseTypeTable implements Serializable {

	@SerializedName("TypeId")
	private int typeId;

	@SerializedName("TypeName")
	private String typeName;

	public void setTypeId(int typeId){
		this.typeId = typeId;
	}

	public int getTypeId(){
		return typeId;
	}

	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public String getTypeName(){
		return typeName;
	}
}