package com.dimasdanz.keamananpintu.logmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.JSONParser;

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
		JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getLogDate(activity),"GET", params);
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
