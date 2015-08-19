package com.margaret.parking.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.JsonObject;
import com.margaret.parking.db.DBContract.ComplaintColumns;
import com.margaret.parking.pojo.BuildingRecord;
import com.margaret.parking.pojo.ClampRecord;
import com.margaret.parking.pojo.ComplaintRecord;
import com.margaret.parking.pojo.LevelRecord;
import com.margaret.parking.pojo.TowerRecord;
import com.margaret.parking.pojo.TowingRecord;
import com.margaret.parking.util.Utils;

/**
 * Created by varmu02 on 6/26/2015.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Margaret.db";
    private static final int DATABASE_VERSION = 1;
    private static DBOpenHelper dbInstance;

    // USE THE FOLLOWING TABLE FOR COMPLAINT REGISTRATION
    private static final String COMPLAINT_TABLE_CREATE = "CREATE TABLE "
            + DBContract.ComplaintColumns.TABLE_NAME + "("
            + DBContract.ComplaintColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.COMPLAINT_ID + " TEXT NOT NULL, "
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
            + DBContract.ComplaintColumns.LIVING_STATUS
            + " TEXT DEFAULT 'true', " + DBContract.ComplaintColumns.V_STATUS
            + " TEXT, " + DBContract.ComplaintColumns.V_DATE + " TEXT, "
            + DBContract.ComplaintColumns.TEMP_LOCATION + " TEXT, "
            + DBContract.ComplaintColumns.COMMENTS + " TEXT, "
            + ComplaintColumns.AMOUNT_PAID + " INTEGER DEFAULT 0" + ")";
    // ONLY FOR CUSTOMERS - DONT USE FOR OTHER PURPOSE
    private static final String CUST_COMPLAINT_TABLE_CREATE = "CREATE TABLE "
            + DBContract.ComplaintColumns.CUST_TABLE_NAME + "("
            + DBContract.ComplaintColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.COMPLAINT_ID + " TEXT NOT NULL, "
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
            + ComplaintColumns.AMOUNT_PAID + " INTEGER DEFAULT 0" + ")";

    private static final String CLAMPING_TABLE_CREATE = "CREATE TABLE "
            + DBContract.ClampDataColumns.TABLE_NAME + "("
            + DBContract.ClampDataColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.COMPLAINT_ID + " TEXT NOT NULL, "
            + DBContract.ClampDataColumns.REFERENCE + " TEXT, "
            + DBContract.ClampDataColumns.PLATE_DETAILS + " TEXT, "
            + DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO + " BLOB, "
            + DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO + " BLOB, "
            + DBContract.ClampDataColumns.CLAMP_DATE + " TEXT" + ")";
    private static final String TOWING_TABLE_CREATE = "CREATE TABLE "
            + DBContract.TowingDataColumns.TABLE_NAME + "("
            + DBContract.TowingDataColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DBContract.ComplaintColumns.COMPLAINT_ID + " TEXT NOT NULL, "
            + DBContract.TowingDataColumns.REFERENCE + " TEXT, "
            + DBContract.TowingDataColumns.PLATE_DETAILS + " TEXT, "
            + DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO + " BLOB, "
            + DBContract.TowingDataColumns.TOWING_AFTER_PHOTO + " BLOB, "
            + DBContract.TowingDataColumns.TOWING_DATE + " TEXT" + ")";

    private static final String BUILDING_TABLE_CREATE = "CREATE TABLE "
            + DBContract.BuildingsDataColumns.BUILDINGS_TABLE + "("
            + DBContract.BuildingsDataColumns.BUILDING_ID
            + " INTEGER PRIMARY KEY, "
            + DBContract.BuildingsDataColumns.BUILDING_NAME + " TEXT NOT NULL, "
            + DBContract.BuildingsDataColumns.BUILDING_STATUS + " TEXT" + ")";

    private static final String LEVEL_TABLE_CREATE = "CREATE TABLE "
            + DBContract.LevelsDataColumns.LEVELS_TABLE + "("
            + DBContract.LevelsDataColumns.LEVEL_ID
            + " INTEGER PRIMARY KEY, "
            + DBContract.LevelsDataColumns.LEVEL_BUILDING_ID + " INTEGER, "
            + DBContract.LevelsDataColumns.LEVEL_NAME + " TEXT, "
            + DBContract.LevelsDataColumns.LEVEL_ALIAS_NAME + " TEXT, "
            + DBContract.LevelsDataColumns.LEVEL_DES + " TEXT, "
            + DBContract.LevelsDataColumns.LEVEL_IMAGE_URL + " TEXT, "
            + DBContract.LevelsDataColumns.LEVEL_STATUS + " TEXT" + ")";

    private static final String TOWER_TABLE_CREATE = "CREATE TABLE "
            + DBContract.TowersDataColumns.TOWERS_TABLE + "("
            + DBContract.TowersDataColumns.TOWER_ID
            + " INTEGER PRIMARY KEY, "
            + DBContract.TowersDataColumns.TOWER_LEVEL_ID + " INTEGER, "
            + DBContract.TowersDataColumns.TOWER_NAME + " TEXT, "
            + DBContract.TowersDataColumns.TOWER_ALIAS_NAME + " TEXT, "
            + DBContract.TowersDataColumns.TOWER_PLACES + " TEXT, "
            + DBContract.TowersDataColumns.TOWER_EXTRA_PACES + " TEXT, "
            + DBContract.TowersDataColumns.TOWER_STATUS + " TEXT" + ")";

    private DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Singleton method
     *
     * @param context
     * @return
     */
    public static DBOpenHelper getInstance(Context context) {
        if (dbInstance == null) {
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
        db.execSQL(BUILDING_TABLE_CREATE);
        db.execSQL(LEVEL_TABLE_CREATE);
        db.execSQL(TOWER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insertBuildingData(Context context, BuildingRecord record) {
        Log.i("insertBuildingData", "insertBuildingData");
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.BuildingsDataColumns.BUILDING_ID,
                record.getBuildingId());
        contentValues.put(DBContract.BuildingsDataColumns.BUILDING_NAME,
                record.getBuildingName());
        contentValues.put(DBContract.BuildingsDataColumns.BUILDING_STATUS,
                record.getStatus());


        return getWritableDatabase().insert(
                DBContract.BuildingsDataColumns.BUILDINGS_TABLE, null, contentValues);

    }

    public List<BuildingRecord> fetchBuildings(Context context) {
        List<BuildingRecord> record = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.BuildingsDataColumns.BUILDINGS_TABLE, null,
                null, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                BuildingRecord buildingRecord = new BuildingRecord(cursor.getInt(1), cursor.getString(2), cursor.getString(3));

                record.add(buildingRecord);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    public List<String> fetchSpinnerBuildings() {
        List<String> record = new ArrayList<>();
        String buildingName;
        Cursor cursor = getReadableDatabase().query(
                DBContract.BuildingsDataColumns.BUILDINGS_TABLE, null,
                null, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                buildingName = cursor.getString(1);

                record.add(buildingName);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    public int getBuildingId(String buildingName) {

        int buildingId = 1;
        Cursor cursor = getReadableDatabase().query(
                DBContract.BuildingsDataColumns.BUILDINGS_TABLE, null,
                DBContract.BuildingsDataColumns.BUILDING_NAME + "='" + buildingName + "'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                buildingId = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return buildingId;
    }

    public long insertLevelData(Context context, LevelRecord record) {
        Log.i("insertLevelData", "insertLevelData");
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.LevelsDataColumns.LEVEL_ID,
                record.getLevelId());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_BUILDING_ID,
                record.getBuildingId());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_NAME,
                record.getLevelName());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_ALIAS_NAME,
                record.getLevelAliasName());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_DES,
                record.getLevelDesc());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_IMAGE_URL,
                record.getImageUrl());
        contentValues.put(DBContract.LevelsDataColumns.LEVEL_STATUS,
                record.getStatus());


        return getWritableDatabase().insert(
                DBContract.LevelsDataColumns.LEVELS_TABLE, null, contentValues);

    }

    public List<LevelRecord> fetchLevels(int buildingId) {
        List<LevelRecord> record = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.LevelsDataColumns.LEVELS_TABLE, null,
                DBContract.LevelsDataColumns.LEVEL_BUILDING_ID + "=" + buildingId, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                LevelRecord levelRecord = new LevelRecord(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

                record.add(levelRecord);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    public int getLevelId(String levelName) {

        int buildingId = 1;
        Cursor cursor = getReadableDatabase().query(
                DBContract.LevelsDataColumns.LEVELS_TABLE, null,
                DBContract.LevelsDataColumns.LEVEL_NAME + "='" + levelName + "'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                buildingId = cursor.getInt(0);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return buildingId;
    }

    public List<String> fetchSpinnerLevels(String bName) {
        List<String> record = new ArrayList<>();
        String buildingName;
        Cursor cursor = getReadableDatabase().query(
                DBContract.LevelsDataColumns.LEVELS_TABLE, null,
                DBContract.LevelsDataColumns.LEVEL_BUILDING_ID + "=" + getBuildingId(bName), null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                buildingName = cursor.getString(2);

                record.add(buildingName);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    public long insertTowerData(Context context, TowerRecord record) {
        Log.i("insertLevelData", "insertLevelData");
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBContract.TowersDataColumns.TOWER_ID,
                record.getTowerId());
        contentValues.put(DBContract.TowersDataColumns.TOWER_LEVEL_ID,
                record.getLevelId());
        contentValues.put(DBContract.TowersDataColumns.TOWER_NAME,
                record.getTowerName());
        contentValues.put(DBContract.TowersDataColumns.TOWER_ALIAS_NAME,
                record.getTowerlAliasName());
        contentValues.put(DBContract.TowersDataColumns.TOWER_PLACES,
                record.getPlaces());
        contentValues.put(DBContract.TowersDataColumns.TOWER_EXTRA_PACES,
                record.getExtraPlaces());
        contentValues.put(DBContract.TowersDataColumns.TOWER_STATUS,
                record.getStatus());


        return getWritableDatabase().insert(
                DBContract.TowersDataColumns.TOWERS_TABLE, null, contentValues);

    }

    public List<TowerRecord> fetchTowers(int levelId) {
        List<TowerRecord> record = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.TowersDataColumns.TOWERS_TABLE, null,
                DBContract.TowersDataColumns.TOWER_LEVEL_ID + "=" + levelId, null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {

                TowerRecord towerRecord = new TowerRecord(cursor.getInt(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4), cursor.getInt(5), cursor.getInt(6), cursor.getString(7));

                record.add(towerRecord);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    public List<String> fetchSpinnerTowers(String lName) {
        List<String> record = new ArrayList<>();
        String towerName;
        Cursor cursor = getReadableDatabase().query(
                DBContract.TowersDataColumns.TOWERS_TABLE, null,
                DBContract.TowersDataColumns.TOWER_LEVEL_ID + "=" + getLevelId(lName), null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                towerName = cursor.getString(2);

                record.add(towerName);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    /**
     * Insert new complaint data.
     *
     * @return Newly created row id.
     */
    public long insertNewComplaint(Context context, ComplaintRecord record) {
        Log.i("insertNewComplaint", "insertNewComplaint");
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.COMPLAINT_ID,
                record.getComplaintID());

        if (!"".equals(record.getReferenceId()) && record.getReferenceId() != null) {
            contentValues.put(DBContract.ComplaintColumns.REFERENCE,
                    record.getReferenceId()); // // TODO: 7/4/2015
        } else {
            contentValues.put(DBContract.ComplaintColumns.REFERENCE,
                    String.valueOf(new Date().getTime()));
        }
        contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME, record
                .getLocation().get("name").getAsString());
        contentValues.put(DBContract.ComplaintColumns.CLUSTER, record
                .getLocation().get("cluster").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LEVEL, record
                .getLocation().get("level").getAsString());
        contentValues.put(DBContract.ComplaintColumns.ZONE, record
                .getLocation().get("zone").getAsString());
        contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER, record
                .getLocation().get("bay").getAsString());

        contentValues.put(DBContract.ComplaintColumns.MAKE, record
                .getWrongparkeddetails().get("make").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COLOR, record
                .getWrongparkeddetails().get("color").getAsString());
        contentValues.put(DBContract.ComplaintColumns.MODEL, record
                .getWrongparkeddetails().get("model").getAsString());
        contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS, record
                .getWrongparkeddetails().get("number").getAsString());

        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NAME, record
                .getReportedby().get("name").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_EMAIL, record
                .getReportedby().get("mail").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NUMBER, record
                .getReportedby().get("contact").getAsString());

        contentValues.put(DBContract.ComplaintColumns.DATE, record
                .getComplaints().get("date").getAsString());
        contentValues.put(DBContract.ComplaintColumns.C_STATUS, record
                .getComplaints().get("c_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.T_STATUS, record
                .getComplaints().get("t_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.P_STATUS, record
                .getComplaints().get("p_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS, "true");
        contentValues.put(DBContract.ComplaintColumns.V_STATUS, record
                .getComplaints().get("v_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.V_DATE, record
                .getComplaints().get("v_date").getAsString());
        contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION, record
                .getComplaints().get("temp_Location").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMMENTS, record
                .getComplaints().get("comments").getAsString());

        return getWritableDatabase().insert(
                DBContract.ComplaintColumns.TABLE_NAME, null, contentValues);

    }

    /**
     * Updates complaint clamping status
     *
     * @param c_status
     * @param c_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
    public long updateComplaintClampDateAndStatus(String c_status,
                                                  String c_date, String referenceNumber, String vehicleNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.C_STATUS, c_status);
        return getWritableDatabase().update(
                DBContract.ComplaintColumns.TABLE_NAME,
                contentValues,
                DBContract.ComplaintColumns.REFERENCE + "='" + referenceNumber
                        + "'", null);
    }

    /**
     * Updates complaint towing status
     *
     * @param t_status
     * @param t_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
    public long updateComplaintTowingDateAndStatus(String t_status,
                                                   String t_date, String referenceNumber, String vehicleNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.T_STATUS, t_status);
        return getWritableDatabase().update(
                DBContract.ComplaintColumns.TABLE_NAME,
                contentValues,
                DBContract.ComplaintColumns.REFERENCE + "='" + referenceNumber
                        + "'", null);
    }

    /**
     * Updates complaint payment status
     *
     * @param p_status
     * @param p_date
     * @param referenceNumber
     * @param vehicleNumber
     * @return
     */
    public long updateCompaintPaymentStatus(String p_status, String p_date,
                                            String referenceNumber, String vehicleNumber) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.P_STATUS, p_status);
        return getWritableDatabase().update(
                DBContract.ComplaintColumns.TABLE_NAME,
                contentValues,
                DBContract.ComplaintColumns.REFERENCE + "='" + referenceNumber
                        + "'", null);
    }

    public List<ComplaintRecord> fetchComplaints(Context context) {
        List<ComplaintRecord> record = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME, null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);


                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                record.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    /**
     * Updates complaint job verification status. Persists temp location,
     * comments
     *
     * @param context
     * @param record
     * @return
     */
    public long updateJobVerificationStatus(Context context,
                                            ComplaintRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME, record
                .getLocation().get("name").getAsString());
        contentValues.put(DBContract.ComplaintColumns.CLUSTER, record
                .getLocation().get("cluster").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LEVEL, record
                .getLocation().get("level").getAsString());
        contentValues.put(DBContract.ComplaintColumns.ZONE, record
                .getLocation().get("zone").getAsString());
        contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER, record
                .getLocation().get("bay").getAsString());

        contentValues.put(DBContract.ComplaintColumns.MAKE, record
                .getWrongparkeddetails().get("make").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COLOR, record
                .getWrongparkeddetails().get("color").getAsString());
        contentValues.put(DBContract.ComplaintColumns.MODEL, record
                .getWrongparkeddetails().get("model").getAsString());
        contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS, record
                .getWrongparkeddetails().get("number").getAsString());

        contentValues.put(DBContract.ComplaintColumns.V_STATUS, record
                .getComplaints().get("v_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.V_DATE, record
                .getComplaints().get("v_date").getAsString());
        contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION, record
                .getComplaints().get("temp_location").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMMENTS, record
                .getComplaints().get("comments").getAsString());
        contentValues.put(DBContract.ComplaintColumns.REJECTED, record
                .getComplaints().get("rejected").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS, record
                .getComplaints().get("l_status").getAsString());
        return getReadableDatabase().update(
                DBContract.ComplaintColumns.TABLE_NAME,
                contentValues,
                DBContract.ComplaintColumns.REFERENCE + "="
                        + record.getReferenceId(), null);

    }

    /**
     * Inserts Clamping Data to Database
     *
     * @param record
     * @return
     */
    public long insertClampData(ClampRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.COMPLAINT_ID,
                record.getComplaintID());
        contentValues.put(DBContract.ClampDataColumns.REFERENCE,
                record.getReferenceId());
        contentValues.put(DBContract.ClampDataColumns.PLATE_DETAILS,
                record.getPlateDetails());
        if (record.getBeforeClampPhoto() != null) {
            contentValues.put(DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO,
                    Utils.compressAndEncode(record.getBeforeClampPhoto()));
        } else {
            contentValues.put(DBContract.ClampDataColumns.CLAMP_BEFORE_PHOTO,
                    "");
        }
        if (record.getAfterClampPhoto() != null) {
            contentValues.put(DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO,
                    Utils.compressAndEncode(record.getAfterClampPhoto()));
        } else {
            contentValues
                    .put(DBContract.ClampDataColumns.CLAMP_AFTER_PHOTO, "");
        }
        contentValues.put(DBContract.ClampDataColumns.CLAMP_DATE,
                record.getClampDate());
        return getWritableDatabase().insert(
                DBContract.ClampDataColumns.TABLE_NAME, null, contentValues);
    }

    public long insertTowingData(TowingRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.COMPLAINT_ID,
                record.getComplaintID());
        contentValues.put(DBContract.TowingDataColumns.REFERENCE,
                record.getReferenceId());
        contentValues.put(DBContract.TowingDataColumns.PLATE_DETAILS,
                record.getPlateDetails());
        if (record.getBeforeTowingPhoto() != null) {
            contentValues.put(DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO,
                    Utils.compressAndEncode(record.getBeforeTowingPhoto()));
        } else {
            contentValues.put(DBContract.TowingDataColumns.TOWING_BEFORE_PHOTO,
                    "");
        }
        if (record.getAfterTowingPhoto() != null) {
            contentValues.put(DBContract.TowingDataColumns.TOWING_AFTER_PHOTO,
                    Utils.compressAndEncode(record.getAfterTowingPhoto()));
        } else {
            contentValues.put(DBContract.TowingDataColumns.TOWING_AFTER_PHOTO,
                    "");
        }
        contentValues.put(DBContract.TowingDataColumns.TOWING_DATE,
                record.getClampDate());
        return getWritableDatabase().insert(
                DBContract.TowingDataColumns.TABLE_NAME, null, contentValues);
    }

    /**
     * Returns ComplaintRecord list which are closed after verification
     *
     * @return
     */
    public List<ComplaintRecord> fetchClosedComplaints() {
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME, null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='false'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampingDoneList;
    }

    /**
     * Returns ComplaintRecord list which are closed after verification
     *
     * @return
     */
    public List<ComplaintRecord> fetchRejectedComplaints() {
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME, null,
                DBContract.ComplaintColumns.REJECTED + "='true'", null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampingDoneList;
    }

    public List<ComplaintRecord> fetchNewComplaints() {

        List<ComplaintRecord> clampedRecords = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME,
                null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND "
                        + DBContract.ComplaintColumns.C_STATUS + "='false' AND "
                        + ComplaintColumns.T_STATUS + "='false' AND "
                        + ComplaintColumns.V_STATUS + "='false' AND "
                        + ComplaintColumns.P_STATUS + "='false'", null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);
                int paidAmount = cursor.getInt(25);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampedRecords.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampedRecords;

    }

    public List<ComplaintRecord> fetchPaidComplaints() {

        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME,
                null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND "
                        + DBContract.ComplaintColumns.P_STATUS + "='true'",
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);
                int paidAmount = cursor.getInt(25);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampingDoneList;

    }

    public long updatePaymentStatus(String referenceNumber,
                                    String paymentStatus, int amountPaid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.P_STATUS, paymentStatus);
        contentValues.put(DBContract.ComplaintColumns.AMOUNT_PAID, amountPaid);
        return getWritableDatabase().update(
                DBContract.ComplaintColumns.TABLE_NAME,
                contentValues,
                DBContract.ComplaintColumns.REFERENCE + "='" + referenceNumber
                        + "'", null);
    }

    public List<ComplaintRecord> fetchClampedRecords() {

        List<ComplaintRecord> clampedRecords = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME,
                null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND "
                        + DBContract.ComplaintColumns.C_STATUS + "='true' AND "
                        + ComplaintColumns.T_STATUS + "='false' AND "
                        + ComplaintColumns.C_STATUS + "='true'", null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);
                int paidAmount = cursor.getInt(25);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampedRecords.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampedRecords;

    }

    public List<ComplaintRecord> fetchTowRecords() {

        List<ComplaintRecord> clampedRecords = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.TABLE_NAME,
                null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true' AND "
                        + DBContract.ComplaintColumns.C_STATUS + "='true' AND "
                        + ComplaintColumns.T_STATUS + "='true' AND "
                        + ComplaintColumns.P_STATUS + "='false'", null, null,
                null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);
                int paidAmount = cursor.getInt(25);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);
                complaint.addProperty("paid_amount", paidAmount);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampedRecords.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampedRecords;

    }

    /**
     * --------------------------------below db for customer
     * --------------------------------------------------
     **/
    /**
     * Insert new complaint data.
     *
     * @return Newly created row id.
     */
    public long insertNewCustComplaint(Context context, ComplaintRecord record) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBContract.ComplaintColumns.COMPLAINT_ID,
                record.getComplaintID());

        if (!"".equals(record.getReferenceId()) && record.getReferenceId() != null) {
            contentValues.put(DBContract.ComplaintColumns.REFERENCE,
                    record.getReferenceId()); // // TODO: 7/4/2015
        } else {
            contentValues.put(DBContract.ComplaintColumns.REFERENCE,
                    String.valueOf(new Date().getTime()));
        }

        contentValues.put(DBContract.ComplaintColumns.BUILDING_NAME, record
                .getLocation().get("name").getAsString());
        contentValues.put(DBContract.ComplaintColumns.CLUSTER, record
                .getLocation().get("cluster").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LEVEL, record
                .getLocation().get("level").getAsString());
        contentValues.put(DBContract.ComplaintColumns.ZONE, record
                .getLocation().get("zone").getAsString());
        contentValues.put(DBContract.ComplaintColumns.BAY_NUMBER, record
                .getLocation().get("bay").getAsString());

        contentValues.put(DBContract.ComplaintColumns.MAKE, record
                .getWrongparkeddetails().get("make").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COLOR, record
                .getWrongparkeddetails().get("color").getAsString());
        contentValues.put(DBContract.ComplaintColumns.MODEL, record
                .getWrongparkeddetails().get("model").getAsString());
        contentValues.put(DBContract.ComplaintColumns.PLATE_DETAILS, record
                .getWrongparkeddetails().get("number").getAsString());

        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NAME, record
                .getReportedby().get("name").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_EMAIL, record
                .getReportedby().get("mail").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMPLAINER_NUMBER, record
                .getReportedby().get("contact").getAsString());

        contentValues.put(DBContract.ComplaintColumns.DATE, record
                .getComplaints().get("date").getAsString());
        contentValues.put(DBContract.ComplaintColumns.C_STATUS, record
                .getComplaints().get("c_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.T_STATUS, record
                .getComplaints().get("t_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.P_STATUS, record
                .getComplaints().get("p_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.LIVING_STATUS, "true");
        contentValues.put(DBContract.ComplaintColumns.V_STATUS, record
                .getComplaints().get("v_status").getAsString());
        contentValues.put(DBContract.ComplaintColumns.V_DATE, record
                .getComplaints().get("v_date").getAsString());
        contentValues.put(DBContract.ComplaintColumns.TEMP_LOCATION, record
                .getComplaints().get("temp_Location").getAsString());
        contentValues.put(DBContract.ComplaintColumns.COMMENTS, record
                .getComplaints().get("comments").getAsString());

        Log.i("stored", "stored");

        return getWritableDatabase().insert(
                DBContract.ComplaintColumns.CUST_TABLE_NAME, null,
                contentValues);

    }

    public List<ComplaintRecord> fetchCustComplaints(Context context) {
        List<ComplaintRecord> record = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.CUST_TABLE_NAME, null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='true'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                record.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return record;
    }

    /**
     * Returns ComplaintRecord list which are closed after verification
     *
     * @return
     */
    public List<ComplaintRecord> fetchCustClosedComplaints() {
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                DBContract.ComplaintColumns.CUST_TABLE_NAME, null,
                DBContract.ComplaintColumns.LIVING_STATUS + "='false'", null,
                null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampingDoneList;
    }

    public List<ComplaintRecord> fetchNewComplaintsFromDB(String tableName) {
        Log.i("tableName ", tableName);
        List<ComplaintRecord> clampingDoneList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().query(
                tableName, null,
                DBContract.ComplaintColumns.COMPLAINT_ID + "=''", null,
                null, null, null, null);
        Log.i("cursor ", cursor.getCount() + "");
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                String complaintId = cursor.getString(1);
                String referenceNumber = cursor.getString(2);
                String buildingName = cursor.getString(3);
                String cluster = cursor.getString(4);
                String level = cursor.getString(5);
                String zone = cursor.getString(6);
                String bayNumber = cursor.getString(7);

                String make = cursor.getString(8);
                String color = cursor.getString(9);
                String model = cursor.getString(10);
                String vehicleNumber = cursor.getString(11);

                String complainerName = cursor.getString(12);
                String complainerEmail = cursor.getString(13);
                String complainerContactNumber = cursor.getString(14);

                String date = cursor.getString(15);
                String c_status = cursor.getString(16);
                String t_status = cursor.getString(17);
                String p_status = cursor.getString(18);
                String l_status = cursor.getString(20);
                String v_status = cursor.getString(21);
                String v_date = cursor.getString(22);

                JsonObject complaint = new JsonObject();

                complaint.addProperty("date", date);
                complaint.addProperty("v_status", v_status);
                complaint.addProperty("c_status", c_status);
                complaint.addProperty("t_status", t_status);
                complaint.addProperty("p_status", p_status);
                complaint.addProperty("v_date", v_date);
                complaint.addProperty("l_status", l_status);

                JsonObject location = new JsonObject();
                location.addProperty("name", buildingName);
                location.addProperty("cluster", cluster);
                location.addProperty("bay", bayNumber);
                location.addProperty("zone", zone);
                location.addProperty("level", level);

                JsonObject reportedBy = new JsonObject();
                reportedBy.addProperty("name", complainerName);
                reportedBy.addProperty("mail", complainerEmail);
                reportedBy.addProperty("contact", complainerContactNumber);

                JsonObject vehicle = new JsonObject();
                vehicle.addProperty("make", make);
                vehicle.addProperty("color", color);
                vehicle.addProperty("model", model);
                vehicle.addProperty("number", vehicleNumber);

                ComplaintRecord complaintRec = new ComplaintRecord(complaintId,
                        referenceNumber, complaint, location, reportedBy,
                        vehicle);
                clampingDoneList.add(complaintRec);

            } while (cursor.moveToNext());
        }
        cursor.close();

        return clampingDoneList;
    }

}
