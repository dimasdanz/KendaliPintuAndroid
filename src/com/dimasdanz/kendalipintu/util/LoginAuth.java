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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class LoginAuth extends AsyncTask<String, Void, String> {
	private Boolean con = true;
	private JSONParser jsonParser = new JSONParser();
	
	private LoginAuthListener mListener;
	private Activity activity;
	
	private GoogleCloudMessaging gcm;
	String SENDER_ID = "635594376353";
	
	public interface LoginAuthListener{
		public void onTaskProgress();
		public void onTaskComplete(String result);
	}
	
	public LoginAuth(Activity act) {
		this.mListener = (LoginAuthListener)act;
		this.activity = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onTaskProgress();
	}
	
	@Override
	protected String doInBackground(String... args) {
		JSONObject json = new JSONObject();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user_id", args[0]));
		params.add(new BasicNameValuePair("password", args[1]));
		json = jsonParser.makeHttpRequest(ServerUtilities.getLoginUrl(activity), "POST", params);
		if(json != null){
			try {
				int response = json.getInt("response");
				Log.d("Login Response", String.valueOf(response));
				if(response == 0){
					return StaticString.TAG_INCORRECT_PASSWORD;
				}else if(response == 1){
					String user_id = json.getString("user_id");
					gcm = GoogleCloudMessaging.getInstance(activity);
					String regid = SharedPreferencesManager.getRegId(activity);
					if (regid.isEmpty()) {
						regid = gcm.register(SENDER_ID);
						params.clear();
						params.add(new BasicNameValuePair("user_id", user_id));
						params.add(new BasicNameValuePair("gcm_id", regid));
						json = jsonParser.makeHttpRequest(ServerUtilities.getRegisterDeviceUrl(activity), "POST", params);
						SharedPreferencesManager.setRegId(activity, regid);
					}
					return user_id;
				}else if(response == 2){
					return StaticString.TAG_NO_ACCOUNT;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			con = false;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if(con){
			mListener.onTaskComplete(result);
		}else{
			CommonUtilities.dialogConnectionError(activity);
		}
	}

}
