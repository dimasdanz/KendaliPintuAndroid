package com.dimasdanz.keamananpintu.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	private static final String prefs_name = "KeamananPintuPrefs";
	private static final String property_hostname = "hostname";
	private static final String property_receiveNotifications = "notifications";
	private static final String property_isLoggedIn = "logged_in";
	private static final String property_usernameId = "username_id";
	private static final String property_appVersion = "appVersion";
	private static final String property_regId = "registration_id";
	
	private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
    }

    public static void setHostnamePrefs(Context context, String string) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(property_hostname, string);
        editor.commit();
    }
    
    public static String getHostnamePrefs(Context context) {
	    return "http://"+getSharedPreferences(context).getString(property_hostname , "192.168.2.4");
	}

	public static void setNotificationPrefs(Context context, Boolean b) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(property_receiveNotifications, b);
        editor.commit();
    }

	public static Boolean getNotificationPrefs(Context context) {
	    return getSharedPreferences(context).getBoolean(property_receiveNotifications, true);
	}
	
	public static void setLoggedIn(Context context, Boolean b, String s){
		final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(property_isLoggedIn, b);
		editor.putString(property_usernameId, s);
		editor.commit();
	}
	
	public static Boolean getLoggedInPrefs(Context context){
		return getSharedPreferences(context).getBoolean(property_isLoggedIn, false);
	}
	
	public static int getUsernameIdPrefs(Context context){
		return getSharedPreferences(context).getInt(property_usernameId, 0);
	}
	
	public static void setRegId(Context context, String regId) {
		final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        int appVersion = CommonUtilities.getAppVersion(context);
        editor.putString(property_regId, regId);
        editor.putInt(property_appVersion, appVersion);
        editor.commit();
    }
	
	public static String getRegId(Context context) {
		final SharedPreferences prefs = getSharedPreferences(context);
        String registrationId = prefs.getString(property_regId, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt(property_appVersion, Integer.MIN_VALUE);
        int currentVersion = CommonUtilities.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }
}
