package com.smart_blasting_drilling.android.ui.models;

import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;

import java.util.List;

public class MapHoleDataModel {

    int rowId;
    List<ResponseHoleDetailData> holeDetailDataList;

    public MapHoleDataModel() {
    }

    public MapHoleDataModel(int rowId, List<ResponseHoleDetailData> holeDetailDataList) {
        this.rowId = rowId;
        this.holeDetailDataList = holeDetailDataList;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public List<ResponseHoleDetailData> getHoleDetailDataList() {
        return holeDetailDataList;
    }

    public void setHoleDetailDataList(List<ResponseHoleDetailData> holeDetailDataList) {
        this.holeDetailDataList = holeDetailDataList;
    }
}
