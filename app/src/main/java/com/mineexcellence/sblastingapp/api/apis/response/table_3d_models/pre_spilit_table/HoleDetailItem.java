package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.pre_spilit_table;

import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.ChargeTypeArrayItem;

public class HoleDetailItem{

	@SerializedName("topEasting")
	private Object topEasting;

	@SerializedName("topNorthing")
	private Object topNorthing;

	@SerializedName("holeDiameter")
	private int holeDiameter;

	@SerializedName("holeType")
	private String holeType;

	@SerializedName("bottomRL")
	private int bottomRL;

	@SerializedName("chargeLength")
	private String chargeLength;

	@SerializedName("totalCharge")
	private String totalCharge;

	@SerializedName("holeDepth")
	private String holeDepth;

	@SerializedName("decking")
	private String decking;

	@SerializedName("bottomNorthing")
	private Object bottomNorthing;

	@SerializedName("bottomEasting")
	private Object bottomEasting;

	@SerializedName("chargingArray")
	private List<ChargeTypeArrayItem> chargingArray;

	@SerializedName("topRL")
	private int topRL;

	@SerializedName("holeId")
	private String holeId;

	@SerializedName("DesignId")
	private String designId;

	public String getDesignId() {
		return designId;
	}

	public void setDesignId(String designId) {
		this.designId = designId;
	}

	public void setTopEasting(Object topEasting){
		this.topEasting = topEasting;
	}

	public Object getTopEasting(){
		return topEasting;
	}

	public void setTopNorthing(Object topNorthing){
		this.topNorthing = topNorthing;
	}

	public Object getTopNorthing(){
		return topNorthing;
	}

	public void setHoleDiameter(int holeDiameter){
		this.holeDiameter = holeDiameter;
	}

	public int getHoleDiameter(){
		return holeDiameter;
	}

	public void setHoleType(String holeType){
		this.holeType = holeType;
	}

	public String getHoleType(){
		return holeType;
	}

	public void setBottomRL(int bottomRL){
		this.bottomRL = bottomRL;
	}

	public int getBottomRL(){
		return bottomRL;
	}

	public void setChargeLength(String chargeLength){
		this.chargeLength = chargeLength;
	}

	public String getChargeLength(){
		return chargeLength;
	}

	public void setTotalCharge(String totalCharge){
		this.totalCharge = totalCharge;
	}

	public String getTotalCharge(){
		return totalCharge;
	}

	public void setHoleDepth(String holeDepth){
		this.holeDepth = holeDepth;
	}

	public String getHoleDepth(){
		return holeDepth;
	}

	public void setDecking(String decking){
		this.decking = decking;
	}

	public String getDecking(){
		return decking;
	}

	public void setBottomNorthing(Object bottomNorthing){
		this.bottomNorthing = bottomNorthing;
	}

	public Object getBottomNorthing(){
		return bottomNorthing;
	}

	public void setBottomEasting(Object bottomEasting){
		this.bottomEasting = bottomEasting;
	}

	public Object getBottomEasting(){
		return bottomEasting;
	}

	public void setChargingArray(List<ChargeTypeArrayItem> chargingArray){
		this.chargingArray = chargingArray;
	}

	public List<ChargeTypeArrayItem> getChargingArray(){
		return chargingArray;
	}

	public void setTopRL(int topRL){
		this.topRL = topRL;
	}

	public int getTopRL(){
		return topRL;
	}

	public void setHoleId(String holeId){
		this.holeId = holeId;
	}

	public String getHoleId(){
		return holeId;
	}
}