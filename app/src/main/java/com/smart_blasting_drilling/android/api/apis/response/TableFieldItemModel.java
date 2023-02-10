package com.smart_blasting_drilling.android.api.apis.response;

import com.smart_blasting_drilling.android.ui.models.TableEditModel;

import java.util.List;

public class TableFieldItemModel {

    List<TableEditModel> tableEditModelList;
    List<ResponseHoleDetailData> holeDetailDataList;

    public TableFieldItemModel() {
    }

    public TableFieldItemModel(List<TableEditModel> tableEditModelList) {
        this.tableEditModelList = tableEditModelList;
    }

    public List<TableEditModel> getTableEditModelList() {
        return tableEditModelList;
    }

    public void setTableEditModelList(List<TableEditModel> tableEditModelList) {
        this.tableEditModelList = tableEditModelList;
    }

    public List<ResponseHoleDetailData> getHoleDetailDataList() {
        return holeDetailDataList;
    }

    public void setHoleDetailDataList(List<ResponseHoleDetailData> holeDetailDataList) {
        this.holeDetailDataList = holeDetailDataList;
    }
}
