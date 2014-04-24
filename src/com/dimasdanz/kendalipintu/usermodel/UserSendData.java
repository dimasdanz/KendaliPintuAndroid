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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class UserSendData extends AsyncTask<String, Void, Boolean>{
	private JSONParser jsonParser = new JSONParser();
	
	private Activity activity;
	private UserSendDataListener mListener;
	
	public interface UserSendDataListener{
		public void onSendDataComplete(Boolean result);
	}
	
	public UserSendData(Activity act) {
		this.mListener = (UserSendDataListener)act;
		this.activity = act;
	}
		
	@Override
	protected Boolean doInBackground(String... args) {
		JSONObject json = new JSONObject();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(args[0] != "delete"){
			if(args[0].isEmpty()){
				params.add(new BasicNameValuePair("name", args[1]));
				params.add(new BasicNameValuePair("password", args[2]));
				json = jsonParser.makeHttpRequest(ServerUtilities.getInsertUserUrl(activity), "POST", params);
			}else{
				params.add(new BasicNameValuePair("id", args[0]));
				params.add(new BasicNameValuePair("name", args[1]));
				params.add(new BasicNameValuePair("password", args[2]));
				json = jsonParser.makeHttpRequest(ServerUtilities.getUpdateUserUrl(activity), "POST", params);
			}
		}else{
			params.add(new BasicNameValuePair("id", args[1]));
			json = jsonParser.makeHttpRequest(ServerUtilities.getDeleteUserUrl(activity), "POST", params);
		}
		
		if(json != null){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result){
		super.onPostExecute(result);
		mListener.onSendDataComplete(result);
	}
}
