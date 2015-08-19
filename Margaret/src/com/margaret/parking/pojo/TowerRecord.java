package com.margaret.parking.pojo;

import com.google.gson.JsonObject;

/**
 * Created by gopi on 8/19/15.
 */
public class TowerRecord {
    private int towerId;
    private int levelId;
    private String towerName;
    private String towerlAliasName;
    private int places;
    private int extraPlaces;
    private String status;

    public TowerRecord(int towerId, int levelId, String towerName, String towerlAliasName, int places, int extraPlaces, String status) {
        this.towerId = towerId;
        this.levelId = levelId;
        this.towerName = towerName;
        this.towerlAliasName = towerlAliasName;
        this.places = places;
        this.extraPlaces = extraPlaces;
        this.status = status;
    }

    public int getTowerId() {
        return towerId;
    }

    public void setTowerId(int towerId) {
        this.towerId = towerId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getTowerName() {
        return towerName;
    }

    public void setTowerName(String towerName) {
        this.towerName = towerName;
    }

    public String getTowerlAliasName() {
        return towerlAliasName;
    }

    public void setTowerlAliasName(String towerlAliasName) {
        this.towerlAliasName = towerlAliasName;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public int getExtraPlaces() {
        return extraPlaces;
    }

    public void setExtraPlaces(int extraPlaces) {
        this.extraPlaces = extraPlaces;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
