package com.smart_blasting_drilling.android.ui.models;

import java.io.Serializable;

public class ChargingDataModel implements Serializable {

    String type, explosive, cost, length, weight;

    public ChargingDataModel() {
    }

    public ChargingDataModel(String type, String explosive, String cost, String length, String weight) {
        this.type = type;
        this.explosive = explosive;
        this.cost = cost;
        this.length = length;
        this.weight = weight;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExplosive() {
        return explosive;
    }

    public void setExplosive(String explosive) {
        this.explosive = explosive;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
