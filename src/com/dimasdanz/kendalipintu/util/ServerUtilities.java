package com.dimasdanz.kendalipintu.util;

import android.content.Context;

public class ServerUtilities {

	public static String getLoginUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/login";
	}
	
	public static String getAdminLoginUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/login_admin";
	}
	
	public static String getRegisterDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/register_device";
	}
	
	public static String getOpenDoorUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/open_door";
	}
	
	public static String getLogDateUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/get_log_date";
	}

	public static String getLogDetailUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/get_log_detail";
	}

	public static String getUserListUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/get_users/";
	}

	public static String getInsertUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/insert_user";
	}

	public static String getUpdateUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/update_user";
	}

	public static String getDeleteUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/delete_user";
	}
	
	/* Deprecated */
	public static String deviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/status";
	}

	public static String changeDeviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/change_status";
	}

	public static String changeDeviceAttemptsUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/change_attempts";
	}

	public static String unlockDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/android/unlock";
	}
}
