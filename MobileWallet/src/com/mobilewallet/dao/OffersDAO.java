package com.mobilewallet.dao;

import java.util.ArrayList;
import java.util.List;

import com.mobilewallet.beans.OfferViewModel;
import com.mobilewallet.persistance.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class OffersDAO {
	private SQLiteDatabase database;
	private DBAdapter dbHelper;
	private Context context;

	public OffersDAO(Context context) {
		this.context = context;
	}

	public OffersDAO opnToWrite() {
		dbHelper = new DBAdapter(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void Close() {
		database.close();
	}

	public long insertOffers(String offer_id, String title, String subtitle, String des,
			String steps, String url, String amount, String appPackage, String type, String status,
			String isClicked, String appStatus, String currentMilliseconds, int position,
			int image_id, String app_exists) {
		ContentValues cv = new ContentValues(12);
		cv.put(DBAdapter.OFFER_ID, offer_id);
		cv.put(DBAdapter.TITLE, title);
		cv.put(DBAdapter.SUB_TITLE, subtitle);
		cv.put(DBAdapter.DES, des);
		cv.put(DBAdapter.STEPS, steps);
		cv.put(DBAdapter.URL, url);
		cv.put(DBAdapter.AMOUNT, amount);
		cv.put(DBAdapter.PACKAGE, appPackage);
		cv.put(DBAdapter.TYPE, type);
		cv.put(DBAdapter.STATUS, status);
		cv.put(DBAdapter.CLICKED, isClicked);
		cv.put(DBAdapter.APP_STATUS, appStatus);
		cv.put(DBAdapter.CURRENT_MILLISECONDS, currentMilliseconds);
		cv.put(DBAdapter.POSITION, position);
		cv.put(DBAdapter.IMAGE_ID, image_id);
		cv.put(DBAdapter.APP_EXISTS, app_exists);
		opnToWrite();
		long val = database.insert(DBAdapter.TABLE_NAME, null, cv);
		Close();
		return val;
	}

	public long updateOffers(String offer_id, String title, String subtitle, String des,
			String steps, String url, String amount, String appPackage, String type, String status,
			String isClicked, String appStatus, String currentMilliseconds, int position,
			int image_id, String app_exists) {
		ContentValues cv = new ContentValues(12);
		cv.put(DBAdapter.OFFER_ID, offer_id);
		cv.put(DBAdapter.TITLE, title);
		cv.put(DBAdapter.SUB_TITLE, subtitle);
		cv.put(DBAdapter.DES, des);
		cv.put(DBAdapter.STEPS, steps);
		cv.put(DBAdapter.URL, url);
		cv.put(DBAdapter.AMOUNT, amount);
		cv.put(DBAdapter.PACKAGE, appPackage);
		cv.put(DBAdapter.TYPE, type);
		cv.put(DBAdapter.STATUS, status);
		cv.put(DBAdapter.CLICKED, isClicked);
		cv.put(DBAdapter.APP_STATUS, appStatus);
		cv.put(DBAdapter.CURRENT_MILLISECONDS, currentMilliseconds);
		cv.put(DBAdapter.POSITION, position);
		cv.put(DBAdapter.IMAGE_ID, image_id);
		cv.put(DBAdapter.APP_EXISTS, app_exists);
		opnToWrite();
		long val = 0;
		if (isOfferExists(offer_id) > 0) {
			val = database.update(DBAdapter.TABLE_NAME, cv, DBAdapter.OFFER_ID + "='" + offer_id
					+ "'", null);
			Close();
		} else {
			val = database.insert(DBAdapter.TABLE_NAME, null, cv);
			Close();
		}

		return val;
	}

	public long getOffersCount() {
		SQLiteDatabase database = null;
		Cursor c = null;
		long count = 0;
		try {
			String[] cols = { DBAdapter.OFFER_ID };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			c = database.query(DBAdapter.TABLE_NAME, cols, null, null, null, null, null);
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

	public List<OfferViewModel> getOffers() {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<OfferViewModel> offersList = null;
		try {
			offersList = new ArrayList<OfferViewModel>();
			String[] cols = { DBAdapter.OFFER_ID, DBAdapter.TITLE, DBAdapter.SUB_TITLE,
					DBAdapter.DES, DBAdapter.STEPS, DBAdapter.URL, DBAdapter.AMOUNT,
					DBAdapter.PACKAGE, DBAdapter.TYPE, DBAdapter.STATUS, DBAdapter.APP_STATUS,
					DBAdapter.IMAGE_ID, DBAdapter.APP_EXISTS };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.TABLE_NAME, cols, null, null, null, null,
					DBAdapter.POSITION + " ASC");
			OfferViewModel offer;
			if (cursor.moveToFirst()) {
				do {
					offer = new OfferViewModel();
					offer.setOffer_id(cursor.getString(cursor.getColumnIndex(DBAdapter.OFFER_ID)));
					offer.setTitle(cursor.getString(cursor.getColumnIndex(DBAdapter.TITLE)));
					offer.setSubTitle(cursor.getString(cursor.getColumnIndex(DBAdapter.SUB_TITLE)));
					offer.setDes(cursor.getString(cursor.getColumnIndex(DBAdapter.DES)));
					offer.setAmount(cursor.getString(cursor.getColumnIndex(DBAdapter.AMOUNT)));
					offer.setUrl(cursor.getString(cursor.getColumnIndex(DBAdapter.URL)));
					offer.setSteps(cursor.getString(cursor.getColumnIndex(DBAdapter.STEPS)));
					offer.setStatus(cursor.getString(cursor.getColumnIndex(DBAdapter.STATUS)));
					offer.setAppPackage(cursor.getString(cursor.getColumnIndex(DBAdapter.PACKAGE)));
					offer.setType(cursor.getString(cursor.getColumnIndex(DBAdapter.TYPE)));
					offer.setAppStatus(cursor.getString(cursor.getColumnIndex(DBAdapter.APP_STATUS)));
					offer.setImage_id(cursor.getInt(cursor.getColumnIndex(DBAdapter.IMAGE_ID)));
					offer.setApp_exists(cursor.getString(cursor
							.getColumnIndex(DBAdapter.APP_EXISTS)));

					offersList.add(offer);
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
		return offersList;
	}

	public OfferViewModel getOffer(String offerId) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		OfferViewModel offer = null;
		try {
			String[] cols = { DBAdapter.OFFER_ID, DBAdapter.TITLE, DBAdapter.SUB_TITLE,
					DBAdapter.DES, DBAdapter.STEPS, DBAdapter.URL, DBAdapter.AMOUNT,
					DBAdapter.PACKAGE, DBAdapter.TYPE, DBAdapter.STATUS, DBAdapter.APP_STATUS,
					DBAdapter.IMAGE_ID, DBAdapter.APP_EXISTS };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.TABLE_NAME, cols, DBAdapter.OFFER_ID + "='" + offerId
					+ "'", null, null, null, null);
			if (cursor.moveToNext()) {
				offer = new OfferViewModel();
				offer.setOffer_id(cursor.getString(cursor.getColumnIndex(DBAdapter.OFFER_ID)));
				offer.setTitle(cursor.getString(cursor.getColumnIndex(DBAdapter.TITLE)));
				offer.setSubTitle(cursor.getString(cursor.getColumnIndex(DBAdapter.SUB_TITLE)));
				offer.setDes(cursor.getString(cursor.getColumnIndex(DBAdapter.DES)));
				offer.setAmount(cursor.getString(cursor.getColumnIndex(DBAdapter.AMOUNT)));
				offer.setUrl(cursor.getString(cursor.getColumnIndex(DBAdapter.URL)));
				offer.setSteps(cursor.getString(cursor.getColumnIndex(DBAdapter.STEPS)));
				offer.setStatus(cursor.getString(cursor.getColumnIndex(DBAdapter.STATUS)));
				offer.setAppPackage(cursor.getString(cursor.getColumnIndex(DBAdapter.PACKAGE)));
				offer.setType(cursor.getString(cursor.getColumnIndex(DBAdapter.TYPE)));
				offer.setAppStatus(cursor.getString(cursor.getColumnIndex(DBAdapter.APP_STATUS)));
				offer.setImage_id(cursor.getInt(cursor.getColumnIndex(DBAdapter.IMAGE_ID)));
				offer.setApp_exists(cursor.getString(cursor.getColumnIndex(DBAdapter.APP_EXISTS)));
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
		return offer;
	}

	public String[] getOfferClickedStatus(String offerId) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		String[] s = null;
		try {
			s = new String[3];
			String[] cols = { DBAdapter.CLICKED, DBAdapter.APP_STATUS, DBAdapter.PACKAGE };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.TABLE_NAME, cols, DBAdapter.OFFER_ID + "='" + offerId
					+ "'", null, null, null, null);
			if (cursor.moveToNext()) {
				s[0] = cursor.getString(cursor.getColumnIndex(DBAdapter.CLICKED));
				s[1] = cursor.getString(cursor.getColumnIndex(DBAdapter.APP_STATUS));
				s[2] = cursor.getString(cursor.getColumnIndex(DBAdapter.PACKAGE));
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
		return s;
	}

	public long isOfferExists(String offer_id) {
		SQLiteDatabase database = null;
		long count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM " + DBAdapter.TABLE_NAME + " where "
					+ DBAdapter.OFFER_ID + "='" + offer_id + "'";

			dbHelper = new DBAdapter(context);
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

	public long updatelOfferClickedStauts(String offerId, String clicked) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBAdapter.OFFER_ID, offerId);
		contentValues.put(DBAdapter.CLICKED, clicked);
		opnToWrite();
		long val = database.update(DBAdapter.TABLE_NAME, contentValues, DBAdapter.OFFER_ID + "='"
				+ offerId + "'", null);
		Close();
		return val;
	}

	public long updateAppStauts(String offerId, String status, String appStatus) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(DBAdapter.OFFER_ID, offerId);
		contentValues.put(DBAdapter.STATUS, status);
		contentValues.put(DBAdapter.APP_STATUS, appStatus);
		opnToWrite();
		long val = database.update(DBAdapter.TABLE_NAME, contentValues, DBAdapter.OFFER_ID + "='"
				+ offerId + "'", null);
		Close();
		return val;
	}

	public long deletePausedOffers(String currentMilliseconds) {
		opnToWrite();
		long val = database.delete(DBAdapter.TABLE_NAME, DBAdapter.CURRENT_MILLISECONDS + "!='"
				+ currentMilliseconds + "'", null);
		Close();
		return val;
	}

	public String isOfferClicked(String packageName) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		String s = null;
		try {
			String[] cols = { DBAdapter.CLICKED };

			dbHelper = new DBAdapter(context);
			database = dbHelper.getReadableDatabase();
			cursor = database.query(DBAdapter.TABLE_NAME, cols, DBAdapter.PACKAGE + "='"
					+ packageName + "'", null, null, null, null);
			if (cursor.moveToNext()) {
				s = cursor.getString(cursor.getColumnIndex(DBAdapter.CLICKED));
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
		return s;
	}

}
