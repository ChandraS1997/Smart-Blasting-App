package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class Response3DTable17DataModel{

	@SerializedName("RowColor")
	private String rowColor;

	@SerializedName("Connections")
	private JsonElement connections;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("HoleColor")
	private String holeColor;

	@SerializedName("InHoleDelayArr")
	private JsonElement inHoleDelayArr;

	@SerializedName("FinalLines")
	private JsonElement finalLines;

	@SerializedName("HoleTime")
	private String holeTime;

	@SerializedName("Mode")
	private String mode;

	@SerializedName("AdvancedMode")
	private String advancedMode;

	@SerializedName("AllElemArr")
	private JsonElement allElemArr;

	@SerializedName("AllSelectedTime")
	private JsonElement allSelectedTime;

	@SerializedName("AllSelected")
	private JsonElement allSelected;

	@SerializedName("InHoleDelay")
	private String inHoleDelay;

	@SerializedName("InitHoleID")
	private JsonElement initHoleID;

	@SerializedName("RowTime")
	private String rowTime;

	@SerializedName("TieLineMode")
	private String tieLineMode;

	public void setRowColor(String rowColor){
		this.rowColor = rowColor;
	}

	public String getRowColor(){
		return rowColor;
	}

	public void setConnections(JsonElement connections){
		this.connections = connections;
	}

	public JsonElement getConnections(){
		return connections;
	}

	public void setDesignId(String designId){
		this.designId = designId;
	}

	public String getDesignId(){
		return designId;
	}

	public void setHoleColor(String holeColor){
		this.holeColor = holeColor;
	}

	public String getHoleColor(){
		return holeColor;
	}

	public void setInHoleDelayArr(JsonElement inHoleDelayArr){
		this.inHoleDelayArr = inHoleDelayArr;
	}

	public JsonElement getInHoleDelayArr(){
		return inHoleDelayArr;
	}

	public void setFinalLines(JsonElement finalLines){
		this.finalLines = finalLines;
	}

	public JsonElement getFinalLines(){
		return finalLines;
	}

	public void setHoleTime(String holeTime){
		this.holeTime = holeTime;
	}

	public String getHoleTime(){
		return holeTime;
	}

	public void setMode(String mode){
		this.mode = mode;
	}

	public String getMode(){
		return mode;
	}

	public void setAdvancedMode(String advancedMode){
		this.advancedMode = advancedMode;
	}

	public String getAdvancedMode(){
		return advancedMode;
	}

	public void setAllElemArr(JsonElement allElemArr){
		this.allElemArr = allElemArr;
	}

	public JsonElement getAllElemArr(){
		return allElemArr;
	}

	public void setAllSelectedTime(JsonElement allSelectedTime){
		this.allSelectedTime = allSelectedTime;
	}

	public JsonElement getAllSelectedTime(){
		return allSelectedTime;
	}

	public void setAllSelected(JsonElement allSelected){
		this.allSelected = allSelected;
	}

	public JsonElement getAllSelected(){
		return allSelected;
	}

	public void setInHoleDelay(String inHoleDelay){
		this.inHoleDelay = inHoleDelay;
	}

	public String getInHoleDelay(){
		return inHoleDelay;
	}

	public void setInitHoleID(JsonElement initHoleID){
		this.initHoleID = initHoleID;
	}

	public JsonElement getInitHoleID(){
		return initHoleID;
	}

	public void setRowTime(String rowTime){
		this.rowTime = rowTime;
	}

	public String getRowTime(){
		return rowTime;
	}

	public void setTieLineMode(String tieLineMode){
		this.tieLineMode = tieLineMode;
	}

	public String getTieLineMode(){
		return tieLineMode;
	}
}