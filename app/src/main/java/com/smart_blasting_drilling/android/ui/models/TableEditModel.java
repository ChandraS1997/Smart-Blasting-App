package com.smart_blasting_drilling.android.ui.models;

import android.widget.CheckBox;

public class TableEditModel {
    String checkBox;
    boolean isSelected = true;

    public TableEditModel(String checkBox) {
        this.checkBox = checkBox;
    }

    public TableEditModel(String checkBox, boolean isSelected) {
        this.checkBox = checkBox;
        this.isSelected = isSelected;
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
}
