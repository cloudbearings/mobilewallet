package com.margaret.parking.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonObject;
import com.margaret.parking.db.DBContract.ComplaintColumns;
import com.margaret.parking.pojo.ClampRecord;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.pojo.TowingRecord;
import com.margaret.parking.util.Utils;

/**
 * Created by varmu02 on 6/26/2015.
 */
public class DBOpenHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "Margaret.db";
    private static final int DATABASE_VERSION = 1;
    private static DBOpenHelper dbInstance;

    //USE THE FOLLOWING TABLE FOR COMPLAINT REGISTRATION
    private static final String COMPLAINT_TABLE_CREATE = "CREATE TABLE " + DBContract.ComplaintColumns.TABLE_NAME + "("
            + DBContract.ComplaintColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.REFERENCE + " TEXT NOT NULL, "
            + DBContract.ComplaintColumns.BUILDING_NAME + " TEXT NOT NULL, "
            + DBContract.ComplaintColumns.CLUSTER + " TEXT, "
            + DBContract.ComplaintColumns.LEVEL + " TEXT, "
            + DBContract.ComplaintColumns.ZONE + " TEXT, "
            + DBContract.ComplaintColumns.BAY_NUMBER + " TEXT, "

            + DBContract.ComplaintColumns.MAKE + " TEXT, "
            + DBContract.ComplaintColumns.COLOR + " TEXT, "
            + DBContract.ComplaintColumns.MODEL + " TEXT, "
            + DBContract.ComplaintColumns.PLATE_DETAILS + " TEXT, "

            + DBContract.ComplaintColumns.COMPLAINER_NAME + " TEXT, "
            + DBContract.ComplaintColumns.COMPLAINER_EMAIL + " TEXT, "
            + DBContract.ComplaintColumns.COMPLAINER_NUMBER + " TEXT, "

            + DBContract.ComplaintColumns.DATE + " TEXT, "
            + DBContract.ComplaintColumns.C_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.T_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.P_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.REJECTED + " TEXT DEFAULT 'false', "
            + DBContract.ComplaintColumns.LIVING_STATUS + " TEXT DEFAULT 'true', "
            + DBContract.ComplaintColumns.V_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.V_DATE + " TEXT, "
            + DBContract.ComplaintColumns.TEMP_LOCATION + " TEXT, "
            + DBContract.ComplaintColumns.COMMENTS + " TEXT, "
            + ComplaintColumns.AMOUNT_PAID + " INTEGER DEFAULT 0"
            + ")";
    //ONLY FOR CUSTOMERS - DONT USE FOR OTHER PURPOSE
    private static final String CUST_COMPLAINT_TABLE_CREATE = "CREATE TABLE " + DBContract.ComplaintColumns.CUST_TABLE_NAME + "("
            + DBContract.ComplaintColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.REFERENCE + " TEXT NOT NULL, "
            + DBContract.ComplaintColumns.BUILDING_NAME + " TEXT NOT NULL, "
            + DBContract.ComplaintColumns.CLUSTER + " TEXT, "
            + DBContract.ComplaintColumns.LEVEL + " TEXT, "
            + DBContract.ComplaintColumns.ZONE + " TEXT, "
            + DBContract.ComplaintColumns.BAY_NUMBER + " TEXT, "

            + DBContract.ComplaintColumns.MAKE + " TEXT, "
            + DBContract.ComplaintColumns.COLOR + " TEXT, "
            + DBContract.ComplaintColumns.MODEL + " TEXT, "
            + DBContract.ComplaintColumns.PLATE_DETAILS + " TEXT, "

            + DBContract.ComplaintColumns.COMPLAINER_NAME + " TEXT, "
            + DBContract.ComplaintColumns.COMPLAINER_EMAIL + " TEXT, "
            + DBContract.ComplaintColumns.COMPLAINER_NUMBER + " TEXT, "

            + DBContract.ComplaintColumns.DATE + " TEXT, "
            + DBContract.ComplaintColumns.C_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.T_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.P_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.REJECTED + " TEXT DEFAULT 'false', "
            + DBContract.ComplaintColumns.LIVING_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.V_STATUS + " TEXT, "
            + DBContract.ComplaintColumns.V_DATE + " TEXT, "
            + DBContract.ComplaintColumns.TEMP_LOCATION + " TEXT, "
            + DBContract.ComplaintColumns.COMMENTS + " TEXT, "
            + ComplaintColumns.AMOUNT_PAID + " INTEGER DEFAULT 0"
            + ")";


    private static final String CLAMPING_TABLE_CREATE = "CREATE TABLE "+ DBContract.ClampDataColumns.TABLE_NAME + "("
            + DBContract.ClampDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ClampDataColumns.REFERENCE + " TEXT, "
            + DBContract.ClampDataColumns.PLATE_DETAILS + " TEXT, "
            + DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO + " BLOB, "
            + DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO + " BLOB, "
            + DBContract.ClampDataColumns.CLAMP_DATE + " TEXT"
            +")";
    private static final String TOWING_TABLE_CREATE = "CREATE TABLE "+ DBContract.TowingDataColumns.TABLE_NAME + "("
            + DBContract.TowingDataColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.TowingDataColumns.REFERENCE + " TEXT, "
            + DBContract.TowingDataColumns.PLATE_DETAILS + " TEXT, "
            + DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO + " BLOB, "
            + DBContract.TowingDataColumns.TOWING_AFTER_PHOTO + " BLOB, "
            + DBContract.TowingDataColumns.TOWING_DATE + " TEXT"
            +")";

        private DBOpenHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         * Singleton method
         * @param context
         * @return
         */
        public static DBOpenHelper getInstance(Context context){
                if(dbInstance == null){
                        dbInstance = new DBOpenHelper(context);
                }
                return dbInstance;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(COMPLAINT_TABLE_CREATE);
                db.execSQL(CLAMPING_TABLE_CREATE);
                db.execSQL(TOWING_TABLE_CREATE);
                db.execSQL(CUST_COMPLAINT_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    /**
     * Insert new complaint data.
     * @return Newly created row id.
     */
        public long insertNewComplaint(Context context, ComplaintRecord record){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.REFERENCE, record.getReferenceId()); //// TODO: 7/4/2015
            contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME , record.getLocation().get("name").getAsString());
            contentValues.put(DBContract.ComplaintColumns.CLUSTER , record.getLocation().get("cluster").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LEVEL , record.getLocation().get("level").getAsString());
            contentValues.put(DBContract.ComplaintColumns.ZONE , record.getLocation().get("zone").getAsString());
            contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER , record.getLocation().get("bay").getAsString());

            contentValues.put(DBContract.ComplaintColumns.MAKE , record.getWronglyParkedDetails().get("make").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COLOR , record.getWronglyParkedDetails().get("color").getAsString());
            contentValues.put(DBContract.ComplaintColumns.MODEL , record.getWronglyParkedDetails().get("model").getAsString());
            contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS , record.getWronglyParkedDetails().get("number").getAsString());

            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NAME , record.getReportedBy().get("name").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_EMAIL, record.getReportedBy().get("mail").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NUMBER , record.getReportedBy().get("contact").getAsString());

            contentValues.put(DBContract.ComplaintColumns.DATE, record.getComplaint().get("date").getAsString());
            contentValues.put(DBContract.ComplaintColumns.C_STATUS , record.getComplaint().get("c_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.T_STATUS , record.getComplaint().get("t_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.P_STATUS, record.getComplaint().get("p_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS , "true");
            contentValues.put(DBContract.ComplaintColumns.V_STATUS , record.getComplaint().get("v_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.V_DATE , record.getComplaint().get("v_date").getAsString());
            contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION , record.getComplaint().get("temp_location").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMMENTS , record.getComplaint().get("comments").getAsString());

            return getWritableDatabase().insert(DBContract.ComplaintColumns.TABLE_NAME, null, contentValues);

        }

    /**
     * Updates complaint clamping status
     * @param c_status
     * @param c_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
        public long updateComplaintClampDateAndStatus(String c_status,String c_date, String referenceNumber, String vehicleNumber ){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.C_STATUS, c_status);
            return getWritableDatabase().update(DBContract.ComplaintColumns.TABLE_NAME, contentValues, DBContract.ComplaintColumns.REFERENCE+ "='"+ referenceNumber +"'" , null);
        }

    /**
     * Updates complaint towing status
     * @param t_status
     * @param t_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
        public long updateComplaintTowingDateAndStatus(String t_status,String t_date, String referenceNumber, String vehicleNumber ){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.T_STATUS, t_status);
            return getWritableDatabase().update(DBContract.ComplaintColumns.TABLE_NAME, contentValues, DBContract.ComplaintColumns.REFERENCE+ "='"+ referenceNumber +"'" , null);
        }

    /**
     * Updates complaint payment status
     * @param p_status
     * @param p_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
        public long updateCompaintPaymentStatus(String p_status,String p_date, String referenceNumber, String vehicleNumber){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.P_STATUS, p_status);
            return getWritableDatabase().update(DBContract.ComplaintColumns.TABLE_NAME, contentValues, DBContract.ComplaintColumns.REFERENCE+ "='"+ referenceNumber +"'" , null);
        }

        public List<ComplaintRecord> fetchComplaints(Context context){
            List<ComplaintRecord> record = new ArrayList<>();
            Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='true'", null, null, null, null, null);
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    String referenceNumber = cursor.getString(1);
                    String buildingName = cursor.getString(2);
                    String cluster = cursor.getString(3);
                    String level = cursor.getString(4);
                    String zone = cursor.getString(5);
                    String bayNumber = cursor.getString(6);

                    String make = cursor.getString(7);
                    String color = cursor.getString(8);
                    String model = cursor.getString(9);
                    String vehicleNumber = cursor.getString(10);

                    String complainerName = cursor.getString(11);
                    String complainerEmail = cursor.getString(12);
                    String complainerContactNumber = cursor.getString(13);

                    String date = cursor.getString(14);
                    String c_status = cursor.getString(15);
                    String t_status = cursor.getString(16);
                    String p_status = cursor.getString(17);
                    String rejrected = cursor.getString(18);
                    String l_status = cursor.getString(19);
                    String v_status = cursor.getString(20);

                    JsonObject complaint = new JsonObject();

                    complaint.addProperty("date", date);
                    complaint.addProperty("v_status", v_status);
                    complaint.addProperty("c_status",c_status);
                    complaint.addProperty("t_status",t_status);
                    complaint.addProperty("p_status", p_status);
                    complaint.addProperty("l_status", l_status);
                    

                    JsonObject location = new JsonObject();
                    location.addProperty("name",buildingName);
                    location.addProperty("cluster",cluster);
                    location.addProperty("bay",bayNumber);
                    location.addProperty("zone",zone);
                    location.addProperty("level",level);

                    JsonObject reportedBy = new JsonObject();
                    reportedBy.addProperty("name",complainerName);
                    reportedBy.addProperty("mail",complainerEmail);
                    reportedBy.addProperty("contact",complainerContactNumber);

                    JsonObject vehicle = new JsonObject();
                    vehicle.addProperty("make",make);
                    vehicle.addProperty("color",color);
                    vehicle.addProperty("model",model);
                    vehicle.addProperty("number",vehicleNumber);

                    ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                    record.add(complaintRec);

                } while (cursor.moveToNext());
            }
            cursor.close();

            return record;
        }

    /**
     * Updates complaint job verification status. Persists temp location, comments
     * @param context
     * @param record
     * @return
     */
        public long updateJobVerificationStatus(Context context, ComplaintRecord record){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME , record.getLocation().get("name").getAsString());
            contentValues.put(DBContract.ComplaintColumns.CLUSTER , record.getLocation().get("cluster").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LEVEL , record.getLocation().get("level").getAsString());
            contentValues.put(DBContract.ComplaintColumns.ZONE , record.getLocation().get("zone").getAsString());
            contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER , record.getLocation().get("bay").getAsString());

            contentValues.put(DBContract.ComplaintColumns.MAKE , record.getWronglyParkedDetails().get("make").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COLOR , record.getWronglyParkedDetails().get("color").getAsString());
            contentValues.put(DBContract.ComplaintColumns.MODEL , record.getWronglyParkedDetails().get("model").getAsString());
            contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS , record.getWronglyParkedDetails().get("number").getAsString());

            contentValues.put(DBContract.ComplaintColumns.V_STATUS , record.getComplaint().get("v_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.V_DATE , record.getComplaint().get("v_date").getAsString());
            contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION , record.getComplaint().get("temp_location").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMMENTS , record.getComplaint().get("comments").getAsString());
            contentValues.put(DBContract.ComplaintColumns.REJECTED , record.getComplaint().get("rejected").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS, record.getComplaint().get("l_status").getAsString());
            return getReadableDatabase().update(DBContract.ComplaintColumns.TABLE_NAME, contentValues, DBContract.ComplaintColumns.REFERENCE + "="+record.getReferenceId(), null);

        }

    /**
     * Inserts Clamping Data to Database
     * @param record
     * @return
     */
     public long insertClampData(ClampRecord record){
         ContentValues contentValues = new ContentValues();
         contentValues.put(DBContract.ClampDataColumns.REFERENCE, record.getReferenceId());
         contentValues.put(DBContract.ClampDataColumns.PLATE_DETAILS, record.getPlateDetails());
         if(record.getBeforeClampPhoto() != null) {
             contentValues.put(DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO, Utils.compressAndEncode(record.getBeforeClampPhoto()));
         }else{
             contentValues.put(DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO, "");
         }
         if(record.getAfterClampPhoto() != null) {
             contentValues.put(DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO, Utils.compressAndEncode(record.getAfterClampPhoto()));
         }else{
             contentValues.put(DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO, "");
         }
         contentValues.put(DBContract.ClampDataColumns.CLAMP_DATE, record.getClampDate());
         return getWritableDatabase().insert(DBContract.ClampDataColumns.TABLE_NAME, null, contentValues);
     }

    public long insertTowingData(TowingRecord record){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.TowingDataColumns.REFERENCE, record.getReferenceId());
        contentValues.put(DBContract.TowingDataColumns.PLATE_DETAILS, record.getPlateDetails());
        if(record.getBeforeTowingPhoto() != null) {
            contentValues.put(DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO, Utils.compressAndEncode(record.getBeforeTowingPhoto()));
        }else{
            contentValues.put(DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO, "");
        }
        if(record.getAfterTowingPhoto() != null) {
            contentValues.put(DBContract.TowingDataColumns.TOWING_AFTER_PHOTO, Utils.compressAndEncode(record.getAfterTowingPhoto()));
        }else{
            contentValues.put(DBContract.TowingDataColumns.TOWING_AFTER_PHOTO, "");
        }
        contentValues.put(DBContract.TowingDataColumns.TOWING_DATE, record.getClampDate());
        return getWritableDatabase().insert(DBContract.TowingDataColumns.TABLE_NAME, null, contentValues);
    }

    /**
     * Returns ComplaintRecord list which are closed after verification
     * @return
     */
    public List<ComplaintRecord> fetchClosedComplaints(){
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='false'", null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String referenceNumber = cursor.getString(1);
                String buildingName = cursor.getString(2);
                String cluster = cursor.getString(3);
                String level = cursor.getString(4);
                String zone = cursor.getString(5);
                String bayNumber = cursor.getString(6);

                String make = cursor.getString(7);
                String color = cursor.getString(8);
                String model = cursor.getString(9);
                String vehicleNumber = cursor.getString(10);

                String complainerName = cursor.getString(11);
                String complainerEmail = cursor.getString(12);
                String complainerContactNumber = cursor.getString(13);

                String date = cursor.getString(14);
                String c_status = cursor.getString(15);
                String t_status = cursor.getString(16);
                String p_status = cursor.getString(17);
                String l_status = cursor.getString(19);
                String v_status = cursor.getString(20);
                String v_date = cursor.getString(21);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status",c_status);
                complaint.addProperty("t_status",t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name",buildingName);
                location.addProperty("cluster",cluster);
                location.addProperty("bay",bayNumber);
                location.addProperty("zone",zone);
                location.addProperty("level",level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name",complainerName);
                reportedBy.addProperty("mail",complainerEmail);
                reportedBy.addProperty("contact",complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make",make);
                vehicle.addProperty("color",color);
                vehicle.addProperty("model",model);
                vehicle.addProperty("number",vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return clampingDoneList;
    }
    
    
    /**
     * Returns ComplaintRecord list which are closed after verification
     * @return
     */
    public List<ComplaintRecord> fetchRejectedComplaints(){
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.REJECTED + "='true'", null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String referenceNumber = cursor.getString(1);
                String buildingName = cursor.getString(2);
                String cluster = cursor.getString(3);
                String level = cursor.getString(4);
                String zone = cursor.getString(5);
                String bayNumber = cursor.getString(6);

                String make = cursor.getString(7);
                String color = cursor.getString(8);
                String model = cursor.getString(9);
                String vehicleNumber = cursor.getString(10);

                String complainerName = cursor.getString(11);
                String complainerEmail = cursor.getString(12);
                String complainerContactNumber = cursor.getString(13);

                String date = cursor.getString(14);
                String c_status = cursor.getString(15);
                String t_status = cursor.getString(16);
                String p_status = cursor.getString(17);
                String l_status = cursor.getString(19);
                String v_status = cursor.getString(20);
                String v_date = cursor.getString(21);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status",c_status);
                complaint.addProperty("t_status",t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name",buildingName);
                location.addProperty("cluster",cluster);
                location.addProperty("bay",bayNumber);
                location.addProperty("zone",zone);
                location.addProperty("level",level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name",complainerName);
                reportedBy.addProperty("mail",complainerEmail);
                reportedBy.addProperty("contact",complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make",make);
                vehicle.addProperty("color",color);
                vehicle.addProperty("model",model);
                vehicle.addProperty("number",vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return clampingDoneList;
    }
    
    public List<ComplaintRecord> fetchPaidComplaints(){

        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND " + DBContract.ComplaintColumns.P_STATUS + "='true'" , null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String referenceNumber = cursor.getString(1);
                String buildingName = cursor.getString(2);
                String cluster = cursor.getString(3);
                String level = cursor.getString(4);
                String zone = cursor.getString(5);
                String bayNumber = cursor.getString(6);

                String make = cursor.getString(7);
                String color = cursor.getString(8);
                String model = cursor.getString(9);
                String vehicleNumber = cursor.getString(10);

                String complainerName = cursor.getString(11);
                String complainerEmail = cursor.getString(12);
                String complainerContactNumber = cursor.getString(13);

                String date = cursor.getString(14);
                String c_status = cursor.getString(15);
                String t_status = cursor.getString(16);
                String p_status = cursor.getString(17);
                String l_status = cursor.getString(19);
                String v_status = cursor.getString(20);
                String v_date = cursor.getString(21);
                int paidAmount = cursor.getInt(24);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status",c_status);
                complaint.addProperty("t_status",t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name",buildingName);
                location.addProperty("cluster",cluster);
                location.addProperty("bay",bayNumber);
                location.addProperty("zone",zone);
                location.addProperty("level",level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name",complainerName);
                reportedBy.addProperty("mail",complainerEmail);
                reportedBy.addProperty("contact",complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make",make);
                vehicle.addProperty("color",color);
                vehicle.addProperty("model",model);
                vehicle.addProperty("number",vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return clampingDoneList;
    
    }
    
    public long updatePaymentStatus(String referenceNumber, String paymentStatus, int amountPaid){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.P_STATUS, paymentStatus);
        contentValues.put(DBContract.ComplaintColumns.AMOUNT_PAID, amountPaid);
        return getWritableDatabase().update(DBContract.ComplaintColumns.TABLE_NAME, contentValues, DBContract.ComplaintColumns.REFERENCE+ "='"+ referenceNumber +"'" , null);
    }
    
    public List<ComplaintRecord> fetchClampedRecords(){

        List<ComplaintRecord> clampedRecords = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND " + DBContract.ComplaintColumns.C_STATUS + "='true' AND "+ ComplaintColumns.T_STATUS + "='false' AND "+ComplaintColumns.P_STATUS + "='false'" , null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String referenceNumber = cursor.getString(1);
                String buildingName = cursor.getString(2);
                String cluster = cursor.getString(3);
                String level = cursor.getString(4);
                String zone = cursor.getString(5);
                String bayNumber = cursor.getString(6);

                String make = cursor.getString(7);
                String color = cursor.getString(8);
                String model = cursor.getString(9);
                String vehicleNumber = cursor.getString(10);

                String complainerName = cursor.getString(11);
                String complainerEmail = cursor.getString(12);
                String complainerContactNumber = cursor.getString(13);

                String date = cursor.getString(14);
                String c_status = cursor.getString(15);
                String t_status = cursor.getString(16);
                String p_status = cursor.getString(17);
                String l_status = cursor.getString(19);
                String v_status = cursor.getString(20);
                String v_date = cursor.getString(21);
                int paidAmount = cursor.getInt(24);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status",c_status);
                complaint.addProperty("t_status",t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name",buildingName);
                location.addProperty("cluster",cluster);
                location.addProperty("bay",bayNumber);
                location.addProperty("zone",zone);
                location.addProperty("level",level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name",complainerName);
                reportedBy.addProperty("mail",complainerEmail);
                reportedBy.addProperty("contact",complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make",make);
                vehicle.addProperty("color",color);
                vehicle.addProperty("model",model);
                vehicle.addProperty("number",vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                clampedRecords.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return clampedRecords;
        
    }
    
    public List<ComplaintRecord> fetchTowRecords(){

            List<ComplaintRecord> clampedRecords = new ArrayList<>();
            Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND " + DBContract.ComplaintColumns.C_STATUS + "='true' AND "+ ComplaintColumns.T_STATUS + "='true' AND "+ ComplaintColumns.P_STATUS + "='false'" , null, null, null, null, null);
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    String referenceNumber = cursor.getString(1);
                    String buildingName = cursor.getString(2);
                    String cluster = cursor.getString(3);
                    String level = cursor.getString(4);
                    String zone = cursor.getString(5);
                    String bayNumber = cursor.getString(6);

                    String make = cursor.getString(7);
                    String color = cursor.getString(8);
                    String model = cursor.getString(9);
                    String vehicleNumber = cursor.getString(10);

                    String complainerName = cursor.getString(11);
                    String complainerEmail = cursor.getString(12);
                    String complainerContactNumber = cursor.getString(13);

                    String date = cursor.getString(14);
                    String c_status = cursor.getString(15);
                    String t_status = cursor.getString(16);
                    String p_status = cursor.getString(17);
                    String l_status = cursor.getString(19);
                    String v_status = cursor.getString(20);
                    String v_date = cursor.getString(21);
                    int paidAmount = cursor.getInt(24);

                    JsonObject complaint = new JsonObject();

                    complaint.addProperty("date", date);
                    complaint.addProperty("v_status", v_status);
                    complaint.addProperty("c_status",c_status);
                    complaint.addProperty("t_status",t_status);
                    complaint.addProperty("p_status", p_status);
                    complaint.addProperty("v_date", v_date);
                    complaint.addProperty("l_status", l_status);
                    complaint.addProperty("paid_amount", paidAmount);

                    JsonObject location = new JsonObject();
                    location.addProperty("name",buildingName);
                    location.addProperty("cluster",cluster);
                    location.addProperty("bay",bayNumber);
                    location.addProperty("zone",zone);
                    location.addProperty("level",level);

                    JsonObject reportedBy = new JsonObject();
                    reportedBy.addProperty("name",complainerName);
                    reportedBy.addProperty("mail",complainerEmail);
                    reportedBy.addProperty("contact",complainerContactNumber);

                    JsonObject vehicle = new JsonObject();
                    vehicle.addProperty("make",make);
                    vehicle.addProperty("color",color);
                    vehicle.addProperty("model",model);
                    vehicle.addProperty("number",vehicleNumber);

                    ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                    clampedRecords.add(complaintRec);

                } while (cursor.moveToNext());
            }
            cursor.close();
            
            return clampedRecords;
            
        }
    
    /**--------------------------------below db for customer --------------------------------------------------**/
    /**
     * Insert new complaint data.
     * @return Newly created row id.
     */
        public long insertNewCustComplaint(Context context, ComplaintRecord record){
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBContract.ComplaintColumns.REFERENCE, record.getReferenceId()); //// TODO: 7/4/2015
            contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME , record.getLocation().get("name").getAsString());
            contentValues.put(DBContract.ComplaintColumns.CLUSTER , record.getLocation().get("cluster").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LEVEL , record.getLocation().get("level").getAsString());
            contentValues.put(DBContract.ComplaintColumns.ZONE , record.getLocation().get("zone").getAsString());
            contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER , record.getLocation().get("bay").getAsString());

            contentValues.put(DBContract.ComplaintColumns.MAKE , record.getWronglyParkedDetails().get("make").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COLOR , record.getWronglyParkedDetails().get("color").getAsString());
            contentValues.put(DBContract.ComplaintColumns.MODEL , record.getWronglyParkedDetails().get("model").getAsString());
            contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS , record.getWronglyParkedDetails().get("number").getAsString());

            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NAME , record.getReportedBy().get("name").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_EMAIL, record.getReportedBy().get("mail").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NUMBER , record.getReportedBy().get("contact").getAsString());

            contentValues.put(DBContract.ComplaintColumns.DATE, record.getComplaint().get("date").getAsString());
            contentValues.put(DBContract.ComplaintColumns.C_STATUS , record.getComplaint().get("c_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.T_STATUS , record.getComplaint().get("t_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.P_STATUS, record.getComplaint().get("p_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS , "true");
            contentValues.put(DBContract.ComplaintColumns.V_STATUS , record.getComplaint().get("v_status").getAsString());
            contentValues.put(DBContract.ComplaintColumns.V_DATE , record.getComplaint().get("v_date").getAsString());
            contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION , record.getComplaint().get("temp_location").getAsString());
            contentValues.put(DBContract.ComplaintColumns.COMMENTS , record.getComplaint().get("comments").getAsString());

            return getWritableDatabase().insert(DBContract.ComplaintColumns.CUST_TABLE_NAME, null, contentValues);

        }


        public List<ComplaintRecord> fetchCustComplaints(Context context){
            List<ComplaintRecord> record = new ArrayList<>();
            Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.CUST_TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='true'", null, null, null, null, null);
            if(cursor != null && cursor.getCount() > 0){
                cursor.moveToFirst();
                do {
                    String referenceNumber = cursor.getString(1);
                    String buildingName = cursor.getString(2);
                    String cluster = cursor.getString(3);
                    String level = cursor.getString(4);
                    String zone = cursor.getString(5);
                    String bayNumber = cursor.getString(6);

                    String make = cursor.getString(7);
                    String color = cursor.getString(8);
                    String model = cursor.getString(9);
                    String vehicleNumber = cursor.getString(10);

                    String complainerName = cursor.getString(11);
                    String complainerEmail = cursor.getString(12);
                    String complainerContactNumber = cursor.getString(13);

                    String date = cursor.getString(14);
                    String c_status = cursor.getString(15);
                    String t_status = cursor.getString(16);
                    String p_status = cursor.getString(17);
                    String l_status = cursor.getString(19);
                    String v_status = cursor.getString(20);

                    JsonObject complaint = new JsonObject();

                    complaint.addProperty("date", date);
                    complaint.addProperty("v_status", v_status);
                    complaint.addProperty("c_status",c_status);
                    complaint.addProperty("t_status",t_status);
                    complaint.addProperty("p_status", p_status);
                    complaint.addProperty("l_status", l_status);
                    

                    JsonObject location = new JsonObject();
                    location.addProperty("name",buildingName);
                    location.addProperty("cluster",cluster);
                    location.addProperty("bay",bayNumber);
                    location.addProperty("zone",zone);
                    location.addProperty("level",level);

                    JsonObject reportedBy = new JsonObject();
                    reportedBy.addProperty("name",complainerName);
                    reportedBy.addProperty("mail",complainerEmail);
                    reportedBy.addProperty("contact",complainerContactNumber);

                    JsonObject vehicle = new JsonObject();
                    vehicle.addProperty("make",make);
                    vehicle.addProperty("color",color);
                    vehicle.addProperty("model",model);
                    vehicle.addProperty("number",vehicleNumber);

                    ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                    record.add(complaintRec);

                } while (cursor.moveToNext());
            }
            cursor.close();

            return record;
        }

   

    /**
     * Returns ComplaintRecord list which are closed after verification
     * @return
     */
    public List<ComplaintRecord> fetchCustClosedComplaints(){
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(DBContract.ComplaintColumns.CUST_TABLE_NAME, null, DBContract.ComplaintColumns.LIVING_STATUS + "='false'", null, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                String referenceNumber = cursor.getString(1);
                String buildingName = cursor.getString(2);
                String cluster = cursor.getString(3);
                String level = cursor.getString(4);
                String zone = cursor.getString(5);
                String bayNumber = cursor.getString(6);

                String make = cursor.getString(7);
                String color = cursor.getString(8);
                String model = cursor.getString(9);
                String vehicleNumber = cursor.getString(10);

                String complainerName = cursor.getString(11);
                String complainerEmail = cursor.getString(12);
                String complainerContactNumber = cursor.getString(13);

                String date = cursor.getString(14);
                String c_status = cursor.getString(15);
                String t_status = cursor.getString(16);
                String p_status = cursor.getString(17);
                String l_status = cursor.getString(19);
                String v_status = cursor.getString(20);
                String v_date = cursor.getString(21);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status",c_status);
                complaint.addProperty("t_status",t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name",buildingName);
                location.addProperty("cluster",cluster);
                location.addProperty("bay",bayNumber);
                location.addProperty("zone",zone);
                location.addProperty("level",level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name",complainerName);
                reportedBy.addProperty("mail",complainerEmail);
                reportedBy.addProperty("contact",complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make",make);
                vehicle.addProperty("color",color);
                vehicle.addProperty("model",model);
                vehicle.addProperty("number",vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(referenceNumber,complaint , location, reportedBy, vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();
        
        return clampingDoneList;
    }
    


}
