package com.smart_blasting_drilling.android.ui.models;

public class InitiatingDeviceModel {

    int pageCount;
    String type;
    String cost;
    String qty;

    public InitiatingDeviceModel() {
    }

    public InitiatingDeviceModel(int pageCount) {
        this.pageCount = pageCount;
    }

    public InitiatingDeviceModel(int pageCount, String type, String cost, String qty) {
        this.pageCount = pageCount;
        this.type = type;
        this.cost = cost;
        this.qty = qty;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
