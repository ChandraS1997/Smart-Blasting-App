package com.mineexcellence.sblastingapp.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class InitiatingDeviceDataEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "DesignId")
    private String designId;

    @ColumnInfo(name = "InitiatingDeviceData")
    private String data;

    public InitiatingDeviceDataEntity() {
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public InitiatingDeviceDataEntity(String data) {
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
