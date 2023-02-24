package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class AllProjectBladesModelEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "Is2dBlade")
    public boolean is2dBlade;

    @ColumnInfo(name = "ProjectCode")
    public String projectCode;

    @ColumnInfo(name = "Blades")
    private String data;

    @ColumnInfo(name = "DesignId")
    private String designId;

    public AllProjectBladesModelEntity() {
    }

    public AllProjectBladesModelEntity(boolean is2dBlade, String data) {
        this.is2dBlade = is2dBlade;
        this.data = data;
    }

    public AllProjectBladesModelEntity(boolean is2dBlade, String projectCode, String data) {
        this.is2dBlade = is2dBlade;
        this.projectCode = projectCode;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isIs2dBlade() {
        return is2dBlade;
    }

    public void setIs2dBlade(boolean is2dBlade) {
        this.is2dBlade = is2dBlade;
    }

    public String getProjectCode() {
        return projectCode == null ? "0" : projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
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
