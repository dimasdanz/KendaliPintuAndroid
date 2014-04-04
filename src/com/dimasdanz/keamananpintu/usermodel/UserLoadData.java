package com.dimasdanz.keamananpintu.usermodel;

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

public class UserLoadData extends AsyncTask<String, Void, List<UserModel>>{
	private Boolean con = true;
	private JSONParser jsonParser = new JSONParser();
	
	int current_page;
	private UserLoadDataListener mListener;
	private Activity activity;
	
	public interface UserLoadDataListener{
		public void onTaskProgress();
		public void onTaskComplete(List<UserModel> userModel);
	}
	
	public UserLoadData(Activity act, Integer page) {
		this.current_page = page;
		this.mListener = (UserLoadDataListener)act;
		this.activity = act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onTaskProgress();
	}
	
	@Override
	protected List<UserModel> doInBackground(String... args) {
		
		List<UserModel> userList = new ArrayList<UserModel>();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getUserList(activity)+current_page, "GET", params);
		if(json != null){
			try {
				if(json.getInt("response") == 1){
					JSONArray userid = json.getJSONArray("userid");
					JSONArray username = json.getJSONArray("username");
					JSONArray userpass = json.getJSONArray("userpass");
					for (int i = 0; i < userid.length(); i++) {
						userList.add(new UserModel(userid.get(i).toString(), username.get(i).toString(), userpass.getString(i).toString()));
					}
					return userList;
				}else{
					return null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			con = false;
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<UserModel> result) {
		super.onPostExecute(result);
		if(con){
			mListener.onTaskComplete(result);
		}else{
			CommonUtilities.dialogConnectionError(activity);
		}
	}
}
