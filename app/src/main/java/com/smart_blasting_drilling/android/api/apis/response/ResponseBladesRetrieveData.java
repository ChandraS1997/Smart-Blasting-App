package com.smart_blasting_drilling.android.api.apis.response;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class ResponseBladesRetrieveData{

	@PrimaryKey(autoGenerate = true)
	public int id;

	@SerializedName("PitName")
	@ColumnInfo(name = "PitName")
	private String pitName;

	@SerializedName("ZoneName")
	@ColumnInfo(name = "ZoneName")
	private String zoneName;

	@SerializedName("DesignId")
	@ColumnInfo(name = "DesignId")
	private String designId;

	@SerializedName("DesignCode")
	@ColumnInfo(name = "DesignCode")
	private String designCode;

	@SerializedName("DesignName")
	@ColumnInfo(name = "DesignName")
	private String designName;

	@SerializedName("DesignDateTime")
	@ColumnInfo(name = "DesignDateTime")
	private String designDateTime;

	@SerializedName("MineName")
	@ColumnInfo(name = "MineName")
	private String mineName;

	@SerializedName("BenchName")
	@ColumnInfo(name = "BenchName")
	private String benchName;

	@SerializedName("RockName")
	@ColumnInfo(name = "RockName")
	private String rockName;

	@SerializedName("PcntUnderSize")
	@ColumnInfo(name = "PcntUnderSize")
	private String pcntUnderSize;

	@SerializedName("BlastPlan")
	@ColumnInfo(name = "BlastPlan")
	private String blastPlan;

	@SerializedName("AirVibration")
	@ColumnInfo(name = "AirVibration")
	private String airVibration;

	@SerializedName("BI")
	@ColumnInfo(name = "BI")
	private String bI;

	@SerializedName("PcntOverSize")
	@ColumnInfo(name = "PcntOverSize")
	private String pcntOverSize;

	@SerializedName("UniformExp")
	@ColumnInfo(name = "UniformExp")
	private String uniformExp;

	@SerializedName("MatSize")
	@ColumnInfo(name = "MatSize")
	private String matSize;

	@SerializedName("GroundVibration")
	@ColumnInfo(name = "GroundVibration")
	private String groundVibration;

	@SerializedName("BoosterCharge")
	@ColumnInfo(name = "BoosterCharge")
	private String boosterCharge;

	@SerializedName("PcntOptSize")
	@ColumnInfo(name = "PcntOptSize")
	private String pcntOptSize;

	@SerializedName("BackThrow")
	@ColumnInfo(name = "BackThrow")
	private String backThrow;

	@SerializedName("BottomCharge")
	@ColumnInfo(name = "BottomCharge")
	private String bottomCharge;

	@SerializedName("ColCharge")
	@ColumnInfo(name = "ColCharge")
	private String colCharge;

	@SerializedName("FrontThrow")
	@ColumnInfo(name = "FrontThrow")
	private String frontThrow;

	@SerializedName("ChrctSize")
	@ColumnInfo(name = "ChrctSize")
	private String chrctSize;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
}