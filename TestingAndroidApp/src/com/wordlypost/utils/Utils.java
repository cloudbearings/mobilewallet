package com.wordlypost.utils;

import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

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

}
