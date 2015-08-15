package com.margaret.parking.pojo;

import android.graphics.Bitmap;

/**
 * Created by varmu02 on 7/5/2015.
 */
public class ClampRecord {

    String complaintID;
    String referenceId;
    String plateDetails;
    Bitmap beforeClampPhoto;
    Bitmap afterClampPhoto;
    String clampDate;

    public ClampRecord() {

    }

    public ClampRecord(String referenceId, Bitmap beforeClampPhoto, String plateDetails, Bitmap afterClampPhoto, String clampDate) {
        this.referenceId = referenceId;
        this.beforeClampPhoto = beforeClampPhoto;
        this.plateDetails = plateDetails;
        this.afterClampPhoto = afterClampPhoto;
        this.clampDate = clampDate;
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

    public String getPlateDetails() {
        return plateDetails;
    }

    public void setPlateDetails(String plateDetails) {
        this.plateDetails = plateDetails;
    }

    public Bitmap getBeforeClampPhoto() {
        return beforeClampPhoto;
    }

    public void setBeforeClampPhoto(Bitmap beforeClampPhoto) {
        this.beforeClampPhoto = beforeClampPhoto;
    }

    public String getClampDate() {
        return clampDate;
    }

    public void setClampDate(String clampDate) {
        this.clampDate = clampDate;
    }

    public Bitmap getAfterClampPhoto() {
        return afterClampPhoto;
    }

    public void setAfterClampPhoto(Bitmap afterClampPhoto) {
        this.afterClampPhoto = afterClampPhoto;
    }
}
