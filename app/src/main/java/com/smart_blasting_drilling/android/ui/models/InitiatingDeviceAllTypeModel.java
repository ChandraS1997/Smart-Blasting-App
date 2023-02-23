package com.smart_blasting_drilling.android.ui.models;

import java.util.List;

public class InitiatingDeviceAllTypeModel {
    String deviceName;
    List<InitiatingDeviceModel> deviceModelList;

    public InitiatingDeviceAllTypeModel() {
    }

    public InitiatingDeviceAllTypeModel(String deviceName, List<InitiatingDeviceModel> deviceModelList) {
        this.deviceName = deviceName;
        this.deviceModelList = deviceModelList;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public List<InitiatingDeviceModel> getDeviceModelList() {
        return deviceModelList;
    }

    public void setDeviceModelList(List<InitiatingDeviceModel> deviceModelList) {
        this.deviceModelList = deviceModelList;
    }

    public void addDeviceModelList(List<InitiatingDeviceModel> deviceModelList) {
        if (this.deviceModelList == null) {
            this.deviceModelList = deviceModelList;
        } else {
            this.deviceModelList.addAll(deviceModelList);
        }
    }

}
