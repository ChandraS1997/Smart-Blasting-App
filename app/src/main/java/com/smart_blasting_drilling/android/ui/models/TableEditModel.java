package com.smart_blasting_drilling.android.ui.models;

public class TableEditModel {
    String checkBox;
    boolean isSelected = false, isFirst = true;

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
        this.isFirst = isFirstTime;
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
