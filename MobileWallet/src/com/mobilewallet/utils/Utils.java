// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mobilewallet.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.mobilewallet.gcm.Config;

public class Utils {

	public Utils() {
	}

	public static String formatAmount(String s) {
		String s1;
		try {
			Locale locale = Locale.ENGLISH;
			Object aobj[] = new Object[1];
			aobj[0] = Double.valueOf(Double.parseDouble(s));
			s1 = String.format(locale, "%.2f", aobj);
		} catch (Exception exception) {
			exception.printStackTrace();
			return s;
		}
		return s1;
	}

	public static String getAmount(Context context) {
		String s = context.getSharedPreferences("amount", 0).getString("amount", null);
		if (s == null || "".equals(s.trim())) {
			s = "0.0";
		}
		return formatAmount(s);
	}

	public static String getDataFromPref(Context context, String s) {
		return context.getSharedPreferences(s, 0).getString(s, null);
	}

	public static Typeface getFont(Context context, String s) {
		Typeface typeface;
		try {
			typeface = Typeface.createFromAsset(context.getAssets(), s);
		} catch (Exception exception) {
			return null;
		}
		return typeface;
	}

	public static String getUserId(Context context) {
		return context.getSharedPreferences("userId", 0).getString("userId", null);
	}

	public static void storeAmount(String s, Context context) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("amount", 0)
				.edit();
		editor.putString("amount", s);
		editor.commit();
	}

	public static void storeDataInPref(Context context, String s, String s1) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences(s, 0).edit();
		editor.putString(s, s1);
		editor.commit();
	}

	public static void storeUserId(String s, Context context) {
		android.content.SharedPreferences.Editor editor = context.getSharedPreferences("userId", 0)
				.edit();
		editor.putString("userId", s);
		editor.commit();
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

	// Method to check internet connection
	public static boolean isNetworkAvailable(Context context) {

		NetworkInfo activeNetwork = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
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

	public static String getDeviceId(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE))
				.getDeviceId();
	}

	public static String getAndroidId(Context context) {
		return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String readVerificationCode(Context context, long time) {
		String verCode = null;
		Cursor cur = null;
		try {
			cur = context.getContentResolver().query(Uri.parse("content://sms/inbox"),
					new String[] { "body" }, "address like '%-FREEPS%' and date >=" + time, null,
					"date desc limit 1");
			if (cur.moveToNext() && cur.getCount() == 1) {
				String body = cur.getString(0);

				if (body != null && !"".equals(body.trim())) {

					if (body.trim().contains("FreePlus verification code ")) {
						verCode = body.split(" ")[3].trim();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cur != null)
				cur.close();
		}
		return verCode;
	}
}
