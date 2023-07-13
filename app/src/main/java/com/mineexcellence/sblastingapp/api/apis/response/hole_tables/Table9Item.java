package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table9Item implements Serializable {

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("ProdName")
	private String prodName;

	@SerializedName("ProdType")
	private String prodType;

	@SerializedName("ProdQty")
	private int prodQty;

	@SerializedName("ProdCost")
	private Object prodCost;

	@SerializedName("ProdId")
	private int prodId;

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setProdName(String prodName){
		this.prodName = prodName;
	}

	public String getProdName(){
		return prodName;
	}

	public void setProdType(String prodType){
		this.prodType = prodType;
	}

	public String getProdType(){
		return prodType;
	}

	public void setProdQty(int prodQty){
		this.prodQty = prodQty;
	}

	public int getProdQty(){
		return prodQty;
	}

	public void setProdCost(Object prodCost){
		this.prodCost = prodCost;
	}

	public Object getProdCost(){
		return prodCost;
	}

	public void setProdId(int prodId){
		this.prodId = prodId;
	}

	public int getProdId(){
		return prodId;
	}
}