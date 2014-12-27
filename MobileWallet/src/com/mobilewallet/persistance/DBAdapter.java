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

	// Questions table properties
	public static final String QT_TABLE_NAME = "questions";
	public static final String QT_NO = "qt_no";
	public static final String QUESTION = "question";
	public static final String ANSWERA = "answerA";
	public static final String ANSWERB = "answerB";
	public static final String ANSWERC = "answerC";
	public static final String ANSWERD = "answerD";
	public static final String ANSWER = "answer";
	public static final String EXPLANATION = "explanation";
	public static final String QT_TYPE = "qt_type";

	private static final String CREATE_OFFERS_TABLE = "CREATE TABLE "
			+ TABLE_NAME + " (" + OFFER_ID + " TEXT PRIMARY KEY, " + TITLE
			+ " TEXT, " + SUB_TITLE + " TEXT, " + DES + " TEXT, " + STEPS
			+ " TEXT, " + URL + " TEXT, " + AMOUNT + " TEXT, " + "" + PACKAGE
			+ " TEXT, " + TYPE + " TEXT, " + STATUS + " TEXT, " + CLICKED
			+ " TEXT, " + APP_STATUS + " TEXT, " + CURRENT_MILLISECONDS
			+ " TEXT, " + POSITION + " INTEGER , " + IMAGE_ID + " INTEGER , "
			+ APP_EXISTS + " TEXT);";

	private static final String CREATE_QUESTIONS_TABLE = "CREAT TABLE"
			+ QT_TABLE_NAME + "(" + QT_NO + " INTEGER PRIMARY KEY, " + QUESTION
			+ " TEXT, " + ANSWERA + " TEXT," + ANSWERB + " TEXT, " + ANSWERC
			+ " TEXT, " + ANSWERD + " TEXT, " + ANSWER + " TEXT," + EXPLANATION
			+ " TEXT, " + QT_TYPE + " qt_type);";

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_OFFERS_TABLE);
		db.execSQL(CREATE_QUESTIONS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_QUESTIONS_TABLE);
		onCreate(db);
	}

}
