package com.dimasdanz.keamananpintu.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {
	public static final String prefs_name = "KeamananPintuPrefs";
	public static final String hostname = "hostname";
	public static final String receive_notifications = "notifications";
	public static final String isLoggedIn = "logged_in";
	public static final String username_id = "username_id";
	
	private SharedPreferencesManager() {}

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(prefs_name, Context.MODE_PRIVATE);
    }

    public static void setHostnamePrefs(Context context, String string) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(hostname, string);
        editor.commit();
    }
    
    public static String getHostnamePrefs(Context context) {
	    return "http://"+getSharedPreferences(context).getString(hostname , "192.168.2.4");
	}

	public static void setNotificationPrefs(Context context, Boolean b) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(receive_notifications, b);
        editor.commit();
    }

	public static Boolean getNotificationPrefs(Context context) {
	    return getSharedPreferences(context).getBoolean(receive_notifications, true);
	}
	
	public static void setLoggedIn(Context context, Boolean b, String s){
		final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(isLoggedIn, b);
		editor.putString(username_id, s);
		editor.commit();
	}
	
	public static Boolean getLoggedInPrefs(Context context){
		return getSharedPreferences(context).getBoolean(isLoggedIn, false);
	}
	
	public static int getUsernameIdPrefs(Context context){
		return getSharedPreferences(context).getInt(username_id, 0);
	}
}
