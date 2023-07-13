package com.mineexcellence.sblastingapp.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class DrimsPermission{

	@SerializedName("drims_startdate")
	private String drimsStartdate;

	@SerializedName("drims_access")
	private int drimsAccess;

	@SerializedName("drims_enddate")
	private String drimsEnddate;

	public void setDrimsStartdate(String drimsStartdate){
		this.drimsStartdate = drimsStartdate;
	}

	public String getDrimsStartdate(){
		return drimsStartdate;
	}

	public void setDrimsAccess(int drimsAccess){
		this.drimsAccess = drimsAccess;
	}

	public int getDrimsAccess(){
		return drimsAccess;
	}

	public void setDrimsEnddate(String drimsEnddate){
		this.drimsEnddate = drimsEnddate;
	}

	public String getDrimsEnddate(){
		return drimsEnddate;
	}
}