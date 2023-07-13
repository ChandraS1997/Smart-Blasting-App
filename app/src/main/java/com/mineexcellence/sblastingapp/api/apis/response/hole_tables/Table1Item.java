package com.mineexcellence.sblastingapp.api.apis.response.hole_tables;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Table1Item implements Serializable {

    @SerializedName("YoungModulus")
    private String youngModulus;

    @SerializedName("DesignId")
    private int designId;

    @SerializedName("BenchHeight")
    private int benchHeight;

    @SerializedName("GroundSiteExponent")
    private Object groundSiteExponent;

    @SerializedName("DeckLength")
    private int deckLength;

    @SerializedName("FragOverSize")
    private int fragOverSize;

    @SerializedName("RockDensity")
    private String rockDensity;

    @SerializedName("JpoType")
    private String jpoType;

    @SerializedName("FragUnderSize")
    private Object fragUnderSize;

    @SerializedName("PatternTypeId")
    private int patternTypeId;

    @SerializedName("TotalRows")
    private int totalRows;

    @SerializedName("Subdrill")
    private int subdrill;

    @SerializedName("FragOptimumSize")
    private Object fragOptimumSize;

    @SerializedName("GroundSiteConstant")
    private Object groundSiteConstant;

    @SerializedName("RockName")
    private String rockName;

    @SerializedName("Distance")
    private int distance;

    @SerializedName("AirSiteExponent")
    private Object airSiteExponent;

    @SerializedName("Rock_USC")
    private String rockUSC;

    @SerializedName("PatternType")
    private String patternType;

    @SerializedName("AirSiteConstant")
    private Object airSiteConstant;

    @SerializedName("FaceAngle")
    private int faceAngle;

    @SerializedName("RmdType")
    private int rmdType;

    @SerializedName("HoleAngle")
    private int holeAngle;

    @SerializedName("WaterDpth")
    private int waterDpth;

    @SerializedName("RockCode")
    private int rockCode;

    @SerializedName("NoOfDecks")
    private int noOfDecks;

    @SerializedName("HoleDia")
    private int holeDia;

    @SerializedName("ChargePerDelay")
    private int chargePerDelay;

    @SerializedName("BenchWidth")
    private int benchWidth;

    @SerializedName("FlyrockConstant")
    private int flyrockConstant;

    @SerializedName("VerticaljointSpacing")
    private String verticaljointSpacing;

    @SerializedName("CompanyID")
    private String companyID;

    @SerializedName("Isvisible")
    private int isvisible;

    @SerializedName("UserID")
    private Object userID;

    @SerializedName("ZoneCode")
    private int zoneCode;

    @SerializedName("BenchCode")
    private int benchCode;

    @SerializedName("Name")
    private String name;

    public void setYoungModulus(String youngModulus) {
        this.youngModulus = youngModulus;
    }

    public String getYoungModulus() {
        return youngModulus;
    }

    public void setDesignId(int designId) {
        this.designId = designId;
    }

    public int getDesignId() {
        return designId;
    }

    public void setBenchHeight(int benchHeight) {
        this.benchHeight = benchHeight;
    }

    public int getBenchHeight() {
        return benchHeight;
    }

    public void setGroundSiteExponent(Object groundSiteExponent) {
        this.groundSiteExponent = groundSiteExponent;
    }

    public Object getGroundSiteExponent() {
        return groundSiteExponent;
    }

    public void setDeckLength(int deckLength) {
        this.deckLength = deckLength;
    }

    public int getDeckLength() {
        return deckLength;
    }

    public void setFragOverSize(int fragOverSize) {
        this.fragOverSize = fragOverSize;
    }

    public int getFragOverSize() {
        return fragOverSize;
    }

    public void setRockDensity(String rockDensity) {
        this.rockDensity = rockDensity;
    }

    public String getRockDensity() {
        return rockDensity;
    }

    public void setJpoType(String jpoType) {
        this.jpoType = jpoType;
    }

    public String getJpoType() {
        return jpoType;
    }

    public void setFragUnderSize(Object fragUnderSize) {
        this.fragUnderSize = fragUnderSize;
    }

    public Object getFragUnderSize() {
        return fragUnderSize;
    }

    public void setPatternTypeId(int patternTypeId) {
        this.patternTypeId = patternTypeId;
    }

    public int getPatternTypeId() {
        return patternTypeId;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setSubdrill(int subdrill) {
        this.subdrill = subdrill;
    }

    public int getSubdrill() {
        return subdrill;
    }

    public void setFragOptimumSize(Object fragOptimumSize) {
        this.fragOptimumSize = fragOptimumSize;
    }

    public Object getFragOptimumSize() {
        return fragOptimumSize;
    }

    public void setGroundSiteConstant(Object groundSiteConstant) {
        this.groundSiteConstant = groundSiteConstant;
    }

    public Object getGroundSiteConstant() {
        return groundSiteConstant;
    }

    public void setRockName(String rockName) {
        this.rockName = rockName;
    }

    public String getRockName() {
        return rockName;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setAirSiteExponent(Object airSiteExponent) {
        this.airSiteExponent = airSiteExponent;
    }

    public Object getAirSiteExponent() {
        return airSiteExponent;
    }

    public void setRockUSC(String rockUSC) {
        this.rockUSC = rockUSC;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setIsvisible(int isvisible) {
        this.isvisible = isvisible;
    }

    public int getIsvisible() {
        return isvisible;
    }

    public void setUserID(Object userID) {
        this.userID = userID;
    }

    public Object getUserID() {
        return userID;
    }

    public void setZoneCode(int zoneCode) {
        this.zoneCode = zoneCode;
    }

    public int getZoneCode() {
        return zoneCode;
    }

    public void setBenchCode(int benchCode) {
        this.benchCode = benchCode;
    }

    public int getBenchCode() {
        return benchCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getRockUSC() {
        return rockUSC;
    }

    public String getPatternType() {
        return patternType;
    }

    public void setPatternType(String patternType) {
        this.patternType = patternType;
    }

    public Object getAirSiteConstant() {
        return airSiteConstant;
    }

    public void setAirSiteConstant(Object airSiteConstant) {
        this.airSiteConstant = airSiteConstant;
    }

    public int getFaceAngle() {
        return faceAngle;
    }

    public void setFaceAngle(int faceAngle) {
        this.faceAngle = faceAngle;
    }

    public int getRmdType() {
        return rmdType;
    }

    public void setRmdType(int rmdType) {
        this.rmdType = rmdType;
    }

    public int getHoleAngle() {
        return holeAngle;
    }

    public void setHoleAngle(int holeAngle) {
        this.holeAngle = holeAngle;
    }

    public int getWaterDpth() {
        return waterDpth;
    }

    public void setWaterDpth(int waterDpth) {
        this.waterDpth = waterDpth;
    }

    public int getRockCode() {
        return rockCode;
    }

    public void setRockCode(int rockCode) {
        this.rockCode = rockCode;
    }

    public int getNoOfDecks() {
        return noOfDecks;
    }

    public void setNoOfDecks(int noOfDecks) {
        this.noOfDecks = noOfDecks;
    }

    public int getHoleDia() {
        return holeDia;
    }

    public void setHoleDia(int holeDia) {
        this.holeDia = holeDia;
    }

    public int getChargePerDelay() {
        return chargePerDelay;
    }

    public void setChargePerDelay(int chargePerDelay) {
        this.chargePerDelay = chargePerDelay;
    }

    public int getBenchWidth() {
        return benchWidth;
    }

    public void setBenchWidth(int benchWidth) {
        this.benchWidth = benchWidth;
    }

    public int getFlyrockConstant() {
        return flyrockConstant;
    }

    public void setFlyrockConstant(int flyrockConstant) {
        this.flyrockConstant = flyrockConstant;
    }

    public String getVerticaljointSpacing() {
        return verticaljointSpacing;
    }

    public void setVerticaljointSpacing(String verticaljointSpacing) {
        this.verticaljointSpacing = verticaljointSpacing;
    }
}