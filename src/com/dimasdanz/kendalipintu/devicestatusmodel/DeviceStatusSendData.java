package com.dimasdanz.kendalipintu.devicestatusmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class DeviceStatusSendData extends AsyncTask<String, Void, Integer>{
	JSONParser jsonParser = new JSONParser();
	
	private DeviceStatusSendDataListener mListener;
	private Activity activity;
	private int type;
	
	public interface DeviceStatusSendDataListener{
		public void onSendDataComplete(int result);
	}
	
	public DeviceStatusSendData(Activity act, int type) {
		this.mListener = (DeviceStatusSendDataListener)act;
		this.activity = act;
		this.type = type;
	}
	
	@Override
	protected Integer doInBackground(String... args) {
		JSONObject json = new JSONObject();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		switch(type) {
		case 0:
			params.add(new BasicNameValuePair("status", args[0]));
			json = jsonParser.makeHttpRequest(ServerUtilities.changeDeviceStatusUrl(activity), "POST", params);
			break;
		case 1:
			params.add(new BasicNameValuePair("password_attempts", args[0]));
			json = jsonParser.makeHttpRequest(ServerUtilities.changeDeviceAttemptsUrl(activity), "POST", params);
			break;
		case 2:
			json = jsonParser.makeHttpRequest(ServerUtilities.unlockDeviceUrl(activity), "POST", params);
			break;
		default:
			break;
		}
		if(json != null){
			try {
				return json.getInt("response");
			} catch (JSONException e) {
				return 5;
			}
		}else{
			return 5;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		mListener.onSendDataComplete(result);
	}
}
