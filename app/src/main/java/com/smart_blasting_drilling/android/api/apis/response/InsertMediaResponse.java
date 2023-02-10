package com.smart_blasting_drilling.android.api.apis.response;

public class InsertMediaResponse{
	private String response;
	private Object returnObject;
	private String errorMessage;

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setReturnObject(Object returnObject){
		this.returnObject = returnObject;
	}

	public Object getReturnObject(){
		return returnObject;
	}

	public void setErrorMessage(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage(){
		return errorMessage;
	}
}



