package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseAllRecordDataItem{

	@SerializedName("image")
	private String image;

	@SerializedName("Video")
	private String video;

	@SerializedName("resultset")
	private List<ResultsetItem> resultset;

	@SerializedName("field_info")
	private String fieldInfo;

	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setVideo(String video){
		this.video = video;
	}

	public String getVideo(){
		return video;
	}

	public void setResultset(List<ResultsetItem> resultset){
		this.resultset = resultset;
	}

	public List<ResultsetItem> getResultset(){
		return resultset;
	}

	public void setFieldInfo(String fieldInfo){
		this.fieldInfo = fieldInfo;
	}

	public String getFieldInfo(){
		return fieldInfo;
	}
}