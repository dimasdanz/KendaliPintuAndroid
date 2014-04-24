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
package com.dimasdanz.kendalipintu.usermodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.CommonUtilities;
import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class UserLoadData extends AsyncTask<String, Void, List<UserModel>>{
	private Boolean con = true;
	private JSONParser jsonParser = new JSONParser();
	
	int current_page;
	private UserLoadDataListener mListener;
	private Activity activity;
	
	public interface UserLoadDataListener{
		public void onTaskProgress();
		public void onTaskComplete(List<UserModel> userModel);
	}
	
	public UserLoadData(Activity act, Integer page) {
		this.current_page = page;
		this.mListener = (UserLoadDataListener)act;
		this.activity = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onTaskProgress();
	}
	
	@Override
	protected List<UserModel> doInBackground(String... args) {
		List<UserModel> userList = new ArrayList<UserModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.makeHttpRequest(ServerUtilities.getUserListUrl(activity)+current_page, "GET", params);
		if(json != null){
			try {
				if(json.getInt("response") == 1){
					JSONArray userid = json.getJSONArray("userid");
					JSONArray username = json.getJSONArray("username");
					JSONArray userpass = json.getJSONArray("userpass");
					for (int i = 0; i < userid.length(); i++) {
						userList.add(new UserModel(userid.get(i).toString(), username.get(i).toString(), userpass.getString(i).toString()));
					}
					return userList;
				}else{
					return null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			con = false;
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<UserModel> result) {
		super.onPostExecute(result);
		if(con){
			mListener.onTaskComplete(result);
		}else{
			CommonUtilities.dialogConnectionError(activity);
		}
	}
}
