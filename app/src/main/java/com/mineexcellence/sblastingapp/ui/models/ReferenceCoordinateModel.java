package com.mineexcellence.sblastingapp.ui.models;

public class ReferenceCoordinateModel {

    int rowNo, holeNo;
    double x, y;

    public ReferenceCoordinateModel() {
    }

    public ReferenceCoordinateModel(int rowNo, int holeNo, double x, double y) {
        this.rowNo = rowNo;
        this.holeNo = holeNo;
        this.x = x;
        this.y = y;
    }

    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public int getHoleNo() {
        return holeNo;
    }

    public void setHoleNo(int holeNo) {
        this.holeNo = holeNo;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
