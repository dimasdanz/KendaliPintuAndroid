package com.dimasdanz.kendalipintu.devicestatusmodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;

import android.app.Activity;
import android.os.AsyncTask;

public class DeviceStatusSendData extends AsyncTask<Boolean, Void, Boolean>{
	JSONParser jsonParser = new JSONParser();
	
	private DeviceStatusSendDataListener mListener;
	private Activity activity;
	
	public interface DeviceStatusSendDataListener{
		public void onSendDataComplete(Boolean result);
	}
	
	public DeviceStatusSendData(Activity act) {
		this.mListener = (DeviceStatusSendDataListener)act;
		this.activity = act;
	}
	
	@Override
	protected Boolean doInBackground(Boolean... args) {
		JSONObject json = new JSONObject();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(args[0]){
			json = jsonParser.makeHttpRequest(ServerUtilities.activateDeviceUrl(activity), "GET", params);
		}else{
			json = jsonParser.makeHttpRequest(ServerUtilities.deactivateDeviceUrl(activity), "GET", params);
		}
		if(json != null){
			if(args[0]){
				return true;
			}else{
				return false;
			}
		}else{
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		mListener.onSendDataComplete(result);
	}
}
