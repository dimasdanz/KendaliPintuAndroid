package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.usermodel.UserAdapter;
import com.dimasdanz.keamananpintu.usermodel.UserListView;
import com.dimasdanz.keamananpintu.usermodel.UserModel;
import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

public class UserActivity extends Activity implements UserListView.EndlessListener{
	UserListView userLv;
	int current_page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		userLv = (UserListView) findViewById(R.id.userListView);
		
		new InitializeUserList().execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_add_account:
				//TODO Handle Add New User here
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class LoadUserList extends AsyncTask<String, Void, List<UserModel>> {
		private Boolean con = true;
		JSONParser jsonParser = new JSONParser();
		
		@Override
		protected List<UserModel> doInBackground(String... args) {
			List<UserModel> userList = new ArrayList<UserModel>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getUserList(getApplicationContext())+current_page, "GET", params);
			if(json != null){
				try {
					Log.d("User", Integer.toString(json.getInt("response")));
					if(json.getInt("response") == 1){
						JSONArray userid = json.getJSONArray("userid");
						JSONArray username = json.getJSONArray("username");
						for (int i = 0; i < userid.length(); i++) {
							userList.add(new UserModel(userid.get(i).toString(), username.get(i).toString()));
						}
					}else{
						return null;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}else{
				con = false;
				return null;
			}
			return userList;
		}

		@Override
		protected void onPostExecute(List<UserModel> result) {			
			super.onPostExecute(result);
			if(con){
				if(result != null){
					current_page += 1;
					userLv.addNewData(result);
				}else{
					userLv.addNewData(null);
				}
			}else{
				//TODO Connection Error
			}
		}
	}
	
	@Override
	public void loadData() {
		LoadUserList loadUl = new LoadUserList();
		loadUl.execute();
	}
	
	private class InitializeUserList extends AsyncTask<String, Void, List<UserModel>> {
		private Boolean con = true;
		JSONParser jsonParser = new JSONParser();
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//TODO Add some cool progress bar here
		}
		
		@Override
		protected List<UserModel> doInBackground(String... args) {
			List<UserModel> userList = new ArrayList<UserModel>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getUserList(getApplicationContext())+current_page, "GET", params);
			if(json != null){
				try {
					if(json.getInt("response") == 1){
						JSONArray userid = json.getJSONArray("userid");
						JSONArray username = json.getJSONArray("username");
						for (int i = 0; i < userid.length(); i++) {
							userList.add(new UserModel(userid.get(i).toString(), username.get(i).toString()));
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
				if(result != null){
					UserAdapter userAdp = new UserAdapter(UserActivity.this, result, R.layout.list_item_user);
					userLv.setLoadingView(R.layout.loading_layout);
					userLv.setLoadedView(R.layout.loaded_layout);
					userLv.setAdapter(userAdp);
					
					userLv.setListener(UserActivity.this);
				}else{
					result = new ArrayList<UserModel>();
					result.add(new UserModel("000", "User List Kosong"));
					UserAdapter userAdp = new UserAdapter(UserActivity.this, result, R.layout.list_item_user);
					userLv.setLoadingView(R.layout.loading_layout);
					userLv.setLoadedView(R.layout.loaded_layout);
					userLv.setAdapter(userAdp);
					
					userLv.setListener(UserActivity.this);
					userLv.addNewData(null);
				}
				current_page += 1;
			}else{
				//TODO Connection Error
			}
		}
	}
}
