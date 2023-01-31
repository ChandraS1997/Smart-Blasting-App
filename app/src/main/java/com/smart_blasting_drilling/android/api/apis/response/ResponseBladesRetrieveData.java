package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

public class ResponseBladesRetrieveData{

	@SerializedName("PitName")
	private String pitName;

	@SerializedName("ZoneName")
	private String zoneName;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("DesignCode")
	private String designCode;

	@SerializedName("DesignName")
	private String designName;

	@SerializedName("DesignDateTime")
	private String designDateTime;

	@SerializedName("MineName")
	private String mineName;

	@SerializedName("BenchName")
	private String benchName;

	@SerializedName("RockName")
	private String rockName;

	public void setPitName(String pitName){
		this.pitName = pitName;
	}

	public String getPitName(){
		return pitName;
	}

	public void setZoneName(String zoneName){
		this.zoneName = zoneName;
	}

	public String getZoneName(){
		return zoneName;
	}

	public void setDesignId(String designId){
		this.designId = designId;
	}

	public String getDesignId(){
		return designId;
	}

	public void setDesignCode(String designCode){
		this.designCode = designCode;
	}

	public String getDesignCode(){
		return designCode;
	}

	public void setDesignName(String designName){
		this.designName = designName;
	}

	public String getDesignName(){
		return designName;
	}

	public void setDesignDateTime(String designDateTime){
		this.designDateTime = designDateTime;
	}

	public String getDesignDateTime(){
		return designDateTime;
	}

	public void setMineName(String mineName){
		this.mineName = mineName;
	}

	public String getMineName(){
		return mineName;
	}

	public void setBenchName(String benchName){
		this.benchName = benchName;
	}

	public String getBenchName(){
		return benchName;
	}

	public void setRockName(String rockName){
		this.rockName = rockName;
	}

	public String getRockName(){
		return rockName;
	}
}