package com.margaret.parking.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by varmu02 on 7/5/2015.
 */
public class PreferenceStore {
    private static final String COMPLAINTS_KEY = "complaints_downloaded";
    private static final String CUST_COMPLAINTS_KEY = "cust_complaints_downloaded";
    private static final String IS_LOGGED_IN = "isloggedin";
    private static final String OPERATOR_LOGGED_IN = "operator";
    
    public static void setComplaintsDownloadStatus(Context context, boolean status){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean(COMPLAINTS_KEY, status);
        editor.commit();
    }
    
    public static boolean saveLoggedIn(Context context, boolean status){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean(IS_LOGGED_IN, status);
        editor.commit();
        return false;
        
    }
    
    public static boolean getLoggedInStatus(Context context){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(IS_LOGGED_IN, false);
    }
    
    public static void saveOperatorLogged(Context context, boolean isOperator){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean(OPERATOR_LOGGED_IN, isOperator);
        editor.commit();
    }
    
    public static boolean getOperatorLogged(Context context){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(OPERATOR_LOGGED_IN, false);
    }

    public static boolean getComplaintsDownloadStatus(Context context){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(COMPLAINTS_KEY, false);
    }
    
    /**Customer specific methods**/
    public static void setCustComplaintsDownloadStatus(Context context, boolean status){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shp.edit();
        editor.putBoolean(CUST_COMPLAINTS_KEY, status);
        editor.commit();
    }

    public static boolean getCustComplaintsDownloadStatus(Context context){
        SharedPreferences shp = PreferenceManager.getDefaultSharedPreferences(context);
        return shp.getBoolean(CUST_COMPLAINTS_KEY, false);
    }
}
