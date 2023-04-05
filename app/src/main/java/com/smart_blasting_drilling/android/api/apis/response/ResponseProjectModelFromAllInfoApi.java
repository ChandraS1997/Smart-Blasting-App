package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseProjectModelFromAllInfoApi implements Serializable {

	@SerializedName("PitName")
	private String pitName;

	@SerializedName("BlastQty")
	private int blastQty;

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("DesignName")
	private String designName;

	@SerializedName("MineLat")
	private String mineLat;

	@SerializedName("DesignDateTime")
	private String designDateTime;

	@SerializedName("isPatternImport")
	private String isPatternImport;

	@SerializedName("UnitSystem")
	private int unitSystem;

	@SerializedName("MineName")
	private String mineName;

	@SerializedName("BenchName")
	private String benchName;

	@SerializedName("MineId")
	private int mineId;

	@SerializedName("FaceHeight")
	private int faceHeight;

	@SerializedName("CoordinateType")
	private int coordinateType;

	@SerializedName("ZoneName")
	private String zoneName;

	@SerializedName("PowderFactorUnitId")
	private int powderFactorUnitId;

	@SerializedName("BlastDesignMethod")
	private int blastDesignMethod;

	@SerializedName("BenchID")
	private int benchID;

	@SerializedName("BlastQtyUnit")
	private String blastQtyUnit;

	@SerializedName("FaceLength")
	private int faceLength;

	@SerializedName("MineLong")
	private String mineLong;

	@SerializedName("DesignCode")
	private String designCode;

	@SerializedName("ZoneId")
	private int zoneId;

	@SerializedName("BlastLocation")
	private String blastLocation;

	@SerializedName("PowderFactor")
	private int powderFactor;

	@SerializedName("FaceWidth")
	private int faceWidth;

	@SerializedName("PitId")
	private int pitId;

	@SerializedName("BlastingTypeId")
	private int blastingTypeId;

	@SerializedName("MineLogo")
	private String mineLogo;

	@SerializedName("FaceCoordinate")
	private Object faceCoordinate;

	@SerializedName("MeanFragmentationSize")
	private int meanFragmentationSize;

	@SerializedName("IsQty")
	private boolean isQty;

	@SerializedName("ChargePerDelay")
	private int chargePerDelay;

	@SerializedName("BlastingType")
	private String blastingType;

	@SerializedName("BIMSId")
	private Object bimsId;

	@SerializedName("DRIMSId")
	private Object drimsId;

	public boolean isQty() {
		return isQty;
	}

	public void setQty(boolean qty) {
		isQty = qty;
	}

	public Object getBimsId() {
		if (bimsId == null)
			return "";
		return bimsId;
	}

	public void setBimsId(Object bimsId) {
		this.bimsId = bimsId;
	}

	public Object getDrimsId() {
		if (drimsId == null)
			return "";
		return drimsId;
	}

	public void setDrimsId(Object drimsId) {
		this.drimsId = drimsId;
	}

	public void setPitName(String pitName){
		this.pitName = pitName;
	}

	public String getPitName(){
		return pitName;
	}

	public void setBlastQty(int blastQty){
		this.blastQty = blastQty;
	}

	public int getBlastQty(){
		return blastQty;
	}

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setDesignName(String designName){
		this.designName = designName;
	}

	public String getDesignName(){
		return designName;
	}

	public void setMineLat(String mineLat){
		this.mineLat = mineLat;
	}

	public String getMineLat(){
		return mineLat;
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

	public void setUnitSystem(int unitSystem){
		this.unitSystem = unitSystem;
	}

	public int getUnitSystem(){
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

	public void setMineId(int mineId){
		this.mineId = mineId;
	}

	public int getMineId(){
		return mineId;
	}

	public void setFaceHeight(int faceHeight){
		this.faceHeight = faceHeight;
	}

	public int getFaceHeight(){
		return faceHeight;
	}

	public void setCoordinateType(int coordinateType){
		this.coordinateType = coordinateType;
	}

	public int getCoordinateType(){
		return coordinateType;
	}

	public void setZoneName(String zoneName){
		this.zoneName = zoneName;
	}

	public String getZoneName(){
		return zoneName;
	}

	public void setPowderFactorUnitId(int powderFactorUnitId){
		this.powderFactorUnitId = powderFactorUnitId;
	}

	public int getPowderFactorUnitId(){
		return powderFactorUnitId;
	}

	public void setBlastDesignMethod(int blastDesignMethod){
		this.blastDesignMethod = blastDesignMethod;
	}

	public int getBlastDesignMethod(){
		return blastDesignMethod;
	}

	public void setBenchID(int benchID){
		this.benchID = benchID;
	}

	public int getBenchID(){
		return benchID;
	}

	public void setBlastQtyUnit(String blastQtyUnit){
		this.blastQtyUnit = blastQtyUnit;
	}

	public String getBlastQtyUnit(){
		return blastQtyUnit;
	}

	public void setFaceLength(int faceLength){
		this.faceLength = faceLength;
	}

	public int getFaceLength(){
		return faceLength;
	}

	public void setMineLong(String mineLong){
		this.mineLong = mineLong;
	}

	public String getMineLong(){
		return mineLong;
	}

	public void setDesignCode(String designCode){
		this.designCode = designCode;
	}

	public String getDesignCode(){
		return designCode;
	}

	public void setZoneId(int zoneId){
		this.zoneId = zoneId;
	}

	public int getZoneId(){
		return zoneId;
	}

	public void setBlastLocation(String blastLocation){
		this.blastLocation = blastLocation;
	}

	public String getBlastLocation(){
		return blastLocation;
	}

	public void setPowderFactor(int powderFactor){
		this.powderFactor = powderFactor;
	}

	public int getPowderFactor(){
		return powderFactor;
	}

	public void setFaceWidth(int faceWidth){
		this.faceWidth = faceWidth;
	}

	public int getFaceWidth(){
		return faceWidth;
	}

	public void setPitId(int pitId){
		this.pitId = pitId;
	}

	public int getPitId(){
		return pitId;
	}

	public void setBlastingTypeId(int blastingTypeId){
		this.blastingTypeId = blastingTypeId;
	}

	public int getBlastingTypeId(){
		return blastingTypeId;
	}

	public void setMineLogo(String mineLogo){
		this.mineLogo = mineLogo;
	}

	public String getMineLogo(){
		return mineLogo;
	}

	public void setFaceCoordinate(Object faceCoordinate){
		this.faceCoordinate = faceCoordinate;
	}

	public Object getFaceCoordinate(){
		return faceCoordinate;
	}

	public void setMeanFragmentationSize(int meanFragmentationSize){
		this.meanFragmentationSize = meanFragmentationSize;
	}

	public int getMeanFragmentationSize(){
		return meanFragmentationSize;
	}

	public void setIsQty(boolean isQty){
		this.isQty = isQty;
	}

	public boolean isIsQty(){
		return isQty;
	}

	public void setChargePerDelay(int chargePerDelay){
		this.chargePerDelay = chargePerDelay;
	}

	public int getChargePerDelay(){
		return chargePerDelay;
	}

	public void setBlastingType(String blastingType){
		this.blastingType = blastingType;
	}

	public String getBlastingType(){
		return blastingType;
	}
}