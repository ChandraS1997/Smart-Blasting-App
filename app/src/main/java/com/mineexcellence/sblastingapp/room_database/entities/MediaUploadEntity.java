package com.mineexcellence.sblastingapp.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MediaUploadEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "ProjectId")
    private String projectId;

    @ColumnInfo(name = "data")
    private String data;

    public MediaUploadEntity(String projectId, String data) {
        this.projectId = projectId;
        this.data = data;
    }

    public MediaUploadEntity() {
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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
