package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

public class Table4Item{

	@SerializedName("PcntUnderSize")
	private Object pcntUnderSize;

	@SerializedName("DesignId")
	private int designId;

	@SerializedName("BI")
	private Object bI;

	@SerializedName("PcntOverSize")
	private Object pcntOverSize;

	@SerializedName("PcntOptSize")
	private Object pcntOptSize;

	@SerializedName("FragModel")
	private int fragModel;

	@SerializedName("ChrctSize")
	private Object chrctSize;

	@SerializedName("UniformExp")
	private Object uniformExp;

	@SerializedName("MatSize")
	private Object matSize;

	public void setPcntUnderSize(Object pcntUnderSize){
		this.pcntUnderSize = pcntUnderSize;
	}

	public Object getPcntUnderSize(){
		return pcntUnderSize;
	}

	public void setDesignId(int designId){
		this.designId = designId;
	}

	public int getDesignId(){
		return designId;
	}

	public void setBI(Object bI){
		this.bI = bI;
	}

	public Object getBI(){
		return bI;
	}

	public void setPcntOverSize(Object pcntOverSize){
		this.pcntOverSize = pcntOverSize;
	}

	public Object getPcntOverSize(){
		return pcntOverSize;
	}

	public void setPcntOptSize(Object pcntOptSize){
		this.pcntOptSize = pcntOptSize;
	}

	public Object getPcntOptSize(){
		return pcntOptSize;
	}

	public void setFragModel(int fragModel){
		this.fragModel = fragModel;
	}

	public int getFragModel(){
		return fragModel;
	}

	public void setChrctSize(Object chrctSize){
		this.chrctSize = chrctSize;
	}

	public Object getChrctSize(){
		return chrctSize;
	}

	public void setUniformExp(Object uniformExp){
		this.uniformExp = uniformExp;
	}

	public Object getUniformExp(){
		return uniformExp;
	}

	public void setMatSize(Object matSize){
		this.matSize = matSize;
	}

	public Object getMatSize(){
		return matSize;
	}
}