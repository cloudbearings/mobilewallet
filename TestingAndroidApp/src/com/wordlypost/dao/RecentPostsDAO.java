package com.wordlypost.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wordlypost.beans.PostRowItem;
import com.wordlypost.persistance.DbAdapter;

public class RecentPostsDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public RecentPostsDAO(Context context) {
		this.context = context;
	}

	public RecentPostsDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertRecentPosts(int post_id, String post_title, String post_date,
			String post_icon_url, String post_author_name, String post_content,
			String post_screen_image_url, int post_comment_count, String post_url,
			String current_milliseconds, String excerpt, String comments, String tags) {
		ContentValues cv = new ContentValues(13);
		cv.put(DbAdapter.RP_ID, post_id);
		cv.put(DbAdapter.RP_TITLE, post_title);
		cv.put(DbAdapter.RP_DATE, post_date);
		cv.put(DbAdapter.RP_ICON_URL, post_icon_url);
		cv.put(DbAdapter.RP_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.RP_CONTENT, post_content);
		cv.put(DbAdapter.RP_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.RP_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.RP_URL, post_url);
		cv.put(DbAdapter.RP_CURRENT_MILLISECONDS, current_milliseconds);
		cv.put(DbAdapter.RP_EXCERPT, excerpt);
		cv.put(DbAdapter.RP_COMMENTS, comments);
		cv.put(DbAdapter.RP_TAGS, tags);

		opnToWrite();
		long val = 0;
		if (isRecentPostExists(post_id) > 0) {
			val = database.update(DbAdapter.RECENT_POSTS_TABLE_NAME, cv, DbAdapter.RP_ID + "="
					+ post_id, null);
			Close();
		} else {
			val = database.insert(DbAdapter.RECENT_POSTS_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long isRecentPostExists(int post_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + DbAdapter.RECENT_POSTS_TABLE_NAME + " where "
					+ DbAdapter.RP_ID + "=" + post_id;

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			SQLiteStatement statement = database.compileStatement(sql);
			count = statement.simpleQueryForLong();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return count;
	}

	public long getRecentPostsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.RP_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.RECENT_POSTS_TABLE_NAME, cols, null, null, null, null,
					null);
			count = c.getCount();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!c.isClosed()) {
					c.close();
				}
			} catch (Exception e) {
			}
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return count;
	}

	public ArrayList<PostRowItem> getRecentPosts() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		ArrayList<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.RP_ID, DbAdapter.RP_TITLE, DbAdapter.RP_DATE,
					DbAdapter.RP_ICON_URL, DbAdapter.RP_AUTHOR_NAME, DbAdapter.RP_CONTENT,
					DbAdapter.RP_SCREEN_IMAGE_URL, DbAdapter.RP_COMMENT_COUNT, DbAdapter.RP_URL,
					DbAdapter.RP_EXCERPT, DbAdapter.RP_COMMENTS, DbAdapter.RP_TAGS };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.RECENT_POSTS_TABLE_NAME, cols, null, null, null,
					null, DbAdapter.RP_DATE + " DESC");
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor.getColumnIndex(DbAdapter.RP_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_TITLE)));
					item.setDate(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.RP_ICON_URL)));
					item.setAuthor(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.RP_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.RP_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_URL)));
					item.setPost_des(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_EXCERPT)));
					item.setCommentsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.RP_COMMENTS)));
					item.setTagsArray(cursor.getString(cursor.getColumnIndex(DbAdapter.RP_TAGS)));

					postsList.add(item);
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			} catch (Exception e) {
			}
			try {
				database.close();
			} catch (Exception e) {
			}
		}
		return postsList;
	}

	public long deleteRecentPosts(String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DbAdapter.RECENT_POSTS_TABLE_NAME,
				DbAdapter.RP_CURRENT_MILLISECONDS + "!='" + currentMilliseconds + "'", null);
		Close();
		return val;
	}
}
