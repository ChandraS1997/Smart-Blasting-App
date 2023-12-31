package com.mineexcellence.sblastingapp.interfaces;

import com.mineexcellence.sblastingapp.ui.models.TableEditModel;

import java.util.List;

public interface OnDataEditTable {
    public void editDataTable(List<TableEditModel> arrayList, boolean fromPref);
    default void pilotEditDataTable(List<TableEditModel> arrayList, boolean fromPref){}
    default void preSplitEditDataTable(List<TableEditModel> arrayList, boolean fromPref){}
}
