package com.smart_blasting_drilling.android.api.apis.response;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseAllRecordData implements Serializable {

	@SerializedName("Response")
	String response;

	@SerializedName("ErrorMessage")
	String errorMessage;

	@SerializedName("ReturnObject")
	private String responseAllRecordData;

	public void setResponseAllRecordData(String responseAllRecordData){
		this.responseAllRecordData = responseAllRecordData;
	}

	public String getResponseAllRecordData(){
		return responseAllRecordData;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}