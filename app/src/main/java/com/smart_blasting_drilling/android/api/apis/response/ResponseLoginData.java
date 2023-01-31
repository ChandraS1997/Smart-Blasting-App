package com.smart_blasting_drilling.android.api.apis.response;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseLoginData{

	@SerializedName("UserName")
	private String userName;

	@SerializedName("Email")
	private String email;

	@SerializedName("Address")
	private String address;

	@SerializedName("drims_UnitSystem")
	private String drimsUnitSystem;

	@SerializedName("message")
	private String message;

	@SerializedName("Contact")
	private String contact;

	@SerializedName("drims_permission")
	private DrimsPermission drimsPermission;

	@SerializedName("Occupation")
	private String occupation;

	@SerializedName("uid")
	private int uid;

	@SerializedName("companyid")
	private String companyid;

	@SerializedName("Userid")
	private String userid;

	@SerializedName("response")
	private String response;

	@SerializedName("user_access")
	private int userAccess;

	@SerializedName("Country")
	private String country;

	@SerializedName("user_permission")
	private int userPermission;

	@SerializedName("disclaimer")
	private List<String> disclaimer;

	@SerializedName("Password")
	private String password;

	@SerializedName("subscription_enddate")
	private String subscriptionEnddate;

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getUserName(){
		return userName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setDrimsUnitSystem(String drimsUnitSystem){
		this.drimsUnitSystem = drimsUnitSystem;
	}

	public String getDrimsUnitSystem(){
		return drimsUnitSystem;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setContact(String contact){
		this.contact = contact;
	}

	public String getContact(){
		return contact;
	}

	public void setDrimsPermission(DrimsPermission drimsPermission){
		this.drimsPermission = drimsPermission;
	}

	public DrimsPermission getDrimsPermission(){
		return drimsPermission;
	}

	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	public String getOccupation(){
		return occupation;
	}

	public void setUid(int uid){
		this.uid = uid;
	}

	public int getUid(){
		return uid;
	}

	public void setCompanyid(String companyid){
		this.companyid = companyid;
	}

	public String getCompanyid(){
		return companyid;
	}

	public void setUserid(String userid){
		this.userid = userid;
	}

	public String getUserid(){
		return userid;
	}

	public void setResponse(String response){
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setUserAccess(int userAccess){
		this.userAccess = userAccess;
	}

	public int getUserAccess(){
		return userAccess;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setUserPermission(int userPermission){
		this.userPermission = userPermission;
	}

	public int getUserPermission(){
		return userPermission;
	}

	public void setDisclaimer(List<String> disclaimer){
		this.disclaimer = disclaimer;
	}

	public List<String> getDisclaimer(){
		return disclaimer;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setSubscriptionEnddate(String subscriptionEnddate){
		this.subscriptionEnddate = subscriptionEnddate;
	}

	public String getSubscriptionEnddate(){
		return subscriptionEnddate;
	}
}