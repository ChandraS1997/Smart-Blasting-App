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
}
