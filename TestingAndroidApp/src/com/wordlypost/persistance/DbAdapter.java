package com.wordlypost.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter extends SQLiteOpenHelper {

	private static final String DB_NAME = "wordlypost.db";
	private static final int DB_VERSION = 1;

	public static final String CATEGORIES_TABLE_NAME = "categories";
	public static final String C_ID = "category_id";
	public static final String C_NAME = "category_name";
	public static final String C_SLUG = "category_slug";
	public static final String C_POST_COUNT = "category_post_count";

	public static final String CATEGORRY_POSTS_TABLE_NAME = "categorory_posts";
	public static final String P_ID = "post_id";
	public static final String P_TITLE = "post_title";
	public static final String P_DATE = "post_date";
	public static final String P_ICON_URL = "post_icon_url";
	public static final String P_AUTHOR_NAME = "post_author_name";
	public static final String P_CONTENT = "post_content";
	public static final String P_SCREEN_IMAGE_URL = "post_screen_image_url";
	public static final String P_COMMENT_COUNT = "post_comment_count";
	public static final String P_URL = "post_url";
	public static final String P_CURRENT_MILLISECONDS = "current_milliseconds";
	public static final String PC_ID = "category_id";
	public static final String PC_SLUG = "category_slug";

	public static final String RECENT_POSTS_TABLE_NAME = "recent_posts";
	public static final String RP_ID = "post_id";
	public static final String RP_TITLE = "post_title";
	public static final String RP_DATE = "post_date";
	public static final String RP_ICON_URL = "post_icon_url";
	public static final String RP_AUTHOR_NAME = "post_author_name";
	public static final String RP_CONTENT = "post_comment";
	public static final String RP_SCREEN_IMAGE_URL = "post_screen_image_url";
	public static final String RP_COMMENT_COUNT = "post_comment_count";
	public static final String RP_URL = "post_url";
	public static final String RP_CURRENT_MILLISECONDS = "current_milliseconds";

	private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + CATEGORIES_TABLE_NAME
			+ " (" + C_ID + " INTEGER PRIMARY KEY, " + C_NAME + " TEXT, " + C_SLUG + " TEXT, "
			+ C_POST_COUNT + " INTEGER);";

	private static final String CREATE_POSTS_TABLE = "CREATE TABLE " + CATEGORRY_POSTS_TABLE_NAME
			+ " (" + P_ID + " INTEGER PRIMARY KEY, " + P_TITLE + " TEXT, " + P_DATE + " TEXT, "
			+ P_ICON_URL + " TEXT, " + P_AUTHOR_NAME + " TEXT, " + P_CONTENT + " TEXT, "
			+ P_SCREEN_IMAGE_URL + " TEXT, " + "" + P_COMMENT_COUNT + " INTEGER, " + P_URL
			+ " TEXT, " + P_CURRENT_MILLISECONDS + " TEXT, " + PC_ID + " INTEGER, " + PC_SLUG
			+ " TEXT);";

	private static final String CREATE_RECENT_POSTS_TABLE = "CREATE TABLE "
			+ RECENT_POSTS_TABLE_NAME + " (" + RP_ID + " INTEGER PRIMARY KEY, " + RP_TITLE
			+ " TEXT, " + RP_DATE + " TEXT, " + RP_ICON_URL + " TEXT, " + RP_AUTHOR_NAME
			+ " TEXT, " + RP_CONTENT + " TEXT, " + RP_SCREEN_IMAGE_URL + " TEXT, " + ""
			+ RP_COMMENT_COUNT + " INTEGER, " + RP_URL + " TEXT, " + RP_CURRENT_MILLISECONDS
			+ " TEXT);";

	public DbAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_POSTS_TABLE);
		db.execSQL(CREATE_RECENT_POSTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_POSTS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + CREATE_RECENT_POSTS_TABLE);
		onCreate(db);
	}
}
