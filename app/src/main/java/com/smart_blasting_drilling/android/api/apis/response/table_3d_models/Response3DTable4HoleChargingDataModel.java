package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.utils.StringUtill;

public class Response3DTable4HoleChargingDataModel implements Serializable {

	@SerializedName("HoleDiameter")
	private String holeDiameter;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("HoleDepth")
	private String holeDepth;

	@SerializedName("Hole Status")
	private String holeStatus;

	@SerializedName("HoleDelay")
	private String holeDelay;

	@SerializedName("Burden")
	private String burden;

	@SerializedName("DeckLength")
	private String deckLength;

	@SerializedName("ChargeLength")
	private String chargeLength;

	@SerializedName("ChargeTypeArray")
	private String chargeTypeArrayStr;

	private List<ChargeTypeArrayItem> chargeTypeArray;

	@SerializedName("HoleType")
	private String holeType;

	@SerializedName("HoleID")
	private String holeID;

	@SerializedName("InHoleDelay")
	private String inHoleDelay;

	@SerializedName("BlockLength")
	private String blockLength;

	@SerializedName("TielineID")
	private String tielineID;

	@SerializedName("Subgrade")
	private String subgrade;

	@SerializedName("TopZ")
	private String topZ;

	@SerializedName("VerticalDip")
	private String verticalDip;

	@SerializedName("TopY")
	private String topY;

	@SerializedName("TopX")
	private String topX;

	@SerializedName("HoleNo")
	private String holeNo;

	@SerializedName("Spacing")
	private String spacing;

	@SerializedName("RowNo")
	private String rowNo;

	@SerializedName("TotalCharge")
	private String totalCharge;

	@SerializedName("StemmingLength")
	private String stemmingLength;

	@SerializedName("BottomY")
	private String bottomY;

	@SerializedName("BottomZ")
	private String bottomZ;

	@SerializedName("BottomX")
	private String bottomX;

	@SerializedName("Block")
	private String block;

	@SerializedName("WaterDepth")
	private String waterDepth;

	public void setHoleDiameter(String holeDiameter){
		this.holeDiameter = holeDiameter;
	}

	public String getHoleDiameter(){
		return holeDiameter;
	}

	public void setDesignId(String designId){
		this.designId = designId;
	}

	public String getDesignId(){
		return designId;
	}

	public void setHoleDepth(String holeDepth){
		this.holeDepth = holeDepth;
	}

	public String getHoleDepth(){
		return holeDepth;
	}

	public void setHoleDelay(String holeDelay){
		this.holeDelay = holeDelay;
	}

	public String getHoleDelay(){
		return holeDelay;
	}

	public void setBurden(String burden){
		this.burden = burden;
	}

	public String getBurden(){
		return burden;
	}

	public void setDeckLength(String deckLength){
		this.deckLength = deckLength;
	}

	public String getDeckLength(){
		return deckLength;
	}

	public void setChargeLength(String chargeLength){
		this.chargeLength = chargeLength;
	}

	public String getChargeLength(){
		return chargeLength;
	}

	public void setChargeTypeArray(String chargeTypeArrayStr){
		this.chargeTypeArrayStr = chargeTypeArrayStr;
		this.chargeTypeArray = new Gson().fromJson(chargeTypeArrayStr, new TypeToken<List<ChargeTypeArrayItem>>(){}.getType());
	}

	public List<ChargeTypeArrayItem> getChargeTypeArray(){
		this.chargeTypeArray = new Gson().fromJson(chargeTypeArrayStr, new TypeToken<List<ChargeTypeArrayItem>>(){}.getType());
		return chargeTypeArray;
	}

	public void setHoleType(String holeType){
		this.holeType = holeType;
	}

	public int getHoleType(){
		return Integer.parseInt(holeType);
	}

	public void setHoleID(String holeID){
		this.holeID = holeID;
	}

	public String getHoleID(){
		return holeID;
	}

	public void setInHoleDelay(String inHoleDelay){
		this.inHoleDelay = inHoleDelay;
	}

	public String getInHoleDelay(){
		return inHoleDelay;
	}

	public void setBlockLength(String blockLength){
		this.blockLength = blockLength;
	}

	public String getBlockLength(){
		return blockLength;
	}

	public void setTielineID(String tielineID){
		this.tielineID = tielineID;
	}

	public String getTielineID(){
		return tielineID;
	}

	public void setSubgrade(String subgrade){
		this.subgrade = subgrade;
	}

	public String getSubgrade(){
		return subgrade;
	}

	public void setTopZ(String topZ){
		this.topZ = topZ;
	}

	public String getTopZ(){
		return StringUtill.isEmpty(topZ) ? "0.0" : topZ;
	}

	public void setVerticalDip(String verticalDip){
		this.verticalDip = verticalDip;
	}

	public String getVerticalDip(){
		return verticalDip;
	}

	public void setTopY(String topY){
		this.topY = topY;
	}

	public String getTopY(){
		return StringUtill.isEmpty(topY) ? "0.0" : topY;
	}

	public void setTopX(String topX){
		this.topX = topX;
	}

	public String getTopX(){
		return StringUtill.isEmpty(topX) ? String.valueOf(0.0) : topX;
	}

	public void setHoleNo(String holeNo){
		this.holeNo = holeNo;
	}

	public int getHoleNo(){
		return Integer.parseInt(holeNo);
	}

	public void setSpacing(String spacing){
		this.spacing = spacing;
	}

	public String getSpacing(){
		return spacing;
	}

	public void setRowNo(String rowNo){
		this.rowNo = rowNo;
	}

	public int getRowNo(){
		return Integer.parseInt(rowNo);
	}

	public void setTotalCharge(String totalCharge){
		this.totalCharge = totalCharge;
	}

	public String getTotalCharge(){
		return totalCharge;
	}

	public void setStemmingLength(String stemmingLength){
		this.stemmingLength = stemmingLength;
	}

	public String getStemmingLength(){
		return stemmingLength;
	}

	public void setBottomY(String bottomY){
		this.bottomY = bottomY;
	}

	public String getBottomY(){
		return StringUtill.isEmpty(bottomY) ? "0.0" : bottomY;
	}

	public void setBottomZ(String bottomZ){
		this.bottomZ = bottomZ;
	}

	public String getBottomZ(){
		return StringUtill.isEmpty(bottomZ) ? "0.0" : bottomZ;
	}

	public void setBottomX(String bottomX){
		this.bottomX = bottomX;
	}

	public String getBottomX(){
		return StringUtill.isEmpty(bottomX) ? "0.0" : bottomX;
	}

	public void setBlock(String block){
		this.block = block;
	}

	public String getBlock(){
		return block;
	}

	public void setWaterDepth(String waterDepth){
		this.waterDepth = waterDepth;
	}

	public String getWaterDepth(){
		return waterDepth;
	}

	public String getHoleStatus() {
		return holeStatus == null ? "Pending Holes" : holeStatus;
	}

	public void setHoleStatus(String holeStatus) {
		this.holeStatus = holeStatus;
	}

	public String getChargeTypeArrayStr() {
		return chargeTypeArrayStr;
	}

	public void setChargeTypeArrayStr(String chargeTypeArrayStr) {
		this.chargeTypeArrayStr = chargeTypeArrayStr;
	}

	public void setChargeTypeArray(List<ChargeTypeArrayItem> chargeTypeArray) {
		this.chargeTypeArray = chargeTypeArray;
	}
}