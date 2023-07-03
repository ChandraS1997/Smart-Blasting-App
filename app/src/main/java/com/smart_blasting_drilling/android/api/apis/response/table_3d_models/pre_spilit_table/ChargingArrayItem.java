package com.smart_blasting_drilling.android.api.apis.response.table_3d_models.pre_spilit_table;

import com.google.gson.annotations.SerializedName;

public class ChargingArrayItem{

	@SerializedName("cost")
	private Object cost;

	@SerializedName("color")
	private String color;

	@SerializedName("percentage")
	private Object percentage;

	@SerializedName("name")
	private String name;

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

	public void setCost(Object cost){
		this.cost = cost;
	}

	public Object getCost(){
		return cost;
	}

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
		return percentage;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLength(Object length){
		this.length = length;
	}

	public Object getLength(){
		return length;
	}

	public void setWeight(Object weight){
		this.weight = weight;
	}

	public Object getWeight(){
		return weight;
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
}