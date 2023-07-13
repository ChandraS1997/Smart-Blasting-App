package com.mineexcellence.sblastingapp.ui.models;

import java.util.List;

public class InitiatingModelAllListDataModel {

    String designId;

    List<InitiatingDeviceModel> electronicDetonatorList;
    List<InitiatingDeviceModel> downTheHoleList;
    List<InitiatingDeviceModel> tldRowToRowList;
    List<InitiatingDeviceModel> tldHoleToHoleList;

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public List<InitiatingDeviceModel> getElectronicDetonatorList() {
        return electronicDetonatorList;
    }

    public void setElectronicDetonatorList(List<InitiatingDeviceModel> electronicDetonatorList) {
        this.electronicDetonatorList = electronicDetonatorList;
    }

    public List<InitiatingDeviceModel> getDownTheHoleList() {
        return downTheHoleList;
    }

    public void setDownTheHoleList(List<InitiatingDeviceModel> downTheHoleList) {
        this.downTheHoleList = downTheHoleList;
    }

    public List<InitiatingDeviceModel> getTldRowToRowList() {
        return tldRowToRowList;
    }

    public void setTldRowToRowList(List<InitiatingDeviceModel> tldRowToRowList) {
        this.tldRowToRowList = tldRowToRowList;
    }

    public List<InitiatingDeviceModel> getTldHoleToHoleList() {
        return tldHoleToHoleList;
    }

    public void setTldHoleToHoleList(List<InitiatingDeviceModel> tldHoleToHoleList) {
        this.tldHoleToHoleList = tldHoleToHoleList;
    }
}
