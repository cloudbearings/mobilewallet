package com.margaret.parking.db;

import android.provider.BaseColumns;

/**
 * Created by varmu02 on 6/26/2015.
 */
public class DBContract {

    private DBContract(){

    }

    /** COMPLAINT REGISTRATION TABLE**/
    public static abstract class ComplaintColumns implements BaseColumns {
        public static final String TABLE_NAME = "complaint";
        public static final String CUST_TABLE_NAME = "custcomplaint";

        public static final String COMPLAINT_ID = "complaintID";
        public static final String BUILDING_NAME = "building";
        public static final String CLUSTER = "cluster";
        public static final String LEVEL = "level";
        public static final String ZONE = "zone";
        public static final String BAY_NUMBER = "bay";
        public static final String REFERENCE = "reference";
        public static final String MAKE = "make";
        public static final String COLOR = "color";
        public static final String MODEL = "model";
        public static final String PLATE_DETAILS = "vehicle_number";
        public static final String COMPLAINER_NAME = "complainer_name";
        public static final String COMPLAINER_EMAIL = "complainer_email";
        public static final String COMPLAINER_NUMBER = "complainer_contact";

        public static final String DATE = "date_created";
        public static final String C_STATUS = "clamp_status";
        public static final String T_STATUS = "tow_status";
        public static final String P_STATUS = "payment_status";
        public static final String REJECTED = "isrejected";
        public static final String LIVING_STATUS = "is_active";
        public static final String V_STATUS = "verification_status";

        public static final String V_DATE = "verified_date";
        public static final String TEMP_LOCATION = "temp_location";
        public static final String COMMENTS = "comments";
        public static final String AMOUNT_PAID = "paid_amount";


    }

    public static abstract  class ClampDataColumns implements BaseColumns{
        public static final String TABLE_NAME = "clamp";
        public static final String PLATE_DETAILS = "vehicle_number";
        public static final String REFERENCE = "reference";
        public static final String CLAMP_BEFORE_PHOTO = "clamp_before_bitmap";
        public static final String CLAMP_AFTER_PHOTO = "clamp_after_bitmap";
        public static final String CLAMP_DATE = "clamp_date";
        public static final String CLAMP_STATUS = "c_status";
    }

    public static abstract  class TowingDataColumns implements BaseColumns{
        public static final String TABLE_NAME = "towing";
        public static final String PLATE_DETAILS = "vehicle_number";
        public static final String REFERENCE = "reference";
        public static final String TOWING_BEFORE_PHOTO = "tow_before_bitmap";
        public static final String TOWING_AFTER_PHOTO = "tow_after_bitmap";
        public static final String TOWING_DATE = "tow_date";
        public static final String TOWING_STATUS = "t_status";
    }

    public static abstract  class BuildingsDataColumns implements BaseColumns{
        public static final String BUILDINGS_TABLE = "buildings";
        public static final String BUILDING_ID = "buildingId";
        public static final String BUILDING_NAME = "buildingName";
        public static final String BUILDING_STATUS = "status";
    }

    public static abstract  class LevelsDataColumns implements BaseColumns{
        public static final String LEVELS_TABLE = "levels";
        public static final String LEVEL_ID = "levelId";
        public static final String LEVEL_BUILDING_ID = "buildingId";
        public static final String LEVEL_NAME = "levelName";
        public static final String LEVEL_ALIAS_NAME = "levelAliasName";
        public static final String LEVEL_DES = "levelDesc";
        public static final String LEVEL_IMAGE_URL = "imageUrl";
        public static final String LEVEL_STATUS = "status";
    }

    public static abstract  class TowersDataColumns implements BaseColumns{
        public static final String TOWERS_TABLE = "towers";
        public static final String TOWER_ID = "towerId";
        public static final String TOWER_LEVEL_ID = "levelId";
        public static final String TOWER_NAME = "towerName";
        public static final String TOWER_ALIAS_NAME = "towerlAliasName";
        public static final String TOWER_PLACES = "places";
        public static final String TOWER_EXTRA_PACES = "extraPlaces";
        public static final String TOWER_STATUS = "status";
    }
}
