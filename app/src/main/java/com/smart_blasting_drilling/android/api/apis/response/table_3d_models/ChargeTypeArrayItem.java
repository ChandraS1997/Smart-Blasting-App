package com.smart_blasting_drilling.android.api.apis.response.table_3d_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChargeTypeArrayItem implements Serializable {

	@SerializedName("color")
	private String color;

	@SerializedName("percentage")
	private Object percentage;

	@SerializedName("length")
	private Object length;

	@SerializedName("weight")
	private Object weight;

	@SerializedName("prodId")
	private int prodId;

	@SerializedName("type")
	private String type;

	@SerializedName("prodType")
	private int prodType;

	@SerializedName("cost")
	private Object cost;

	@SerializedName("name")
	private String name;

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setPercentage(Object percentage){
		this.percentage = percentage;
	}

	public Object getPercentage(){
		return percentage != null ? percentage : "";
	}

	public void setLength(Object length){
		this.length = length;
	}

	public double getLength(){
		return length != null ? (double) length : 0.0;
	}

	public void setWeight(Object weight){
		this.weight = weight;
	}

	public double getWeight(){
		return weight != null ? (double) weight : 0.0;
	}

	public void setProdId(int prodId){
		this.prodId = prodId;
	}

	public int getProdId(){
		return prodId;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setProdType(int prodType){
		this.prodType = prodType;
	}

	public int getProdType(){
		return prodType;
	}

	public void setCost(Object cost){
		this.cost = cost;
	}

	public int getCost(){
		return cost != null ? (int) cost : 0;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}