package com.margaret.parking.pojo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class ComplaintRecord {

    private String complaintID;
    private String referenceId;
    private JsonObject complaints;
    private JsonObject location;
    private JsonObject reportedby;
    private JsonObject wrongparkeddetails;

    public ComplaintRecord(String complaintID, String referenceId, JsonObject complaints, JsonObject location, JsonObject reportedby, JsonObject wrongparkeddetails) {
        this.complaintID = complaintID;
        this.referenceId = referenceId;
        this.complaints = complaints;
        this.location = location;
        this.reportedby = reportedby;
        this.wrongparkeddetails = wrongparkeddetails;
    }

    public String getComplaintID() {
        return complaintID;
    }

    public void setComplaintID(String complaintID) {
        this.complaintID = complaintID;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public JsonObject getLocation() {
        return location;
    }

    public void setLocation(JsonObject location) {
        this.location = location;
    }

    public JsonObject getComplaints() {
        return complaints;
    }

    public void setComplaints(JsonObject complaints) {
        this.complaints = complaints;
    }

    public JsonObject getReportedby() {
        return reportedby;
    }

    public void setReportedby(JsonObject reportedby) {
        this.reportedby = reportedby;
    }

    public JsonObject getWrongparkeddetails() {
        return wrongparkeddetails;
    }

    public void setWrongparkeddetails(JsonObject wrongparkeddetails) {
        this.wrongparkeddetails = wrongparkeddetails;
    }
}
