package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response3DTable7DesignElementDataModel implements Serializable {

	@SerializedName("RowColor")
	private String rowColor;

	@SerializedName("Connections")
	private List<String> connections;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("HoleColor")
	private String holeColor;

	@SerializedName("InHoleDelayArr")
	private String inHoleDelayArr;

	@SerializedName("FinalLines")
	private List<String> finalLines;

	@SerializedName("HoleTime")
	private String holeTime;

	@SerializedName("Mode")
	private String mode;

	@SerializedName("AllElemArr")
	private List<String> allElemArr;

	@SerializedName("AllSelectedTime")
	private List<Integer> allSelectedTime;

	@SerializedName("AllSelected")
	private List<String> allSelected;

	@SerializedName("InHoleDelay")
	private String inHoleDelay;

	@SerializedName("InitHoleID")
	private List<String> initHoleID;

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

	public void setConnections(List<String> connections){
		this.connections = connections;
	}

	public List<String> getConnections(){
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

	public void setInHoleDelayArr(String inHoleDelayArr){
		this.inHoleDelayArr = inHoleDelayArr;
	}

	public String getInHoleDelayArr(){
		return inHoleDelayArr;
	}

	public void setFinalLines(List<String> finalLines){
		this.finalLines = finalLines;
	}

	public List<String> getFinalLines(){
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

	public void setAllElemArr(List<String> allElemArr){
		this.allElemArr = allElemArr;
	}

	public List<String> getAllElemArr(){
		return allElemArr;
	}

	public void setAllSelectedTime(List<Integer> allSelectedTime){
		this.allSelectedTime = allSelectedTime;
	}

	public List<Integer> getAllSelectedTime(){
		return allSelectedTime;
	}

	public void setAllSelected(List<String> allSelected){
		this.allSelected = allSelected;
	}

	public List<String> getAllSelected(){
		return allSelected;
	}

	public void setInHoleDelay(String inHoleDelay){
		this.inHoleDelay = inHoleDelay;
	}

	public String getInHoleDelay(){
		return inHoleDelay;
	}

	public void setInitHoleID(List<String> initHoleID){
		this.initHoleID = initHoleID;
	}

	public List<String> getInitHoleID(){
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