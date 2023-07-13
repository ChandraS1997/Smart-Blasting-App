package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseSiteDetail{

	@SerializedName("OwnerName")
	private String ownerName;

	@SerializedName("IsDeleted")
	private boolean isDeleted;

	@SerializedName("ClientName")
	private String clientName;

	@SerializedName("Email")
	private String email;

	@SerializedName("SiteAddress")
	private String siteAddress;

	@SerializedName("SiteName")
	private String siteName;

	@SerializedName("CompanyId")
	private String companyId;

	@SerializedName("SiteCode")
	private int siteCode;

	@SerializedName("Phone")
	private String phone;

	@SerializedName("UserId")
	private String userId;

	@SerializedName("CompanyLogoPath")
	private String companyLogoPath;

	public void setOwnerName(String ownerName){
		this.ownerName = ownerName;
	}

	public String getOwnerName(){
		return ownerName;
	}

	public void setIsDeleted(boolean isDeleted){
		this.isDeleted = isDeleted;
	}

	public boolean isIsDeleted(){
		return isDeleted;
	}

	public void setClientName(String clientName){
		this.clientName = clientName;
	}

	public String getClientName(){
		return clientName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setSiteAddress(String siteAddress){
		this.siteAddress = siteAddress;
	}

	public String getSiteAddress(){
		return siteAddress;
	}

	public void setSiteName(String siteName){
		this.siteName = siteName;
	}

	public String getSiteName(){
		return siteName;
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

	public void setPhone(String phone){
		this.phone = phone;
	}

	public String getPhone(){
		return phone;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

	public void setCompanyLogoPath(String companyLogoPath){
		this.companyLogoPath = companyLogoPath;
	}

	public String getCompanyLogoPath(){
		return companyLogoPath;
	}
}