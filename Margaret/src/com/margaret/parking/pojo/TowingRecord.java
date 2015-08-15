package com.margaret.parking.pojo;

import android.graphics.Bitmap;

/**
 * Created by varmu02 on 7/5/2015.
 */
public class TowingRecord {

    String complaintID;
    String referenceId;
    String plateDetails;
    Bitmap beforeTowingPhoto;
    Bitmap afterTowingPhoto;
    String clampDate;


    public TowingRecord(){

    }

    public TowingRecord(String referenceId, Bitmap beforeTowingPhoto, String plateDetails, Bitmap afterTowingPhoto, String clampDate) {
        this.referenceId = referenceId;
        this.beforeTowingPhoto = beforeTowingPhoto;
        this.plateDetails = plateDetails;
        this.afterTowingPhoto = afterTowingPhoto;
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

    public Bitmap getBeforeTowingPhoto() {
        return beforeTowingPhoto;
    }

    public void setBeforeTowingPhoto(Bitmap beforeTowingPhoto) {
        this.beforeTowingPhoto = beforeTowingPhoto;
    }

    public String getClampDate() {
        return clampDate;
    }

    public void setClampDate(String clampDate) {
        this.clampDate = clampDate;
    }

    public Bitmap getAfterTowingPhoto() {
        return afterTowingPhoto;
    }

    public void setAfterTowingPhoto(Bitmap afterTowingPhoto) {
        this.afterTowingPhoto = afterTowingPhoto;
    }
}
