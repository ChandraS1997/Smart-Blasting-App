package com.smart_blasting_drilling.android.ui.models;

public class TableEditModel {
    String checkBox;
    boolean isSelected = false;

    public TableEditModel(String checkBox) {
        this.checkBox = checkBox;
    }

    public TableEditModel(String checkBox, boolean isSelected) {
        this.checkBox = checkBox;
        this.isSelected = isSelected;
    }

    public TableEditModel(String checkBox, boolean isSelected, boolean isFirstTime) {
        this.checkBox = checkBox;
        this.isSelected = isSelected;
//        this.isFirstTime = isFirstTime;
    }

    public String getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(String checkBox) {
        this.checkBox = checkBox;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /*public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }*/
}
