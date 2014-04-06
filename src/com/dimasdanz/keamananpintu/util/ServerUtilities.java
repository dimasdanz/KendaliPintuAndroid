package com.dimasdanz.keamananpintu.util;

import android.content.Context;

public class ServerUtilities {

	public static String deviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_status";
	}

	public static String changeDeviceStatus(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_status";
	}

	public static String changeDeviceAttempts(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_attempts";
	}

	public static String unlockDevice(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_unlock";
	}

	public static String getLogDate(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_log_date";
	}

	public static String getLogDetail(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_detail_log";
	}

	public static String getUserList(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_users/";
	}

	public static String getInsertUser(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_insert_user";
	}

	public static String getUpdateUser(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_update_user";
	}

	public static String getDeleteUser(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_delete_user";
	}

}
