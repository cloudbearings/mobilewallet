package com.wordlypost.persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter extends SQLiteOpenHelper {

	private static final String DB_NAME = "wordlypost.db";
	private static final int DB_VERSION = 1;

	// Categories table columns
	public static final String CATEGORIES_TABLE_NAME = "categories";
	public static final String C_ID = "category_id";
	public static final String C_NAME = "category_name";
	public static final String C_SLUG = "category_slug";
	public static final String C_POST_COUNT = "category_post_count";

	// Category posts table columns
	public static final String CATEGORRY_POSTS_TABLE_NAME = "category_posts";
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
	public static final String P_COMMENTS = "comments";
	public static final String P_TAGS = "tags";

	// Recent posts table columns
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
	public static final String RP_EXCERPT = "excerpt";
	public static final String RP_COMMENTS = "comments";
	public static final String RP_TAGS = "tags";

	// Tag posts table columns
	public static final String TAG_POSTS_TABLE_NAME = "tag_posts";
	public static final String TP_ID = "post_id";
	public static final String TP_TITLE = "post_title";
	public static final String TP_DATE = "post_date";
	public static final String TP_ICON_URL = "post_icon_url";
	public static final String TP_AUTHOR_NAME = "post_author_name";
	public static final String TP_CONTENT = "post_comment";
	public static final String TP_SCREEN_IMAGE_URL = "post_screen_image_url";
	public static final String TP_COMMENT_COUNT = "post_comment_count";
	public static final String TP_URL = "post_url";
	public static final String TP_CURRENT_MILLISECONDS = "current_milliseconds";
	public static final String TP_EXCERPT = "excerpt";
	public static final String TP_COMMENTS = "comments";
	public static final String TP_TAGS = "tags";

	// Search posts table columns
	public static final String SEARCH_POSTS_TABLE_NAME = "search_posts";
	public static final String SP_ID = "post_id";
	public static final String SP_TITLE = "post_title";
	public static final String SP_DATE = "post_date";
	public static final String SP_ICON_URL = "post_icon_url";
	public static final String SP_AUTHOR_NAME = "post_author_name";
	public static final String SP_CONTENT = "post_comment";
	public static final String SP_SCREEN_IMAGE_URL = "post_screen_image_url";
	public static final String SP_COMMENT_COUNT = "post_comment_count";
	public static final String SP_URL = "post_url";
	public static final String SP_CURRENT_MILLISECONDS = "current_milliseconds";
	public static final String SP_EXCERPT = "excerpt";
	public static final String SP_COMMENTS = "comments";
	public static final String SP_TAGS = "tags";

	// Home posts table columns
	public static final String HOME_POSTS_TABLE_NAME = "home_posts";
	public static final String H_ID = "post_id";
	public static final String H_TITLE = "post_title";
	public static final String H_DATE = "post_date";
	public static final String H_ICON_URL = "post_icon_url";
	public static final String H_AUTHOR_NAME = "post_author_name";
	public static final String H_CONTENT = "post_comment";
	public static final String H_SCREEN_IMAGE_URL = "post_screen_image_url";
	public static final String H_COMMENT_COUNT = "post_comment_count";
	public static final String H_URL = "post_url";
	public static final String H_CURRENT_MILLISECONDS = "current_milliseconds";
	public static final String H_EXCERPT = "excerpt";
	public static final String HC_ID = "category_id";
	public static final String HC_SLUG = "category_slug";
	public static final String H_COMMENTS = "comments";
	public static final String H_TAGS = "tags";

	private static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE "
			+ CATEGORIES_TABLE_NAME + " (" + C_ID + " INTEGER PRIMARY KEY, "
			+ C_NAME + " TEXT, " + C_SLUG + " TEXT, " + C_POST_COUNT
			+ " INTEGER);";

	private static final String CREATE_POSTS_TABLE = "CREATE TABLE "
			+ CATEGORRY_POSTS_TABLE_NAME + " (" + P_ID
			+ " INTEGER PRIMARY KEY, " + P_TITLE + " TEXT, " + P_DATE
			+ " TEXT, " + P_ICON_URL + " TEXT, " + P_AUTHOR_NAME + " TEXT, "
			+ P_CONTENT + " TEXT, " + P_SCREEN_IMAGE_URL + " TEXT, " + ""
			+ P_COMMENT_COUNT + " INTEGER, " + P_URL + " TEXT, "
			+ P_CURRENT_MILLISECONDS + " TEXT, " + PC_ID + " INTEGER, "
			+ PC_SLUG + " TEXT, " + P_COMMENTS + " TEXT, " + P_TAGS + " TEXT);";

	private static final String CREATE_RECENT_POSTS_TABLE = "CREATE TABLE "
			+ RECENT_POSTS_TABLE_NAME + " (" + RP_ID + " INTEGER PRIMARY KEY, "
			+ RP_TITLE + " TEXT, " + RP_DATE + " TEXT, " + RP_ICON_URL
			+ " TEXT, " + RP_AUTHOR_NAME + " TEXT, " + RP_CONTENT + " TEXT, "
			+ RP_SCREEN_IMAGE_URL + " TEXT, " + "" + RP_COMMENT_COUNT
			+ " INTEGER, " + RP_URL + " TEXT, " + RP_CURRENT_MILLISECONDS
			+ " TEXT, " + RP_EXCERPT + " TEXT, " + RP_COMMENTS + " TEXT, "
			+ RP_TAGS + " TEXT);";

	private static final String CREATE_TAG_POSTS_TABLE = "CREATE TABLE "
			+ TAG_POSTS_TABLE_NAME + " (" + TP_ID + " INTEGER PRIMARY KEY, "
			+ TP_TITLE + " TEXT, " + TP_DATE + " TEXT, " + TP_ICON_URL
			+ " TEXT, " + TP_AUTHOR_NAME + " TEXT, " + TP_CONTENT + " TEXT, "
			+ TP_SCREEN_IMAGE_URL + " TEXT, " + "" + TP_COMMENT_COUNT
			+ " INTEGER, " + TP_URL + " TEXT, " + TP_CURRENT_MILLISECONDS
			+ " TEXT, " + TP_EXCERPT + " TEXT, " + TP_COMMENTS + " TEXT, "
			+ TP_TAGS + " TEXT);";

	private static final String CREATE_SEARCH_POSTS_TABLE = "CREATE TABLE "
			+ SEARCH_POSTS_TABLE_NAME + " (" + SP_ID + " INTEGER PRIMARY KEY, "
			+ SP_TITLE + " TEXT, " + SP_DATE + " TEXT, " + SP_ICON_URL
			+ " TEXT, " + SP_AUTHOR_NAME + " TEXT, " + SP_CONTENT + " TEXT, "
			+ SP_SCREEN_IMAGE_URL + " TEXT, " + "" + SP_COMMENT_COUNT
			+ " INTEGER, " + SP_URL + " TEXT, " + SP_CURRENT_MILLISECONDS
			+ " TEXT, " + SP_EXCERPT + " TEXT, " + SP_COMMENTS + " TEXT, "
			+ SP_TAGS + " TEXT);";

	private static final String CREATE_HOME_POSTS_TABLE = "CREATE TABLE "
			+ HOME_POSTS_TABLE_NAME + " (" + H_ID + " INTEGER PRIMARY KEY, "
			+ H_TITLE + " TEXT, " + H_DATE + " TEXT, " + H_ICON_URL + " TEXT, "
			+ H_AUTHOR_NAME + " TEXT, " + H_CONTENT + " TEXT, "
			+ H_SCREEN_IMAGE_URL + " TEXT, " + "" + H_COMMENT_COUNT
			+ " INTEGER, " + H_URL + " TEXT, " + H_CURRENT_MILLISECONDS
			+ " TEXT, " + H_EXCERPT + " TEXT, " + HC_ID + " INTEGER, "
			+ HC_SLUG + " TEXT, " + H_COMMENTS + " TEXT, " + H_TAGS + " TEXT);";

	public DbAdapter(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CATEGORIES_TABLE);
		db.execSQL(CREATE_POSTS_TABLE);
		db.execSQL(CREATE_RECENT_POSTS_TABLE);
		db.execSQL(CREATE_TAG_POSTS_TABLE);
		db.execSQL(CREATE_SEARCH_POSTS_TABLE);
		db.execSQL(CREATE_HOME_POSTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + CATEGORRY_POSTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + RECENT_POSTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + TAG_POSTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + SEARCH_POSTS_TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + HOME_POSTS_TABLE_NAME);
		onCreate(db);
	}
}
