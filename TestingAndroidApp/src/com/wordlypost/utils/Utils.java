package com.wordlypost.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.wordlypost.gcm.Config;

public class Utils {

	public static void displayToad(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	public static Typeface getFont(Context context, String fontType) {
		Typeface typeface = null;
		try {
			typeface = Typeface.createFromAsset(context.getAssets(), fontType);
		} catch (Exception e) {
		}
		return typeface;
	}

	public static boolean isNetworkAvailable(Context context) {

		NetworkInfo activeNetwork = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public static Dialog alertDailog(Context context, int layout) {
		Dialog dialog = null;
		try {
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(true);
			dialog.setContentView(layout);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	public static String changeDateFormat(String oldDateString) {

		String OLD_FORMAT = "yyyy-MM-dd HH:mm:ss";
		String NEW_FORMAT = "MMM dd, yyyy hh:mm aaa";

		// August 12, 2010
		String newDateString = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT, Locale.ENGLISH);
			Date d = sdf.parse(oldDateString.trim());

			sdf.applyPattern(NEW_FORMAT);
			newDateString = sdf.format(d);
		} catch (ParseException e) {
			Log.i("TAG :", "Date parse exception");
			e.printStackTrace();
		}
		return newDateString;
	}

	public static String getGcmId(Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(Config.GCM_ID,
				Context.MODE_PRIVATE);
		String registrationId = prefs.getString(Config.GCM_ID, "");
		if (registrationId == null || "".equals(registrationId.trim())) {
			return null;
		}
		int registeredVersion = prefs.getInt(Config.APP_VERSION, Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return null;
		}
		return registrationId;
	}

	public static void storeGcmId(String gcmId, Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(Config.GCM_ID,
				Context.MODE_PRIVATE);
		int appVersion = Utils.getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Config.GCM_ID, gcmId);
		editor.putInt(Config.APP_VERSION, appVersion);
		editor.commit();
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static void storeAddClosedDate(Context context) {
		SharedPreferences.Editor editor = (context.getSharedPreferences(Config.ADD_LOADED_DATE,
				Context.MODE_PRIVATE)).edit();
		editor.putLong(Config.ADD_LOADED_DATE, new Date().getTime());
		editor.commit();
	}

	private static long getAddClosedDate(Context context) {
		return (context.getSharedPreferences(Config.ADD_LOADED_DATE, Context.MODE_PRIVATE))
				.getLong(Config.ADD_LOADED_DATE, 0);
	}

	public static boolean openAd(Context context) {
		boolean call = false;
		try {
			long closedTime = getAddClosedDate(context);

			if (closedTime != 0) {
				long diff = (new Date().getTime()) - closedTime;
				call = (diff / 1000) % 60 >= 5;
				Log.i("Seconds Diff :", (diff / 1000) % 60 + "");
			} else {
				call = true;
			}
		} catch (Exception e) {

		}
		return call;
	}
}
