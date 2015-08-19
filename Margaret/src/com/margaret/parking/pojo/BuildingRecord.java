package com.margaret.parking.pojo;

import com.google.gson.JsonObject;

/**
 * Created by gopi on 8/19/15.
 */
public class BuildingRecord {

    private int buildingId;
    private String buildingName;
    private String status;

    public BuildingRecord(int buildingId, String buildingName, String status){
        this.buildingId = buildingId;
        this.buildingName = buildingName;
        this.status = status;
    }

    public int getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(int buildingId) {
        this.buildingId = buildingId;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
