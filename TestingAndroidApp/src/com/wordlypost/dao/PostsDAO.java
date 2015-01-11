package com.wordlypost.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wordlypost.beans.PostRowItem;
import com.wordlypost.persistance.DbAdapter;

public class PostsDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public PostsDAO(Context context) {
		this.context = context;
	}

	public PostsDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertPosts(int post_id, String post_title, String post_date,
			String post_icon_url, String post_author_name, String post_content,
			String post_screen_image_url, int post_comment_count,
			String post_url) {
		ContentValues cv = new ContentValues(9);
		cv.put(DbAdapter.P_ID, post_id);
		cv.put(DbAdapter.P_TITLE, post_title);
		cv.put(DbAdapter.P_DATE, post_date);
		cv.put(DbAdapter.P_ICON_URL, post_icon_url);
		cv.put(DbAdapter.P_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.P_CONTENT, post_content);
		cv.put(DbAdapter.P_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.P_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.P_URL, post_url);

		opnToWrite();
		long val = database.insert(DbAdapter.CATEGORRY_POSTS_TABLE_NAME, null,
				cv);
		Close();
		return val;
	}

	public long updatePosts(int post_id, String post_title, String post_date,
			String post_icon_url, String post_author_name, String post_content,
			String post_screen_image_url, int post_comment_count,
			String post_url) {
		ContentValues cv = new ContentValues(9);
		cv.put(DbAdapter.P_ID, post_id);
		cv.put(DbAdapter.P_TITLE, post_title);
		cv.put(DbAdapter.P_DATE, post_date);
		cv.put(DbAdapter.P_ICON_URL, post_icon_url);
		cv.put(DbAdapter.P_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.P_CONTENT, post_content);
		cv.put(DbAdapter.P_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.P_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.P_URL, post_url);

		opnToWrite();
		long val = 0;
		if (isPostExists(post_id) > 0) {
			val = database.update(DbAdapter.CATEGORRY_POSTS_TABLE_NAME, cv,
					DbAdapter.P_ID + "='" + post_id + "'", null);
			Close();
		} else {
			val = database.insert(DbAdapter.CATEGORRY_POSTS_TABLE_NAME, null,
					cv);
			Close();
		}

		return val;
	}

	public long isPostExists(int post_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ DbAdapter.CATEGORRY_POSTS_TABLE_NAME + " where "
					+ DbAdapter.P_ID + "=" + post_id;

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

	public long getPostsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.C_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.CATEGORRY_POSTS_TABLE_NAME, cols,
					null, null, null, null, null);
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

	public List<PostRowItem> getPosts() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.P_ID, DbAdapter.P_TITLE,
					DbAdapter.P_DATE, DbAdapter.P_ICON_URL,
					DbAdapter.P_AUTHOR_NAME, DbAdapter.P_CONTENT,
					DbAdapter.P_SCREEN_IMAGE_URL, DbAdapter.P_COMMENT_COUNT,
					DbAdapter.P_URL };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.CATEGORRY_POSTS_TABLE_NAME, cols,
					null, null, null, null, null);
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.P_ID)));
					item.setTitle(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_TITLE)));
					item.setDate(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_ICON_URL)));
					item.setAuthor(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.P_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.P_URL)));

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
}