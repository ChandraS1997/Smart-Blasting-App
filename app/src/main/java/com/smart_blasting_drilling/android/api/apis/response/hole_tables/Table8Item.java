package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

public class Table8Item{

	@SerializedName("PatternType")
	private int patternType;

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("PatternImgLoc")
	private String patternImgLoc;

	@SerializedName("HolesPerRow")
	private int holesPerRow;

	@SerializedName("TotalRows")
	private int totalRows;

	@SerializedName("isReset")
	private boolean isReset;

	public void setPatternType(int patternType){
		this.patternType = patternType;
	}

	public int getPatternType(){
		return patternType;
	}

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setPatternImgLoc(String patternImgLoc){
		this.patternImgLoc = patternImgLoc;
	}

	public String getPatternImgLoc(){
		return patternImgLoc;
	}

	public void setHolesPerRow(int holesPerRow){
		this.holesPerRow = holesPerRow;
	}

	public int getHolesPerRow(){
		return holesPerRow;
	}

	public void setTotalRows(int totalRows){
		this.totalRows = totalRows;
	}

	public int getTotalRows(){
		return totalRows;
	}

	public void setIsReset(boolean isReset){
		this.isReset = isReset;
	}

	public boolean isIsReset(){
		return isReset;
	}
}