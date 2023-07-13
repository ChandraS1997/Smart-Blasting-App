package com.mineexcellence.sblastingapp.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response3DTable3DataModel implements Serializable {

	@SerializedName("HoleAngle")
	private String holeAngle;

	@SerializedName("Subgrade")
	private String subgrade;

	@SerializedName("HoleDiameter")
	private String holeDiameter;

	@SerializedName("DesignId")
	private String designId;

	@SerializedName("Spacing")
	private String spacing;

	@SerializedName("HoleDepth")
	private String holeDepth;

	@SerializedName("Burden")
	private String burden;

	@SerializedName("RowNo")
	private String rowNo;

	@SerializedName("TotalHoles")
	private String totalHoles;

	public void setHoleAngle(String holeAngle){
		this.holeAngle = holeAngle;
	}

	public String getHoleAngle(){
		return holeAngle;
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

	public void setRowNo(String rowNo){
		this.rowNo = rowNo;
	}

	public String getRowNo(){
		return rowNo;
	}

	public void setTotalHoles(String totalHoles){
		this.totalHoles = totalHoles;
	}

	public String getTotalHoles(){
		return totalHoles;
	}
}