package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseProjectModelFromAllInfoApi;

public class AllTablesData implements Serializable {

	@SerializedName("Table")
	private List<ResponseProjectModelFromAllInfoApi> table;

	@SerializedName("Table2")
	private List<ResponseHoleDetailData> tableHoleData;

	@SerializedName("Table3")
	private List<Table3Item> table3;

	@SerializedName("Table4")
	private List<Table4Item> table4;

	@SerializedName("Table5")
	private List<Table5Item> table5;

	@SerializedName("Table1")
	private List<Table1Item> table1;

	@SerializedName("Table6")
	private List<Table6Item> table6;

	@SerializedName("Table10")
	private List<Table10Item> table10;

	@SerializedName("Table7")
	private List<Table7Item> table7;

	@SerializedName("Table8")
	private List<Table8Item> table8;

	@SerializedName("Table9")
	private List<Table9Item> table9;

	public void setTable6(List<Table6Item> table6){
		this.table6 = table6;
	}

	public List<Table6Item> getTable6(){
		return table6;
	}

	public void setTable10(List<Table10Item> table10){
		this.table10 = table10;
	}

	public List<Table10Item> getTable10(){
		return table10;
	}

	public void setTable7(List<Table7Item> table7){
		this.table7 = table7;
	}

	public List<Table7Item> getTable7(){
		return table7;
	}

	public void setTable8(List<Table8Item> table8){
		this.table8 = table8;
	}

	public List<Table8Item> getTable8(){
		return table8;
	}

	public void setTable9(List<Table9Item> table9){
		this.table9 = table9;
	}

	public List<Table9Item> getTable9(){
		return table9;
	}

	public void setTable(List<ResponseProjectModelFromAllInfoApi> table){
		this.table = table;
	}

	public List<ResponseProjectModelFromAllInfoApi> getTable(){
		return table;
	}

	public void setTable2(List<ResponseHoleDetailData> table2){
		this.tableHoleData = table2;
	}

	public List<ResponseHoleDetailData> getTable2(){
		return tableHoleData;
	}

	public void setTable3(List<Table3Item> table3){
		this.table3 = table3;
	}

	public List<Table3Item> getTable3(){
		return table3;
	}

	public void setTable4(List<Table4Item> table4){
		this.table4 = table4;
	}

	public List<Table4Item> getTable4(){
		return table4;
	}

	public void setTable5(List<Table5Item> table5){
		this.table5 = table5;
	}

	public List<Table5Item> getTable5(){
		return table5;
	}

	public void setTable1(List<Table1Item> table1){
		this.table1 = table1;
	}

	public List<Table1Item> getTable1(){
		return table1;
	}
}