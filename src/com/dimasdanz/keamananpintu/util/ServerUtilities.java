package com.dimasdanz.keamananpintu.util;

import android.content.Context;

public class ServerUtilities {

	public static String getLoginUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_login";
	}
	
	public static String getAdminLoginUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_login_admin";
	}
	
	public static String getRegisterDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_register_device";
	}
	
	public static String deviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_status";
	}

	public static String changeDeviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_status";
	}

	public static String changeDeviceAttemptsUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_change_attempts";
	}

	public static String unlockDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_unlock";
	}

	public static String getLogDateUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_log_date";
	}

	public static String getLogDetailUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_detail_log";
	}

	public static String getUserListUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_get_users/";
	}

	public static String getInsertUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_insert_user";
	}

	public static String getUpdateUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_update_user";
	}

	public static String getDeleteUserUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/dcs/dcs_delete_user";
	}

}
