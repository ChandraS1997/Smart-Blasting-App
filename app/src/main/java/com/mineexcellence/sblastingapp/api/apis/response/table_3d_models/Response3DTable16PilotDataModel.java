package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

public class Response3DTable16PilotDataModel implements Serializable {

	@SerializedName("HoleDiameter")
	private String holeDiameter;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("HoleDepth")
	private String holeDepth;

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

	@SerializedName("Spacing")
	private String spacing;

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

	private String northing, easting;

	public String getNorthing() {
		return northing;
	}

	public void setNorthing(String northing) {
		this.northing = northing;
	}

	public String getEasting() {
		return easting;
	}

	public void setEasting(String easting) {
		this.easting = easting;
	}

	public List<ChargeTypeArrayItem> getChargeTypeArray() {
		this.chargeTypeArray = new Gson().fromJson(chargeTypeArrayStr, new TypeToken<List<ChargeTypeArrayItem>>(){}.getType());
		return chargeTypeArray;
	}

	public void setChargeTypeArray(List<ChargeTypeArrayItem> chargeTypeArray) {
		this.chargeTypeArray = chargeTypeArray;
		this.chargeTypeArray = new Gson().fromJson(chargeTypeArrayStr, new TypeToken<List<ChargeTypeArrayItem>>(){}.getType());
	}

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

	public void setChargeTypeArrayStr(String chargeTypeArrayStr){
		this.chargeTypeArrayStr = chargeTypeArrayStr;
	}

	public String getChargeTypeArrayStr(){
		return chargeTypeArrayStr;
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
		return topZ;
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
		return topY;
	}

	public void setTopX(String topX){
		this.topX = topX;
	}

	public String getTopX(){
		return topX;
	}

	public void setSpacing(String spacing){
		this.spacing = spacing;
	}

	public String getSpacing(){
		return spacing;
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
		return bottomY;
	}

	public void setBottomZ(String bottomZ){
		this.bottomZ = bottomZ;
	}

	public String getBottomZ(){
		return bottomZ;
	}

	public void setBottomX(String bottomX){
		this.bottomX = bottomX;
	}

	public String getBottomX(){
		return bottomX;
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
}