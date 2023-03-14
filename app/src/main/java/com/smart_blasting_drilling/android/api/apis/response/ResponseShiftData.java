package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseShiftData{

	@SerializedName("IsDeleted")
	private boolean isDeleted;

	@SerializedName("ShiftName")
	private String shiftName;

	@SerializedName("EndTime")
	private String endTime;

	@SerializedName("CompanyId")
	private String companyId;

	@SerializedName("SiteCode")
	private int siteCode;

	@SerializedName("ShiftCode")
	private int shiftCode;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("StartTime")
	private String startTime;

	public void setIsDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public void setShiftName(String shiftName){
		this.shiftName = shiftName;
	}

	public String getShiftName(){
		return shiftName;
	}

	public void setEndTime(String endTime){
		this.endTime = endTime;
	}

	public String getEndTime(){
		return endTime;
	}

	public void setCompanyId(String companyId){
		this.companyId = companyId;
	}

	public String getCompanyId(){
		return companyId;
	}

	public void setSiteCode(int siteCode){
		this.siteCode = siteCode;
	}

	public int getSiteCode(){
		return siteCode;
	}

	public void setShiftCode(int shiftCode){
		this.shiftCode = shiftCode;
	}

	public int getShiftCode(){
		return shiftCode;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setStartTime(String startTime){
		this.startTime = startTime;
	}

	public String getStartTime(){
		return startTime;
	}
}