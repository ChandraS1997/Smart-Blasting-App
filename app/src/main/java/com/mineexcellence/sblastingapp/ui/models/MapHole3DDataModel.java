package com.mineexcellence.sblastingapp.ui.models;

import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;

import java.util.List;

public class MapHole3DDataModel {

    String rowId;
    List<Response3DTable4HoleChargingDataModel> holeDetailDataList;

    public MapHole3DDataModel() {
    }

    public MapHole3DDataModel(String rowId, List<Response3DTable4HoleChargingDataModel> holeDetailDataList) {
        this.rowId = rowId;
        this.holeDetailDataList = holeDetailDataList;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public List<Response3DTable4HoleChargingDataModel> getHoleDetailDataList() {
        return holeDetailDataList;
    }

    public void setHoleDetailDataList(List<Response3DTable4HoleChargingDataModel> holeDetailDataList) {
        this.holeDetailDataList = holeDetailDataList;
    }

}
