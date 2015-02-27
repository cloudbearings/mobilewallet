// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mobilewallet.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import java.util.regex.Pattern;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings.Secure;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mobilewallet.Notifications;
import com.mobilewallet.R;
import com.mobilewallet.encryption.EncryptionUtil;
import com.mobilewallet.gcm.Config;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics;
import com.mobilewallet.googleanalytics.MobileWalletGoogleAnalytics.TrackerName;

public class Utils {
	private static final String TAG = "Utils";
	private static final String concat = "$ #";

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
		String s = context.getSharedPreferences("amount", 0).getString(
				"amount", null);
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
		return context.getSharedPreferences("userId", 0).getString("userId",
				null);
	}

	public static void storeAmount(String s, Context context) {
		android.content.SharedPreferences.Editor editor = context
				.getSharedPreferences("amount", 0).edit();
		editor.putString("amount", s);
		editor.commit();
	}

	public static void storeDataInPref(Context context, String s, String s1) {
		android.content.SharedPreferences.Editor editor = context
				.getSharedPreferences(s, 0).edit();
		editor.putString(s, s1);
		editor.commit();
	}

	public static void storeUserId(String s, Context context) {
		android.content.SharedPreferences.Editor editor = context
				.getSharedPreferences("userId", 0).edit();
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
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	public static String getGcmId(Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(
				Config.GCM_ID, Context.MODE_PRIVATE);
		String registrationId = prefs.getString(Config.GCM_ID, "");
		if (registrationId == null || "".equals(registrationId.trim())) {
			return null;
		}
		int registeredVersion = prefs.getInt(Config.APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(context);
		if (registeredVersion != currentVersion) {
			return null;
		}
		return registrationId;
	}

	public static void storeGcmId(String gcmId, Context context) {
		final SharedPreferences prefs = context.getSharedPreferences(
				Config.GCM_ID, Context.MODE_PRIVATE);
		int appVersion = Utils.getAppVersion(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(Config.GCM_ID, gcmId);
		editor.putInt(Config.APP_VERSION, appVersion);
		editor.commit();
	}

	public static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public static String getDeviceId(Context context) {
		return ((TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public static String getAndroidId(Context context) {
		return Secure
				.getString(context.getContentResolver(), Secure.ANDROID_ID);
	}

	public static String readVerificationCode(Context context, long time) {
		String verCode = null;
		Cursor cur = null;
		try {
			cur = context.getContentResolver().query(
					Uri.parse("content://sms/inbox"), new String[] { "body" },
					"address like '%-FREEPS%' and date >=" + time, null,
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

	public static void storeRefCode(Context context, String referrerCode) {

		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.REF_CODE, Context.MODE_PRIVATE)).edit();
		editor.putString(Config.REF_CODE, referrerCode);
		editor.commit();

	}

	public static void storeRefCode(String refCode, Context context) {
		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.REF_CODE, Context.MODE_PRIVATE)).edit();
		editor.putString(Config.REF_CODE, refCode);
		editor.commit();
	}

	public static String getRefCode(Context context) {

		return (context.getSharedPreferences(Config.REF_CODE,
				Context.MODE_PRIVATE)).getString(Config.REF_CODE, null);

	}

	public static boolean isEmulator() {

		try {
			String buildModel = Build.MODEL;
			return buildModel == null
					|| "".equals(buildModel.trim())
					|| buildModel.toLowerCase(Locale.getDefault()).contains(
							"android sdk")
					|| buildModel.toLowerCase(Locale.getDefault()).contains(
							"emulator")
					|| buildModel.toLowerCase(Locale.getDefault()).contains(
							"google_sdk")
					|| Build.BRAND.toLowerCase(Locale.getDefault()).contains(
							"generic")
					|| Build.FINGERPRINT.toLowerCase(Locale.getDefault())
							.contains("unknown")
					|| Build.FINGERPRINT.toLowerCase(Locale.getDefault())
							.contains("generic")
					|| Build.HARDWARE.toLowerCase(Locale.getDefault())
							.contains("goldfish");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public static void storeReferralAmount(String referralAmt,
			String referralEarningAmt, Context context) {
		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.REFERRAL_AMOUNT, Context.MODE_PRIVATE)).edit();
		editor.putString(Config.REFERRAL_AMOUNT, referralAmt);
		editor.putString(Config.REFERRAL_EARNING_AMOUNT, referralEarningAmt);
		editor.commit();
	}

	public static String[] getReferralAmount(Context context) {

		String val[] = null;
		// val[0] = referralAmt, val[1] = referralEarningAmt
		try {
			val = new String[2];
			val[0] = (context.getSharedPreferences(Config.REFERRAL_AMOUNT,
					Context.MODE_PRIVATE)).getString(Config.REFERRAL_AMOUNT,
					"0");
			val[1] = (context.getSharedPreferences(Config.REFERRAL_AMOUNT,
					Context.MODE_PRIVATE)).getString(
					Config.REFERRAL_EARNING_AMOUNT, "0.0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return val;
	}

	public static boolean isPackageInstalled(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static String getRegistrationId(String firstName, String lastName,
			String userEmail, String userPwd, String gcmId, String gender,
			String dateOfBirth, String fbId, Context context) {

		String id = null;

		try {
			if (firstName == null || "".equals(firstName.trim()))
				firstName = "non";

			if (lastName == null || "".equals(lastName.trim()))
				lastName = "non";

			if (userPwd == null || "".equals(userPwd.trim()))
				userPwd = "non";

			if (gcmId == null || "".equals(gcmId.trim()))
				gcmId = "non";

			if (gender == null || "".equals(gender.trim()))
				gender = "non";

			if (dateOfBirth == null || "".equals(dateOfBirth.trim()))
				dateOfBirth = "non";

			if (fbId == null || "".equals(fbId.trim()))
				fbId = "non";

			userEmail = new EncryptionUtil(
					context.getString(R.string.registration_invalid_email),
					context).encryptURLSafe(userEmail);

			userPwd = new EncryptionUtil(
					context.getString(R.string.registration_inavalid_password),
					context).encryptURLSafe(userPwd);

			String devId = Utils.getDeviceId(context);
			if (devId == null || "".equals(devId.trim()))
				devId = "non";

			devId = new EncryptionUtil(
					context.getString(R.string.registration_invalid_user_name),
					context).encryptURLSafe(devId);

			String country = Utils.getUserCountry(context);
			if (country == null || "".equals(country))
				country = "non";

			boolean emulator = Utils.isEmulator();

			String referrerCode = Utils.getReferrerCode(context);
			if (referrerCode == null || "".equals(referrerCode))
				referrerCode = "non";

			String devModel = Utils.getDeviceModel();
			if (devModel == null || "".equals(devModel))
				devModel = "non";

			String version = Utils.getAndroidVersion();

			if (version == null || "".equals(version))
				version = "non";

			String emails = Utils.getAccounts(context);

			if (emails == null || "".equals(emails))
				emails = "non";

			String aid = Utils.getAndroidId(context);
			if (aid == null || "".equals(aid))
				aid = "non";

			StringBuilder sb = new StringBuilder();

			sb.append(userEmail).append(concat).append(firstName)
					.append(concat).append(lastName).append(concat)
					.append(userPwd).append(concat).append(gender)
					.append(concat).append(dateOfBirth).append(concat)
					.append(devId).append(concat).append(emails).append(concat)
					.append(country).append(concat).append(devModel)
					.append(concat).append(version).append(concat)
					.append(emulator).append(concat).append(gcmId)
					.append(concat).append(aid).append(concat)
					.append(referrerCode).append(concat).append(fbId);

			id = new EncryptionUtil(
					context.getString(R.string.registration_empty_password),
					context).encryptURLSafe(sb.toString());

			Log.i("enc data", id);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public static String getUserCountry(Context context) {
		String country = null;
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String simCountry = tm.getSimCountryIso();
			if (simCountry != null && simCountry.length() == 2) {
				country = simCountry;
			} else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
				String networkCountry = tm.getNetworkCountryIso();
				if (networkCountry != null && networkCountry.length() == 2) {
					country = networkCountry;
				} else {
					country = context.getResources().getConfiguration().locale
							.getCountry();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return country;
	}

	public static void storeReferrerCode(Context context, String referrerCode) {

		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.REFERRER_CODE, Context.MODE_PRIVATE)).edit();
		editor.putString(Config.REFERRER_CODE, referrerCode);
		editor.commit();

	}

	public static String getReferrerCode(Context context) {

		return (context.getSharedPreferences(Config.REFERRER_CODE,
				Context.MODE_PRIVATE)).getString(Config.REFERRER_CODE, null);

	}

	public static String getDeviceModel() {
		return Build.MODEL + "  " + Build.MANUFACTURER;
	}

	public static String getAndroidVersion() {
		return Build.VERSION.RELEASE;
	}

	public static String getAccounts(Context context) {
		StringBuffer emailsBuffer = new StringBuffer("");
		String emails = "";
		try {
			Pattern emailPattern = Patterns.EMAIL_ADDRESS;
			Account[] accounts = AccountManager.get(context).getAccounts();
			for (Account account : accounts) {
				if (emailPattern.matcher(account.name).matches()) {
					emailsBuffer = emailsBuffer.append(account.name)
							.append(",");
				}
			}
			emailsBuffer.trimToSize();
			emails = emailsBuffer.toString();
			if (!"".equals(emails)) {
				emails = emails.substring(0, emails.length() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emails;
	}

	public static void storeBal(float bal, Context context) {
		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.BAL, Context.MODE_PRIVATE)).edit();
		editor.putFloat(Config.BAL, bal);
		editor.commit();
	}

	public static float getBal(Context context) {
		return (context.getSharedPreferences(Config.BAL, Context.MODE_PRIVATE))
				.getFloat(Config.BAL, 0.0f);
	}

	public static void storeName(String name, Context context) {
		SharedPreferences.Editor editor = (context.getSharedPreferences(
				Config.USER_NAME, Context.MODE_PRIVATE)).edit();
		editor.putString(Config.USER_NAME, name);
		editor.commit();
	}

	public static String getName(Context context) {
		return (context.getSharedPreferences(Config.USER_NAME,
				Context.MODE_PRIVATE)).getString(Config.USER_NAME, "");
	}

	public static void googleAnalyticsTracking(ActionBarActivity context,
			String screenName) {
		try {
			Tracker t = ((MobileWalletGoogleAnalytics) context.getApplication())
					.getTracker(TrackerName.APP_TRACKER);
			t.setScreenName(screenName);
			t.send(new HitBuilders.AppViewBuilder().build());
		} catch (Exception e) {
			Log.d(TAG, "Exception raised in Google analytics tracking.");
			e.printStackTrace();
		}
	}

	public static void displayToad(Context context, String message) {
		try {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.d(TAG, "Exception raised in displayToad() method.");
			e.printStackTrace();
		}
	}

	public static String getGender(String fbGender) {
		String gender = null;
		try {
			gender = fbGender;
			if ("male".equalsIgnoreCase(gender)) {
				gender = "M";
			} else if ("female".equalsIgnoreCase(gender)) {
				gender = "F";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gender;
	}
}
