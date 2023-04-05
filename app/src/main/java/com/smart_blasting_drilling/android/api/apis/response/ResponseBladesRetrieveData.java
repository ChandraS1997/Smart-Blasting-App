package com.smart_blasting_drilling.android.api.apis.response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResponseBladesRetrieveData implements Serializable {

	boolean isDownloaded = false;

	@SerializedName("BladesType")
	boolean is3dBlade;

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

	@SerializedName("RockCode")
	private String rockCode;

	@SerializedName("PcntUnderSize")
	private String pcntUnderSize;

	@SerializedName("BlastPlan")
	private String blastPlan;

	@SerializedName("AirVibration")
	private String airVibration;

	@SerializedName("BI")
	private String bI;

	@SerializedName("PcntOverSize")
	private String pcntOverSize;

	@SerializedName("UniformExp")
	private String uniformExp;

	@SerializedName("MatSize")
	private String matSize;

	@SerializedName("GroundVibration")
	private String groundVibration;

	@SerializedName("BoosterCharge")
	private String boosterCharge;

	@SerializedName("PcntOptSize")
	private String pcntOptSize;

	@SerializedName("BackThrow")
	private String backThrow;

	@SerializedName("BottomCharge")
	private String bottomCharge;

	@SerializedName("ColCharge")
	private String colCharge;

	@SerializedName("FrontThrow")
	private String frontThrow;

	@SerializedName("ChrctSize")
	private String chrctSize;

	public boolean isDownloaded() {
		return isDownloaded;
	}

	public void setDownloaded(boolean downloaded) {
		isDownloaded = downloaded;
	}

	public boolean isIs3dBlade() {
		return is3dBlade;
	}

	public void setIs3dBlade(boolean is3dBlade) {
		this.is3dBlade = is3dBlade;
	}

	public void setPcntUnderSize(String pcntUnderSize){
		this.pcntUnderSize = pcntUnderSize;
	}

	public String getPcntUnderSize(){
		return pcntUnderSize;
	}

	public void setBlastPlan(String blastPlan){
		this.blastPlan = blastPlan;
	}

	public String getBlastPlan(){
		return blastPlan;
	}

	public void setAirVibration(String airVibration){
		this.airVibration = airVibration;
	}

	public String getAirVibration(){
		return airVibration;
	}

	public void setBI(String bI){
		this.bI = bI;
	}

	public String getBI(){
		return bI;
	}

	public void setPcntOverSize(String pcntOverSize){
		this.pcntOverSize = pcntOverSize;
	}

	public String getPcntOverSize(){
		return pcntOverSize;
	}

	public void setUniformExp(String uniformExp){
		this.uniformExp = uniformExp;
	}

	public String getUniformExp(){
		return uniformExp;
	}

	public void setMatSize(String matSize){
		this.matSize = matSize;
	}

	public String getMatSize(){
		return matSize;
	}

	public void setGroundVibration(String groundVibration){
		this.groundVibration = groundVibration;
	}

	public String getGroundVibration(){
		return groundVibration;
	}

	public void setBoosterCharge(String boosterCharge){
		this.boosterCharge = boosterCharge;
	}

	public String getBoosterCharge(){
		return boosterCharge;
	}

	public void setPcntOptSize(String pcntOptSize){
		this.pcntOptSize = pcntOptSize;
	}

	public String getPcntOptSize(){
		return pcntOptSize;
	}

	public void setBackThrow(String backThrow){
		this.backThrow = backThrow;
	}

	public String getBackThrow(){
		return backThrow;
	}

	public void setBottomCharge(String bottomCharge){
		this.bottomCharge = bottomCharge;
	}

	public String getBottomCharge(){
		return bottomCharge;
	}

	public void setColCharge(String colCharge){
		this.colCharge = colCharge;
	}

	public String getColCharge(){
		return colCharge;
	}

	public void setFrontThrow(String frontThrow){
		this.frontThrow = frontThrow;
	}

	public String getFrontThrow(){
		return frontThrow;
	}

	public void setChrctSize(String chrctSize){
		this.chrctSize = chrctSize;
	}

	public String getChrctSize(){
		return chrctSize;
	}

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

	public String getRockCode() {
		return rockCode;
	}

	public void setRockCode(String rockCode) {
		this.rockCode = rockCode;
	}
}