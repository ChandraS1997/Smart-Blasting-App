package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response3DTable1DataModel implements Serializable {

	@SerializedName("BladesType")
	boolean is3dBlade;

	@SerializedName("PitName")
	private String pitName;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("DesignCode")
	private String designCode;

	@SerializedName("DesignName")
	private String designName;

	@SerializedName("ZoneId")
	private String zoneId;

	@SerializedName("DesignDateTime")
	private String designDateTime;

	@SerializedName("isPatternImport")
	private String isPatternImport;

	@SerializedName("UnitSystem")
	private String unitSystem;

	@SerializedName("MineName")
	private String mineName;

	@SerializedName("BenchName")
	private String benchName;

	@SerializedName("MineId")
	private String mineId;

	@SerializedName("PitId")
	private String pitId;

	@SerializedName("BlastingTypeId")
	private String blastingTypeId;

	@SerializedName("ZoneName")
	private String zoneName;

	@SerializedName("FileCode")
	private String fileCode;

	@SerializedName("DrillBlastLocation")
	private String drillBlastLocation;

	@SerializedName("RockCode")
	private String rockCode;

	@SerializedName("BenchID")
	private String benchID;

	@SerializedName("BlastingType")
	private String blastingType;

	@SerializedName("RockName")
	private String rockName;

	public boolean isIs3dBlade() {
		return is3dBlade;
	}

	public void setIs3dBlade(boolean is3dBlade) {
		this.is3dBlade = is3dBlade;
	}

	public void setPitName(String pitName){
		this.pitName = pitName;
	}

	public String getPitName(){
		return pitName;
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

	public void setZoneId(String zoneId){
		this.zoneId = zoneId;
	}

	public String getZoneId(){
		return zoneId;
	}

	public void setDesignDateTime(String designDateTime){
		this.designDateTime = designDateTime;
	}

	public String getDesignDateTime(){
		return designDateTime;
	}

	public void setIsPatternImport(String isPatternImport){
		this.isPatternImport = isPatternImport;
	}

	public String getIsPatternImport(){
		return isPatternImport;
	}

	public void setUnitSystem(String unitSystem){
		this.unitSystem = unitSystem;
	}

	public String getUnitSystem(){
		return unitSystem;
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

	public void setMineId(String mineId){
		this.mineId = mineId;
	}

	public String getMineId(){
		return mineId;
	}

	public void setPitId(String pitId){
		this.pitId = pitId;
	}

	public String getPitId(){
		return pitId;
	}

	public void setBlastingTypeId(String blastingTypeId){
		this.blastingTypeId = blastingTypeId;
	}

	public String getBlastingTypeId(){
		return blastingTypeId;
	}

	public void setZoneName(String zoneName){
		this.zoneName = zoneName;
	}

	public String getZoneName(){
		return zoneName;
	}

	public void setFileCode(String fileCode){
		this.fileCode = fileCode;
	}

	public String getFileCode(){
		return fileCode;
	}

	public void setDrillBlastLocation(String drillBlastLocation){
		this.drillBlastLocation = drillBlastLocation;
	}

	public String getDrillBlastLocation(){
		return drillBlastLocation;
	}

	public void setRockCode(String rockCode){
		this.rockCode = rockCode;
	}

	public String getRockCode(){
		return rockCode;
	}

	public void setBenchID(String benchID){
		this.benchID = benchID;
	}

	public String getBenchID(){
		return benchID;
	}

	public void setBlastingType(String blastingType){
		this.blastingType = blastingType;
	}

	public String getBlastingType(){
		return blastingType;
	}

	public void setRockName(String rockName){
		this.rockName = rockName;
	}

	public String getRockName(){
		return rockName;
	}
}