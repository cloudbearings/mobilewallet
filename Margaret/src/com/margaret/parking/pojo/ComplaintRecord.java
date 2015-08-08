package com.margaret.parking.pojo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class ComplaintRecord {
    private String referenceId;
    private JsonObject complaint;
    private JsonObject location;
    private JsonObject reportedBy;
    private JsonObject wronglyParkedDetails;

    public ComplaintRecord(String referenceId, JsonObject complaint, JsonObject location, JsonObject reportedBy, JsonObject wronglyParkedDetails){
        this.referenceId = referenceId;
        this.complaint = complaint;
        this.location = location;
        this.reportedBy = reportedBy;
        this.wronglyParkedDetails = wronglyParkedDetails;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public JsonObject getComplaint() {
        return complaint;
    }

    public void setComplaint(JsonObject complaint) {
        this.complaint = complaint;
    }

    public JsonObject getLocation() {
        return location;
    }

    public void setLocation(JsonObject location) {
        this.location = location;
    }

    public JsonObject getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(JsonObject reportedBy) {
        this.reportedBy = reportedBy;
    }

    public JsonObject getWronglyParkedDetails() {
        return wronglyParkedDetails;
    }

    public void setWronglyParkedDetails(JsonObject wronglyParkedDetails) {
        this.wronglyParkedDetails = wronglyParkedDetails;
    }
}
