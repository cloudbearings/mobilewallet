package com.funnyzone.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.funnyzone.beans.PostRowItem;
import com.funnyzone.persistance.DbAdapter;

public class SearchPostsDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public SearchPostsDAO(Context context) {
		this.context = context;
	}

	public SearchPostsDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertSearchPosts(int post_id, String post_title,
			String post_date, String post_icon_url, String post_author_name,
			String post_content, String post_screen_image_url,
			int post_comment_count, String post_url,
			String current_milliseconds, String excerpt, String comments,
			String tags) {
		ContentValues cv = new ContentValues(13);
		cv.put(DbAdapter.SP_ID, post_id);
		cv.put(DbAdapter.SP_TITLE, post_title);
		cv.put(DbAdapter.SP_DATE, post_date);
		cv.put(DbAdapter.SP_ICON_URL, post_icon_url);
		cv.put(DbAdapter.SP_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.SP_CONTENT, post_content);
		cv.put(DbAdapter.SP_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.SP_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.SP_URL, post_url);
		cv.put(DbAdapter.SP_CURRENT_MILLISECONDS, current_milliseconds);
		cv.put(DbAdapter.SP_EXCERPT, excerpt);
		cv.put(DbAdapter.SP_COMMENTS, comments);
		cv.put(DbAdapter.SP_TAGS, tags);

		opnToWrite();
		long val = 0;
		if (isSearchPostExists(post_id) > 0) {
			val = database.update(DbAdapter.SEARCH_POSTS_TABLE_NAME, cv,
					DbAdapter.SP_ID + "=" + post_id, null);
			Close();
		} else {
			val = database.insert(DbAdapter.SEARCH_POSTS_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long isSearchPostExists(int post_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ DbAdapter.SEARCH_POSTS_TABLE_NAME + " where "
					+ DbAdapter.SP_ID + "=" + post_id;

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

	public long getSearchPostsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.SP_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.SEARCH_POSTS_TABLE_NAME, cols, null,
					null, null, null, null);
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

	public ArrayList<PostRowItem> getSearchPosts() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		ArrayList<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.SP_ID, DbAdapter.SP_TITLE,
					DbAdapter.SP_DATE, DbAdapter.SP_ICON_URL,
					DbAdapter.SP_AUTHOR_NAME, DbAdapter.SP_CONTENT,
					DbAdapter.SP_SCREEN_IMAGE_URL, DbAdapter.SP_COMMENT_COUNT,
					DbAdapter.SP_URL, DbAdapter.SP_EXCERPT,
					DbAdapter.SP_COMMENTS, DbAdapter.SP_TAGS };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.SEARCH_POSTS_TABLE_NAME, cols, null,
					null, null, null, DbAdapter.SP_DATE + " DESC");
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.SP_ID)));
					item.setTitle(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_TITLE)));
					item.setDate(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_ICON_URL)));
					item.setAuthor(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.SP_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_URL)));
					item.setPost_des(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_EXCERPT)));
					item.setCommentsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_COMMENTS)));
					item.setTagsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.SP_TAGS)));

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

	public long deleteSearchPosts(String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DbAdapter.SEARCH_POSTS_TABLE_NAME,
				DbAdapter.SP_CURRENT_MILLISECONDS + "!='" + currentMilliseconds
						+ "'", null);
		Close();
		return val;
	}
}
