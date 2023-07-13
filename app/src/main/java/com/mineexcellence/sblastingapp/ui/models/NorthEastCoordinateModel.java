package com.mineexcellence.sblastingapp.ui.models;

public class NorthEastCoordinateModel {
    double minEasting, maxEasting, minNorthing, maxNorthing;

    public NorthEastCoordinateModel() {
    }

    public NorthEastCoordinateModel(double minEasting, double maxEasting, double minNorthing, double maxNorthing) {
        this.minEasting = minEasting;
        this.maxEasting = maxEasting;
        this.minNorthing = minNorthing;
        this.maxNorthing = maxNorthing;
    }

    public double getMinEasting() {
        return minEasting;
    }

    public void setMinEasting(double minEasting) {
        this.minEasting = minEasting;
    }

    public double getMaxEasting() {
        return maxEasting;
    }

    public void setMaxEasting(double maxEasting) {
        this.maxEasting = maxEasting;
    }

    public double getMinNorthing() {
        return minNorthing;
    }

    public void setMinNorthing(double minNorthing) {
        this.minNorthing = minNorthing;
    }

    public double getMaxNorthing() {
        return maxNorthing;
    }

    public void setMaxNorthing(double maxNorthing) {
        this.maxNorthing = maxNorthing;
    }
}
