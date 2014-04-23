package com.dimasdanz.kendalipintu.devicestatusmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;
import com.dimasdanz.kendalipintu.util.StaticString;

import android.app.Activity;
import android.os.AsyncTask;

public class DeviceStatusLoadData extends AsyncTask<Void, Void, String>{
	JSONParser jsonParser = new JSONParser();
	
	private DeviceStatusLoadDataListener mListener;
	private Activity activity;
	
	public interface DeviceStatusLoadDataListener{
		public void onLoadDataProgress();
		public void onLoadDataComplete(String results);
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
	protected String doInBackground(Void... args) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.makeHttpRequest(ServerUtilities.getDeviceStatusUrl(activity),"GET", params);
		if(json != null){
			try {
				String response = json.getString("response");
				if(response.equals(StaticString.TAG_STATUS_ACTIVE)){
					return StaticString.TAG_STATUS_ACTIVE;
				}else if(response.equals(StaticString.TAG_STATUS_INACTIVE)){
					return StaticString.TAG_STATUS_INACTIVE;
				}else{
					return StaticString.TAG_STATUS_OFFLINE;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}else{
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		mListener.onLoadDataComplete(result);
	}
}
