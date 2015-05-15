package com.funnyzone.dao;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.funnyzone.beans.PostRowItem;
import com.funnyzone.persistance.DbAdapter;

public class TagPostsDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public TagPostsDAO(Context context) {
		this.context = context;
	}

	public TagPostsDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertTagPosts(int post_id, String post_title,
			String post_date, String post_icon_url, String post_author_name,
			String post_content, String post_screen_image_url,
			int post_comment_count, String post_url,
			String current_milliseconds, String excerpt, String comments,
			String tags) {
		ContentValues cv = new ContentValues(13);
		cv.put(DbAdapter.TP_ID, post_id);
		cv.put(DbAdapter.TP_TITLE, post_title);
		cv.put(DbAdapter.TP_DATE, post_date);
		cv.put(DbAdapter.TP_ICON_URL, post_icon_url);
		cv.put(DbAdapter.TP_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.TP_CONTENT, post_content);
		cv.put(DbAdapter.TP_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.TP_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.TP_URL, post_url);
		cv.put(DbAdapter.TP_CURRENT_MILLISECONDS, current_milliseconds);
		cv.put(DbAdapter.TP_EXCERPT, excerpt);
		cv.put(DbAdapter.TP_COMMENTS, comments);
		cv.put(DbAdapter.TP_TAGS, tags);

		opnToWrite();
		long val = 0;
		if (isTagPostExists(post_id) > 0) {
			val = database.update(DbAdapter.TAG_POSTS_TABLE_NAME, cv,
					DbAdapter.TP_ID + "=" + post_id, null);
			Close();
		} else {
			val = database.insert(DbAdapter.TAG_POSTS_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long isTagPostExists(int post_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM "
					+ DbAdapter.TAG_POSTS_TABLE_NAME + " where "
					+ DbAdapter.TP_ID + "=" + post_id;

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

	public long getTagPostsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.TP_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.TAG_POSTS_TABLE_NAME, cols, null,
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

	public ArrayList<PostRowItem> getTagPosts() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		ArrayList<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.TP_ID, DbAdapter.TP_TITLE,
					DbAdapter.TP_DATE, DbAdapter.TP_ICON_URL,
					DbAdapter.TP_AUTHOR_NAME, DbAdapter.TP_CONTENT,
					DbAdapter.TP_SCREEN_IMAGE_URL, DbAdapter.TP_COMMENT_COUNT,
					DbAdapter.TP_URL, DbAdapter.TP_EXCERPT,
					DbAdapter.TP_COMMENTS, DbAdapter.TP_TAGS };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.TAG_POSTS_TABLE_NAME, cols, null,
					null, null, null, DbAdapter.TP_DATE + " DESC");
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.TP_ID)));
					item.setTitle(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_TITLE)));
					item.setDate(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_ICON_URL)));
					item.setAuthor(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.TP_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_URL)));
					item.setPost_des(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_EXCERPT)));
					item.setCommentsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_COMMENTS)));
					item.setTagsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.TP_TAGS)));

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

	public long deleteTagPosts(String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DbAdapter.TAG_POSTS_TABLE_NAME,
				DbAdapter.TP_CURRENT_MILLISECONDS + "!='" + currentMilliseconds
						+ "'", null);
		Close();
		return val;
	}
}
