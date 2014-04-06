package com.dimasdanz.keamananpintu.devicestatusmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.util.JSONParser;
import com.dimasdanz.keamananpintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class DeviceStatusLoadData extends AsyncTask<Void, Void, DeviceStatusModel>{
	JSONParser jsonParser = new JSONParser();
	
	private DeviceStatusLoadDataListener mListener;
	private Activity activity;
	
	public interface DeviceStatusLoadDataListener{
		public void onLoadDataProgress();
		public void onLoadDataComplete(DeviceStatusModel results);
	}
	
	public DeviceStatusLoadData(Activity act) {
		this.mListener = (DeviceStatusLoadDataListener) act;
		this.activity = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onLoadDataProgress();
	}
	
	@Override
	protected DeviceStatusModel doInBackground(Void... args) {
		DeviceStatusModel deviceStatusModel = null;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.makeHttpRequest(ServerUtilities.deviceStatusUrl(activity),"GET", params);
		if(json != null){
			try {
				deviceStatusModel = new DeviceStatusModel(json.getBoolean("status"), json.getInt("password_attempts"), json.getBoolean("condition"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return deviceStatusModel;
		}else{
			return null;
		}
	}

	@Override
	protected void onPostExecute(DeviceStatusModel result) {
		super.onPostExecute(result);
		mListener.onLoadDataComplete(result);
	}
}
