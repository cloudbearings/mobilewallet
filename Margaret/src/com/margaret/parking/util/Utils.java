package com.margaret.parking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by varmu02 on 6/17/2015.
 */
public class Utils {
    public static String loadJSONAsStringFromAssets(Context context,
                                                    String jsonName) {
        String json = null;
        try {

            InputStream is = context.getAssets().open(jsonName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    public static String loadJSONAsStringFromServer(Context context,
                                                    String jsonName) {
        String json = "";
        try {
            URL oracle = new URL(jsonName);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                json = inputLine;
            in.close();
            return json;

        } catch (Exception e) {
            e.printStackTrace();
            return json;
        }
    }

    public static String getMyComplaints(Context context) {

        String json = null;
        try {

            InputStream is = context.getAssets().open("my_complaints.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String compressAndEncode(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean isCompressed = bitmap.compress(Bitmap.CompressFormat.JPEG, 100,
                baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getDecodedBitmap(String string) {
        byte[] decodedBytes = Base64.decode(string, Base64.NO_WRAP);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0,
                decodedBytes.length);
        return bitmap;
    }

    public static String parseDateToString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        String newDate = df.format(date);
        return newDate;
    }

    public static boolean isInternetAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void storeDataInPref(Context context, String key, String value) {
        SharedPreferences.Editor editor = (context.getSharedPreferences(key,
                Context.MODE_PRIVATE)).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDataFromPref(Context context, String key) {
        return (context.getSharedPreferences(key, Context.MODE_PRIVATE))
                .getString(key, null);
    }

    public static void removeDataFromPref(Context context, String key) {
        if (getDataFromPref(context, key) != null) {
            SharedPreferences.Editor editor = (context.getSharedPreferences(
                    key, Context.MODE_PRIVATE)).edit();
            editor.remove(key);
            editor.commit();
        }
    }

    public static boolean isNetworkAvailable(Context context) {

        NetworkInfo activeNetwork = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void displayToad(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
