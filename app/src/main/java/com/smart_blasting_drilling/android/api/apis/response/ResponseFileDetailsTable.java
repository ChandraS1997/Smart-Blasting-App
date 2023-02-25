package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseFileDetailsTable implements Serializable {

	@SerializedName("Status")
	private String status;

	@SerializedName("IsDeleted")
	private int isDeleted;

	@SerializedName("LASFilePath")
	private String lASFilePath;

	@SerializedName("PostProcessPath")
	private Object postProcessPath;

	@SerializedName("LastModifiedDateTime")
	private String lastModifiedDateTime;

	@SerializedName("CompanyID")
	private String companyID;

	@SerializedName("PreProcessPath")
	private String preProcessPath;

	@SerializedName("FileName")
	private String fileName;

	@SerializedName("MineCode")
	private int mineCode;

	@SerializedName("FileCode")
	private int fileCode;

	@SerializedName("FileType")
	private String fileType;

	@SerializedName("LASPrepareTime")
	private String lASPrepareTime;

	@SerializedName("UserID")
	private String userID;

	@SerializedName("UploadTime")
	private String uploadTime;

	@SerializedName("FailureMessage")
	private Object failureMessage;

	@SerializedName("CompleteTime")
	private Object completeTime;

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setIsDeleted(int isDeleted){
		this.isDeleted = isDeleted;
	}

	public int getIsDeleted(){
		return isDeleted;
	}

	public void setLASFilePath(String lASFilePath){
		this.lASFilePath = lASFilePath;
	}

	public String getLASFilePath(){
		return lASFilePath;
	}

	public void setPostProcessPath(Object postProcessPath){
		this.postProcessPath = postProcessPath;
	}

	public Object getPostProcessPath(){
		return postProcessPath;
	}

	public void setLastModifiedDateTime(String lastModifiedDateTime){
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	public String getLastModifiedDateTime(){
		return lastModifiedDateTime;
	}

	public void setCompanyID(String companyID){
		this.companyID = companyID;
	}

	public String getCompanyID(){
		return companyID;
	}

	public void setPreProcessPath(String preProcessPath){
		this.preProcessPath = preProcessPath;
	}

	public String getPreProcessPath(){
		return preProcessPath;
	}

	public void setFileName(String fileName){
		this.fileName = fileName;
	}

	public String getFileName(){
		return fileName;
	}

	public void setMineCode(int mineCode){
		this.mineCode = mineCode;
	}

	public int getMineCode(){
		return mineCode;
	}

	public void setFileCode(int fileCode){
		this.fileCode = fileCode;
	}

	public int getFileCode(){
		return fileCode;
	}

	public void setFileType(String fileType){
		this.fileType = fileType;
	}

	public String getFileType(){
		return fileType;
	}

	public void setLASPrepareTime(String lASPrepareTime){
		this.lASPrepareTime = lASPrepareTime;
	}

	public String getLASPrepareTime(){
		return lASPrepareTime;
	}

	public void setUserID(String userID){
		this.userID = userID;
	}

	public String getUserID(){
		return userID;
	}

	public void setUploadTime(String uploadTime){
		this.uploadTime = uploadTime;
	}

	public String getUploadTime(){
		return uploadTime;
	}

	public void setFailureMessage(Object failureMessage){
		this.failureMessage = failureMessage;
	}

	public Object getFailureMessage(){
		return failureMessage;
	}

	public void setCompleteTime(Object completeTime){
		this.completeTime = completeTime;
	}

	public Object getCompleteTime(){
		return completeTime;
	}
}