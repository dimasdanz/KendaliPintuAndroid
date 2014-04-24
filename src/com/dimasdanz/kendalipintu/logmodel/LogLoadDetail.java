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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class LogLoadDetail extends AsyncTask<String, Void, List<LogModel>> {
	JSONParser jsonParser = new JSONParser();
	List<LogModel> detail = new ArrayList<LogModel>();
	
	private LogLoadDetailListener mListener;
	private Activity activity;
	
	public interface LogLoadDetailListener{
		public void onLoadDetailComplete(List<LogModel> LogModel);
	}
	
	public LogLoadDetail(Activity act){
		this.mListener = (LogLoadDetailListener) act;
		this.activity = act;
	}
	
	@Override
	protected List<LogModel> doInBackground(String... args) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("date", args[0]));
		JSONObject json = jsonParser.makeHttpRequest(ServerUtilities.getLogDetailUrl(activity),"POST", params);
		if(json != null){
			try {
				JSONArray name = json.getJSONArray("name");
				JSONArray time = json.getJSONArray("time");
				JSONArray info = json.getJSONArray("info");
				for (int i = 0; i < name.length(); i++) {
					detail.add(new LogModel(name.get(i).toString(), time.get(i).toString()+" - "+info.get(i).toString()));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return detail;
		}else{
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<LogModel> result){
		super.onPostExecute(result);
		mListener.onLoadDetailComplete(result);
	}

}
