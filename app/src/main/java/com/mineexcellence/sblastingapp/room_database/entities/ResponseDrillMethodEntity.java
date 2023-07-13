package com.mineexcellence.sblastingapp.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ResponseDrillMethodEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "DrillMethod")
    private String data;

    public ResponseDrillMethodEntity() {
    }

    public ResponseDrillMethodEntity(long id, String data) {
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
