package com.smart_blasting_drilling.android.api.apis.response.hole_tables;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;

public class AllTablesData implements Serializable {

	@SerializedName("Table2")
	private List<ResponseHoleDetailData> table2;

	public List<ResponseHoleDetailData> getTable2() {
		return table2;
	}

	public void setTable2(List<ResponseHoleDetailData> table2) {
		this.table2 = table2;
	}
}