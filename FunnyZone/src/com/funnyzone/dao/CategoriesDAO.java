package com.funnyzone.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.funnyzone.beans.NavDrawerItem;
import com.funnyzone.persistance.DbAdapter;

public class CategoriesDAO {

	private SQLiteDatabase database;
	private DbAdapter dbHelper;
	private Context context;

	public CategoriesDAO(Context context) {
		this.context = context;
	}

	public CategoriesDAO opnToWrite() {
		dbHelper = new DbAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertCategories(int category_id, String category_name, String category_slug,
			int category_post_count, String isHomeCategory) {
		ContentValues cv = new ContentValues(4);
		cv.put(DbAdapter.C_ID, category_id);
		cv.put(DbAdapter.C_NAME, category_name);
		cv.put(DbAdapter.C_SLUG, category_slug);
		cv.put(DbAdapter.C_POST_COUNT, category_post_count);
		cv.put(DbAdapter.IS_HOME_CATEGORY, isHomeCategory);

		opnToWrite();
		long val = 0;
		if (isCategoryExists(category_id) == 0) {
			val = database.insert(DbAdapter.CATEGORIES_TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long isCategoryExists(int category_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + DbAdapter.CATEGORIES_TABLE_NAME + " where "
					+ DbAdapter.C_ID + "=" + category_id;

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

	public long getCategoriesCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.C_ID };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.CATEGORIES_TABLE_NAME, cols, null, null, null, null, null);
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

	public List<NavDrawerItem> getCategories() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<NavDrawerItem> categoriesList = null;
		try {
			categoriesList = new ArrayList<NavDrawerItem>();
			String[] cols = { DbAdapter.C_ID, DbAdapter.C_NAME, DbAdapter.C_SLUG,
					DbAdapter.C_POST_COUNT, DbAdapter.IS_HOME_CATEGORY };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.CATEGORIES_TABLE_NAME, cols, null, null, null, null,
					DbAdapter.C_NAME + " ASC");
			NavDrawerItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new NavDrawerItem();

					item.setId(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.C_NAME)));
					item.setSlug(cursor.getString(cursor.getColumnIndex(DbAdapter.C_SLUG)));
					item.setPost_count(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_POST_COUNT)));
					item.setIsHomeCategory(cursor.getString(cursor
							.getColumnIndex(DbAdapter.IS_HOME_CATEGORY)));

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

	public List<NavDrawerItem> getRandomCategories() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<NavDrawerItem> categoriesList = null;
		try {
			categoriesList = new ArrayList<NavDrawerItem>();
			String[] cols = { DbAdapter.C_ID, DbAdapter.C_NAME, DbAdapter.C_SLUG,
					DbAdapter.C_POST_COUNT, DbAdapter.IS_HOME_CATEGORY };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.CATEGORIES_TABLE_NAME, cols,
					DbAdapter.IS_HOME_CATEGORY + "='Y'", null, null, null, null);
			NavDrawerItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new NavDrawerItem();

					item.setId(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.C_NAME)));
					item.setSlug(cursor.getString(cursor.getColumnIndex(DbAdapter.C_SLUG)));
					item.setPost_count(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_POST_COUNT)));
					item.setIsHomeCategory(cursor.getString(cursor
							.getColumnIndex(DbAdapter.IS_HOME_CATEGORY)));

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

	public long updateIsHomeCategory(int category_id, String isHomeCategory) {
		ContentValues cv = new ContentValues(4);
		cv.put(DbAdapter.C_ID, category_id);
		cv.put(DbAdapter.IS_HOME_CATEGORY, isHomeCategory);

		opnToWrite();
		long val = 0;
		val = database.update(DbAdapter.CATEGORIES_TABLE_NAME, cv, DbAdapter.C_ID + "="
				+ category_id, null);
		Close();
		return val;
	}

	public long getHomeCategoriesCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DbAdapter.C_ID, DbAdapter.IS_HOME_CATEGORY };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DbAdapter.CATEGORIES_TABLE_NAME, cols, DbAdapter.IS_HOME_CATEGORY
					+ "='Y'", null, null, null, null);
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

	public List<NavDrawerItem> getHomeCategories() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<NavDrawerItem> categoriesList = null;
		try {
			categoriesList = new ArrayList<NavDrawerItem>();
			String[] cols = { DbAdapter.C_ID, DbAdapter.C_NAME, DbAdapter.C_SLUG,
					DbAdapter.C_POST_COUNT, DbAdapter.IS_HOME_CATEGORY };

			dbHelper = new DbAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DbAdapter.CATEGORIES_TABLE_NAME, cols,
					DbAdapter.IS_HOME_CATEGORY + "='Y'", null, null, null, DbAdapter.C_NAME
							+ " DESC");
			NavDrawerItem item;
			if (cursor.moveToFirst()) {
				do {
					item = new NavDrawerItem();

					item.setId(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_ID)));
					item.setTitle(cursor.getString(cursor.getColumnIndex(DbAdapter.C_NAME)));
					item.setSlug(cursor.getString(cursor.getColumnIndex(DbAdapter.C_SLUG)));
					item.setPost_count(cursor.getInt(cursor.getColumnIndex(DbAdapter.C_POST_COUNT)));
					item.setIsHomeCategory(cursor.getString(cursor
							.getColumnIndex(DbAdapter.IS_HOME_CATEGORY)));

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
