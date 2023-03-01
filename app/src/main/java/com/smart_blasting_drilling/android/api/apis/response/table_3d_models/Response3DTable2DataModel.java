package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response3DTable2DataModel implements Serializable {

	@SerializedName("PatternType")
	private String patternType;

	@SerializedName("Subgrade")
	private String subgrade;

	@SerializedName("HoleDiameter")
	private String holeDiameter;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("HolesPerRow")
	private String holesPerRow;

	@SerializedName("Position")
	private Position position;

	@SerializedName("BenchHeight")
	private String benchHeight;

	@SerializedName("Spacing")
	private String spacing;

	@SerializedName("HoleDepth")
	private String holeDepth;

	@SerializedName("Burden")
	private String burden;

	@SerializedName("Rotation")
	private Rotation rotation;

	@SerializedName("HoleAngle")
	private String holeAngle;

	@SerializedName("TotalRows")
	private String totalRows;

	@SerializedName("PatternColor")
	private String patternColor;

	@SerializedName("isReset")
	private String isReset;

	public void setPatternType(String patternType){
		this.patternType = patternType;
	}

	public String getPatternType(){
		return patternType;
	}

	public void setSubgrade(String subgrade){
		this.subgrade = subgrade;
	}

	public String getSubgrade(){
		return subgrade;
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

	public void setHolesPerRow(String holesPerRow){
		this.holesPerRow = holesPerRow;
	}

	public String getHolesPerRow(){
		return holesPerRow;
	}

	public void setPosition(Position position){
		this.position = position;
	}

	public Position getPosition(){
		return position;
	}

	public void setBenchHeight(String benchHeight){
		this.benchHeight = benchHeight;
	}

	public String getBenchHeight(){
		return benchHeight;
	}

	public void setSpacing(String spacing){
		this.spacing = spacing;
	}

	public String getSpacing(){
		return spacing;
	}

	public void setHoleDepth(String holeDepth){
		this.holeDepth = holeDepth;
	}

	public String getHoleDepth(){
		return holeDepth;
	}

	public void setBurden(String burden){
		this.burden = burden;
	}

	public String getBurden(){
		return burden;
	}

	public void setRotation(Rotation rotation){
		this.rotation = rotation;
	}

	public Rotation getRotation(){
		return rotation;
	}

	public void setHoleAngle(String holeAngle){
		this.holeAngle = holeAngle;
	}

	public String getHoleAngle(){
		return holeAngle;
	}

	public void setTotalRows(String totalRows){
		this.totalRows = totalRows;
	}

	public String getTotalRows(){
		return totalRows;
	}

	public void setPatternColor(String patternColor){
		this.patternColor = patternColor;
	}

	public String getPatternColor(){
		return patternColor;
	}

	public void setIsReset(String isReset){
		this.isReset = isReset;
	}

	public String getIsReset(){
		return isReset;
	}
}