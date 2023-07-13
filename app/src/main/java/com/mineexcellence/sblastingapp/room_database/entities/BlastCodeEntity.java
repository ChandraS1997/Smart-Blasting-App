package com.mineexcellence.sblastingapp.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BlastCodeEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "BlastCode")
    public String blastCode;

    @ColumnInfo(name = "DesignId")
    public String designId;

    public BlastCodeEntity() {
    }

    public BlastCodeEntity(String blastCode, String designId) {
        this.blastCode = blastCode;
        this.designId = designId;
    }

    public BlastCodeEntity(String blastCode) {
        this.blastCode = blastCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBlastCode() {
        return blastCode;
    }

    public void setBlastCode(String blastCode) {
        this.blastCode = blastCode;
    }
}
