package com.dimasdanz.keamananpintu.usermodel;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.util.JSONParser;
import com.dimasdanz.keamananpintu.util.ServerUtilities;

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
				json = jsonParser.makeHttpRequest(ServerUtilities.getInsertUser(activity), "POST", params);
			}else{
				params.add(new BasicNameValuePair("id", args[0]));
				params.add(new BasicNameValuePair("name", args[1]));
				params.add(new BasicNameValuePair("password", args[2]));
				json = jsonParser.makeHttpRequest(ServerUtilities.getUpdateUser(activity), "POST", params);
			}
		}else{
			params.add(new BasicNameValuePair("id", args[1]));
			json = jsonParser.makeHttpRequest(ServerUtilities.getDeleteUser(activity), "POST", params);
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
