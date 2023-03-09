package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BlastCodeEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "BlastCode")
    public String blastCode;

    public BlastCodeEntity() {
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
