package com.smart_blasting_drilling.android.api.apis.response;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ResponseAllRecordData implements Serializable {

    @SerializedName("field_info")
    String fieldInfo;

    @SerializedName("resultset")
    List<JsonArray> resultSetList;

    public ResponseAllRecordData(String fieldInfo, List<JsonArray> resultSetList) {
        this.fieldInfo = fieldInfo;
        this.resultSetList = resultSetList;
    }

    public ResponseAllRecordData() {
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public List<JsonArray> getResultSetList() {
        return resultSetList;
    }

    public void setResultSetList(List<JsonArray> resultSetList) {
        this.resultSetList = resultSetList;
    }
}
