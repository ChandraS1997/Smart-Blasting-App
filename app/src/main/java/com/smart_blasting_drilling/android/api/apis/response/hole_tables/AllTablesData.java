package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;

public class AllTablesData implements Serializable {

	@SerializedName("Table")
	private List<TableItem> table;

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

	public void setTable(List<TableItem> table){
		this.table = table;
	}

	public List<TableItem> getTable(){
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