package com.dimasdanz.keamananpintu.util;

import com.dimasdanz.keamananpintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public final class CommonUtilities {

	private CommonUtilities() {}
	
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
	
	public static void dialogConnectionError(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, 4);
		builder.setIcon(android.R.drawable.ic_dialog_info);
    	builder.setTitle(R.string.title_dialog_server_error);
    	builder.setMessage(R.string.message_dialog_connection_error);
    	builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		Activity a = (Activity) context;
        		a.finish();
        	}
        });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
}
