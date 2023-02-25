package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ProjectHoleDetailRowColEntity")
public class ProjectHoleDetailRowColEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "designId")
    public String designId;

    @ColumnInfo(name = "project_hole")
    public String projectHole;

    public ProjectHoleDetailRowColEntity(String designId, String projectHole) {
        this.designId = designId;
        this.projectHole = projectHole;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getProjectHole() {
        return projectHole;
    }

    public void setProjectHole(String projectHole) {
        this.projectHole = projectHole;
    }
}
