/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
	
	public static String getDeviceStatusUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/arduino/check_arduino";
	}

	public static String activateDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/arduino/activate";
	}
	
	public static String deactivateDeviceUrl(Context context){
		return SharedPreferencesManager.getHostnamePrefs(context)+"/api/arduino/deactivate";
	}
}
