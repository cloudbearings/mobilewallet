// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.mobilewallet.utils;

import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;

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
}