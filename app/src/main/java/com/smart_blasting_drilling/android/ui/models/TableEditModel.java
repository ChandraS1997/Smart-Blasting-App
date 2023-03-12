package com.smart_blasting_drilling.android.ui.models;

public class TableEditModel {
    String checkBox;
    String titleVal;
    boolean isSelected = false, isFirst = true;

    public TableEditModel(String checkBox) {
        this.checkBox = checkBox;
    }

    public TableEditModel(String checkBox, String titleVal) {
        this.checkBox = checkBox;
        this.titleVal = titleVal;
    }

    public TableEditModel(String checkBox, boolean isSelected, boolean isFirstTime) {
        this.checkBox = checkBox;
        this.isSelected = isSelected;
        this.isFirst = isFirstTime;
    }

    public TableEditModel(String checkBox, String titleVal, boolean isSelected, boolean isFirst) {
        this.checkBox = checkBox;
        this.titleVal = titleVal;
        this.isSelected = isSelected;
        this.isFirst = isFirst;
    }

    public String getTitleVal() {
        return titleVal;
    }

    public void setTitleVal(String titleVal) {
        this.titleVal = titleVal;
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

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }
}
