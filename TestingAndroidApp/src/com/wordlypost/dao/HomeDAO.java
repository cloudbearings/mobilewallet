package com.wordlypost.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.wordlypost.beans.NavDrawerItem;
import com.wordlypost.beans.PostRowItem;
import com.wordlypost.persistance.DbAdapter;

public class HomeDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public HomeDAO(Context context) {
		this.context = context;
	}

	public HomeDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertPosts(int post_id, String post_title, String post_date, String post_icon_url,
			String post_author_name, String post_content, String post_screen_image_url,
			int post_comment_count, String post_url, String current_milliseconds, String excerpt,
			int categotyId, String categorySlug, String categoryTitle, String comments, String tags) {
		ContentValues cv = new ContentValues(16);
		cv.put(DbAdapter.H_ID, post_id);
		cv.put(DbAdapter.H_TITLE, post_title);
		cv.put(DbAdapter.H_DATE, post_date);
		cv.put(DbAdapter.H_ICON_URL, post_icon_url);
		cv.put(DbAdapter.H_AUTHOR_NAME, post_author_name);
		cv.put(DbAdapter.H_CONTENT, post_content);
		cv.put(DbAdapter.H_SCREEN_IMAGE_URL, post_screen_image_url);
		cv.put(DbAdapter.H_COMMENT_COUNT, post_comment_count);
		cv.put(DbAdapter.H_URL, post_url);
		cv.put(DbAdapter.H_CURRENT_MILLISECONDS, current_milliseconds);
		cv.put(DbAdapter.H_EXCERPT, excerpt);
		cv.put(DbAdapter.HC_ID, categotyId);
		cv.put(DbAdapter.HC_SLUG, categorySlug);
		cv.put(DbAdapter.HC_TITLE, categoryTitle);
		cv.put(DbAdapter.H_COMMENTS, comments);
		cv.put(DbAdapter.H_TAGS, tags);

		opnToWrite();
		long val = 0;
		if (isPostExists(post_id) > 0) {
			val = database.update(DbAdapter.HOME_POSTS_TABLE_NAME, cv, DbAdapter.H_ID + "="
					+ post_id, null);
			Close();
		} else {
			val = database.insert(DbAdapter.HOME_POSTS_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long isPostExists(int post_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + DbAdapter.HOME_POSTS_TABLE_NAME + " where "
					+ DbAdapter.H_ID + "=" + post_id;

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

	public long getPostsCount(int categoryId, String categorySlug) {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.HC_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.HOME_POSTS_TABLE_NAME, cols, DbAdapter.HC_ID + "="
					+ categoryId + " and " + DbAdapter.HC_SLUG + "='" + categorySlug + "'", null,
					null, null, null);
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

	public long getTotlaPostsCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.HC_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.HOME_POSTS_TABLE_NAME, cols, null, null, null, null, null);
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

	public ArrayList<PostRowItem> getPosts(int categoryId, String categorySlug) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		ArrayList<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.H_ID, DbAdapter.H_TITLE, DbAdapter.H_DATE,
					DbAdapter.H_ICON_URL, DbAdapter.H_AUTHOR_NAME, DbAdapter.H_CONTENT,
					DbAdapter.H_SCREEN_IMAGE_URL, DbAdapter.H_COMMENT_COUNT, DbAdapter.H_URL,
					DbAdapter.H_COMMENTS, DbAdapter.H_TAGS, DbAdapter.H_EXCERPT };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.HOME_POSTS_TABLE_NAME, cols, DbAdapter.HC_ID + "="
					+ categoryId + " and " + DbAdapter.HC_SLUG + "='" + categorySlug + "'", null,
					null, null, DbAdapter.H_DATE + " ASC");
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor.getColumnIndex(DbAdapter.H_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.H_TITLE)));
					item.setDate(cursor.getString(cursor.getColumnIndex(DbAdapter.H_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_ICON_URL)));
					item.setAuthor(cursor.getString(cursor.getColumnIndex(DbAdapter.H_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor.getColumnIndex(DbAdapter.H_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.H_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor.getColumnIndex(DbAdapter.H_URL)));
					item.setCommentsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_COMMENTS)));
					item.setTagsArray(cursor.getString(cursor.getColumnIndex(DbAdapter.H_TAGS)));
					item.setPost_des(cursor.getString(cursor.getColumnIndex(DbAdapter.H_EXCERPT)));

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

	public ArrayList<PostRowItem> getfivePosts(int categoryId, String categorySlug) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		ArrayList<PostRowItem> postsList = null;
		try {
			postsList = new ArrayList<PostRowItem>();
			String[] cols = { DbAdapter.H_ID, DbAdapter.H_TITLE, DbAdapter.H_DATE,
					DbAdapter.H_ICON_URL, DbAdapter.H_AUTHOR_NAME, DbAdapter.H_CONTENT,
					DbAdapter.H_SCREEN_IMAGE_URL, DbAdapter.H_COMMENT_COUNT, DbAdapter.H_URL,
					DbAdapter.H_COMMENTS, DbAdapter.H_TAGS, DbAdapter.H_EXCERPT };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.HOME_POSTS_TABLE_NAME, cols, DbAdapter.HC_ID + "="
					+ categoryId + " and " + DbAdapter.HC_SLUG + "='" + categorySlug + "'", null,
					null, null, DbAdapter.H_DATE + " ASC", "5");
			PostRowItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new PostRowItem();

					item.setPost_id(cursor.getInt(cursor.getColumnIndex(DbAdapter.H_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.H_TITLE)));
					item.setDate(cursor.getString(cursor.getColumnIndex(DbAdapter.H_DATE)));
					item.setPost_icon_url(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_ICON_URL)));
					item.setAuthor(cursor.getString(cursor.getColumnIndex(DbAdapter.H_AUTHOR_NAME)));
					item.setContent(cursor.getString(cursor.getColumnIndex(DbAdapter.H_CONTENT)));
					item.setPost_banner(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_SCREEN_IMAGE_URL)));
					item.setComment_count(cursor.getInt(cursor
							.getColumnIndex(DbAdapter.H_COMMENT_COUNT)));
					item.setPost_url(cursor.getString(cursor.getColumnIndex(DbAdapter.H_URL)));
					item.setCommentsArray(cursor.getString(cursor
							.getColumnIndex(DbAdapter.H_COMMENTS)));
					item.setTagsArray(cursor.getString(cursor.getColumnIndex(DbAdapter.H_TAGS)));
					item.setPost_des(cursor.getString(cursor.getColumnIndex(DbAdapter.H_EXCERPT)));

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

	public long deletePosts(int categoryId, String categorySlug, String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DbAdapter.HOME_POSTS_TABLE_NAME,
				DbAdapter.H_CURRENT_MILLISECONDS + "!='" + currentMilliseconds + "' and "
						+ DbAdapter.HC_ID + "=" + categoryId + " and " + DbAdapter.HC_SLUG + "='"
						+ categorySlug + "'", null);
		Close();
		return val;
	}

	public List<NavDrawerItem> getRandomCategories() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<NavDrawerItem> categoriesList = null;
		try {
			categoriesList = new ArrayList<NavDrawerItem>();
			String[] cols = { DbAdapter.HC_ID, DbAdapter.HC_SLUG, DbAdapter.HC_TITLE };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(true, DbAdapter.HOME_POSTS_TABLE_NAME, cols, null, null, null,
					null, "RANDOM()", "5");
			NavDrawerItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new NavDrawerItem();
					item.setId(cursor.getInt(cursor.getColumnIndex(DbAdapter.HC_ID)));
					item.setSlug(cursor.getString(cursor.getColumnIndex(DbAdapter.HC_SLUG)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.HC_TITLE)));
					categoriesList.add(item);
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
		return categoriesList;
	}
}
