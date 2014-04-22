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
import android.util.Log;

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
				for (int i = 0; i < name.length(); i++) {
					detail.add(new LogModel(name.get(i).toString(), time.get(i).toString()));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return detail;
		}else{
			Log.d("Log", "Failed");
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<LogModel> result){
		super.onPostExecute(result);
		mListener.onLoadDetailComplete(result);
	}

}
