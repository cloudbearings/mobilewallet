package com.mobilewallet.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper {

	private static final String DB_NAME = "mobilewallet.db";
	// Current app version 1.0.7
	private static final int DB_VERSION = 1;

	public static final String TABLE_NAME = "offers";
	public static final String OFFER_ID = "offer_id";
	public static final String TITLE = "title";
	public static final String SUB_TITLE = "sub_title";
	public static final String DES = "des";
	public static final String STEPS = "steps";
	public static final String URL = "url";
	public static final String AMOUNT = "amount";
	public static final String PACKAGE = "package";
	public static final String TYPE = "type";
	public static final String STATUS = "status";
	public static final String CLICKED = "clicked";
	public static final String APP_STATUS = "app_status";
	public static final String CURRENT_MILLISECONDS = "current_milliseconds";
	public static final String POSITION = "position";
	public static final String IMAGE_ID = "image_id";
	public static final String APP_EXISTS = "app_exists";

	private static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + OFFER_ID
			+ " TEXT PRIMARY KEY, " + TITLE + " TEXT, " + SUB_TITLE + " TEXT, " + DES + " TEXT, "
			+ STEPS + " TEXT, " + URL + " TEXT, " + AMOUNT + " TEXT, " + "" + PACKAGE + " TEXT, "
			+ TYPE + " TEXT, " + STATUS + " TEXT, " + CLICKED + " TEXT, " + APP_STATUS + " TEXT, "
			+ CURRENT_MILLISECONDS + " TEXT, " + POSITION + " INTEGER , " + IMAGE_ID
			+ " INTEGER , " + APP_EXISTS + " TEXT);";

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(STRING_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}


}
