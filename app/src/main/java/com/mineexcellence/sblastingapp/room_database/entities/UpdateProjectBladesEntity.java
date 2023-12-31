package com.mineexcellence.sblastingapp.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class UpdateProjectBladesEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "RowId")
    public int rowId;

    @ColumnInfo(name = "HoleId")
    public int holeId;

    @ColumnInfo(name = "BladesData")
    private String data;

    @ColumnInfo(name = "designId")
    private String designId;

    public UpdateProjectBladesEntity(String data) {
        this.data = data;
    }

    public UpdateProjectBladesEntity(String data, String designId) {
        this.data = data;
        this.designId = designId;
    }

    public UpdateProjectBladesEntity() {
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public int getHoleId() {
        return holeId;
    }

    public void setHoleId(int holeId) {
        this.holeId = holeId;
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

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }
}
