package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResponseDrillMaterialEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "DrillMaterial")
    private String data;

    public ResponseDrillMaterialEntity() {
    }

    public ResponseDrillMaterialEntity(long id, String data) {
        this.id = id;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
