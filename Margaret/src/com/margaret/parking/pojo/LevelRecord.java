package com.margaret.parking.pojo;

import com.google.gson.JsonObject;

/**
 * Created by gopi on 8/19/15.
 */
public class LevelRecord {
    private int levelId;
    private int buildingId;
    private String levelName;
    private String levelAliasName;
    private String levelDesc;
    private String imageUrl;
    private String status;

    public LevelRecord(int levelId, int buildingId, String levelName, String levelAliasName, String levelDesc, String imageUrl, String status) {
        this.levelId = levelId;
        this.buildingId = buildingId;
        this.levelName = levelName;
        this.levelAliasName = levelAliasName;
        this.levelDesc = levelDesc;
        this.imageUrl = imageUrl;
        this.status = status;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getLevelAliasName() {
        return levelAliasName;
    }

    public void setLevelAliasName(String levelAliasName) {
        this.levelAliasName = levelAliasName;
    }

    public String getLevelDesc() {
        return levelDesc;
    }

    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
