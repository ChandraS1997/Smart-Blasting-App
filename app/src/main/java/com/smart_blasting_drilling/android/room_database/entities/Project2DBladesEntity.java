package com.smart_blasting_drilling.android.room_database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity
public class Project2DBladesEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @SerializedName("PitName")
    @ColumnInfo(name = "PitName")
    private String pitName;

    @SerializedName("ZoneName")
    @ColumnInfo(name = "ZoneName")
    private String zoneName;

    @SerializedName("DesignId")
    @ColumnInfo(name = "DesignId")
    private String designId;

    @SerializedName("DesignCode")
    @ColumnInfo(name = "DesignCode")
    private String designCode;

    @SerializedName("DesignName")
    @ColumnInfo(name = "DesignName")
    private String designName;

    @SerializedName("DesignDateTime")
    @ColumnInfo(name = "DesignDateTime")
    private String designDateTime;

    @SerializedName("MineName")
    @ColumnInfo(name = "MineName")
    private String mineName;

    @SerializedName("BenchName")
    @ColumnInfo(name = "BenchName")
    private String benchName;

    @SerializedName("RockName")
    @ColumnInfo(name = "RockName")
    private String rockName;

    @SerializedName("PcntUnderSize")
    @ColumnInfo(name = "PcntUnderSize")
    private String pcntUnderSize;

    @SerializedName("BlastPlan")
    @ColumnInfo(name = "BlastPlan")
    private String blastPlan;

    @SerializedName("AirVibration")
    @ColumnInfo(name = "AirVibration")
    private String airVibration;

    public String getB_i() {
        return b_i;
    }

    public void setB_i(String b_i) {
        this.b_i = b_i;
    }

    @SerializedName("BI")
    @ColumnInfo(name = "BI")
    private String b_i;

    @SerializedName("PcntOverSize")
    @ColumnInfo(name = "PcntOverSize")
    private String pcntOverSize;

    @SerializedName("UniformExp")
    @ColumnInfo(name = "UniformExp")
    private String uniformExp;

    @SerializedName("MatSize")
    @ColumnInfo(name = "MatSize")
    private String matSize;

    @SerializedName("GroundVibration")
    @ColumnInfo(name = "GroundVibration")
    private String groundVibration;

    @SerializedName("BoosterCharge")
    @ColumnInfo(name = "BoosterCharge")
    private String boosterCharge;

    @SerializedName("PcntOptSize")
    @ColumnInfo(name = "PcntOptSize")
    private String pcntOptSize;

    @SerializedName("BackThrow")
    @ColumnInfo(name = "BackThrow")
    private String backThrow;

    @SerializedName("BottomCharge")
    @ColumnInfo(name = "BottomCharge")
    private String bottomCharge;

    @SerializedName("ColCharge")
    @ColumnInfo(name = "ColCharge")
    private String colCharge;

    @SerializedName("FrontThrow")
    @ColumnInfo(name = "FrontThrow")
    private String frontThrow;

    @SerializedName("ChrctSize")
    @ColumnInfo(name = "ChrctSize")
    private String chrctSize;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPitName() {
        return pitName;
    }

    public void setPitName(String pitName) {
        this.pitName = pitName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getDesignCode() {
        return designCode;
    }

    public void setDesignCode(String designCode) {
        this.designCode = designCode;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getDesignDateTime() {
        return designDateTime;
    }

    public void setDesignDateTime(String designDateTime) {
        this.designDateTime = designDateTime;
    }

    public String getMineName() {
        return mineName;
    }

    public void setMineName(String mineName) {
        this.mineName = mineName;
    }

    public String getBenchName() {
        return benchName;
    }

    public void setBenchName(String benchName) {
        this.benchName = benchName;
    }

    public String getRockName() {
        return rockName;
    }

    public void setRockName(String rockName) {
        this.rockName = rockName;
    }

    public String getPcntUnderSize() {
        return pcntUnderSize;
    }

    public void setPcntUnderSize(String pcntUnderSize) {
        this.pcntUnderSize = pcntUnderSize;
    }

    public String getBlastPlan() {
        return blastPlan;
    }

    public void setBlastPlan(String blastPlan) {
        this.blastPlan = blastPlan;
    }

    public String getAirVibration() {
        return airVibration;
    }

    public void setAirVibration(String airVibration) {
        this.airVibration = airVibration;
    }

    public String getPcntOverSize() {
        return pcntOverSize;
    }

    public void setPcntOverSize(String pcntOverSize) {
        this.pcntOverSize = pcntOverSize;
    }

    public String getUniformExp() {
        return uniformExp;
    }

    public void setUniformExp(String uniformExp) {
        this.uniformExp = uniformExp;
    }

    public String getMatSize() {
        return matSize;
    }

    public void setMatSize(String matSize) {
        this.matSize = matSize;
    }

    public String getGroundVibration() {
        return groundVibration;
    }

    public void setGroundVibration(String groundVibration) {
        this.groundVibration = groundVibration;
    }

    public String getBoosterCharge() {
        return boosterCharge;
    }

    public void setBoosterCharge(String boosterCharge) {
        this.boosterCharge = boosterCharge;
    }

    public String getPcntOptSize() {
        return pcntOptSize;
    }

    public void setPcntOptSize(String pcntOptSize) {
        this.pcntOptSize = pcntOptSize;
    }

    public String getBackThrow() {
        return backThrow;
    }

    public void setBackThrow(String backThrow) {
        this.backThrow = backThrow;
    }

    public String getBottomCharge() {
        return bottomCharge;
    }

    public void setBottomCharge(String bottomCharge) {
        this.bottomCharge = bottomCharge;
    }

    public String getColCharge() {
        return colCharge;
    }

    public void setColCharge(String colCharge) {
        this.colCharge = colCharge;
    }

    public String getFrontThrow() {
        return frontThrow;
    }

    public void setFrontThrow(String frontThrow) {
        this.frontThrow = frontThrow;
    }

    public String getChrctSize() {
        return chrctSize;
    }

    public void setChrctSize(String chrctSize) {
        this.chrctSize = chrctSize;
    }
}
