package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table10Item implements Serializable {

	@SerializedName("ImportFee")
	private int importFee;

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("ManpowerCost")
	private int manpowerCost;

	@SerializedName("DetType")
	private Object detType;

	@SerializedName("MobPlantFee")
	private int mobPlantFee;

	@SerializedName("DualDelay")
	private int dualDelay;

	@SerializedName("FixedPlantFee")
	private int fixedPlantFee;

	@SerializedName("DrillCostPerMeter")
	private int drillCostPerMeter;

	@SerializedName("HandlingFee")
	private int handlingFee;

	@SerializedName("InHoleDelay")
	private int inHoleDelay;

	@SerializedName("SurfaceRowDelay")
	private int surfaceRowDelay;

	@SerializedName("LoadingServices")
	private int loadingServices;

	@SerializedName("SurfaceHoleDelay")
	private int surfaceHoleDelay;

	public void setImportFee(int importFee){
		this.importFee = importFee;
	}

	public int getImportFee(){
		return importFee;
	}

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setManpowerCost(int manpowerCost){
		this.manpowerCost = manpowerCost;
	}

	public int getManpowerCost(){
		return manpowerCost;
	}

	public void setDetType(Object detType){
		this.detType = detType;
	}

	public Object getDetType(){
		return detType;
	}

	public void setMobPlantFee(int mobPlantFee){
		this.mobPlantFee = mobPlantFee;
	}

	public int getMobPlantFee(){
		return mobPlantFee;
	}

	public void setDualDelay(int dualDelay){
		this.dualDelay = dualDelay;
	}

	public int getDualDelay(){
		return dualDelay;
	}

	public void setFixedPlantFee(int fixedPlantFee){
		this.fixedPlantFee = fixedPlantFee;
	}

	public int getFixedPlantFee(){
		return fixedPlantFee;
	}

	public void setDrillCostPerMeter(int drillCostPerMeter){
		this.drillCostPerMeter = drillCostPerMeter;
	}

	public int getDrillCostPerMeter(){
		return drillCostPerMeter;
	}

	public void setHandlingFee(int handlingFee){
		this.handlingFee = handlingFee;
	}

	public int getHandlingFee(){
		return handlingFee;
	}

	public void setInHoleDelay(int inHoleDelay){
		this.inHoleDelay = inHoleDelay;
	}

	public int getInHoleDelay(){
		return inHoleDelay;
	}

	public void setSurfaceRowDelay(int surfaceRowDelay){
		this.surfaceRowDelay = surfaceRowDelay;
	}

	public int getSurfaceRowDelay(){
		return surfaceRowDelay;
	}

	public void setLoadingServices(int loadingServices){
		this.loadingServices = loadingServices;
	}

	public int getLoadingServices(){
		return loadingServices;
	}

	public void setSurfaceHoleDelay(int surfaceHoleDelay){
		this.surfaceHoleDelay = surfaceHoleDelay;
	}

	public int getSurfaceHoleDelay(){
		return surfaceHoleDelay;
	}
}