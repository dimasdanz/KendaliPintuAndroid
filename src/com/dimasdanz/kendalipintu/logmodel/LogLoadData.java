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
package com.dimasdanz.kendalipintu.logmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class LogLoadData extends AsyncTask<Void, Void, ArrayList<String>>{
	JSONParser jsonParser = new JSONParser();
	ArrayList<String> listDataHeader = new ArrayList<String>();
	
	private LogLoadDataListener mListener;
	private Activity activity;
	
	
	public interface LogLoadDataListener{
		public void onLoadDataProgress();
		public void onLoadDataComplete(ArrayList<String> result);
	}
	
	public LogLoadData(Activity act){
		this.mListener = (LogLoadDataListener)act;
		this.activity = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onLoadDataProgress();
	}
	
	@Override
	protected ArrayList<String> doInBackground(Void... args) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.makeHttpRequest(ServerUtilities.getLogDateUrl(activity),"GET", params);
		if(json != null){
			try {
				JSONArray date = json.getJSONArray("date");
				for (int i = 0; i < date.length(); i++) {
					listDataHeader.add(date.get(i).toString());
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return listDataHeader;
		}else{
			Log.d("Log", "Failed");
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		mListener.onLoadDataComplete(result);
	}

}
