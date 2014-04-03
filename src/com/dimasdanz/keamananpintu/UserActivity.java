package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.usermodel.UserAdapter;
import com.dimasdanz.keamananpintu.usermodel.UserAsyncTask;
import com.dimasdanz.keamananpintu.usermodel.UserDialogManager;
import com.dimasdanz.keamananpintu.usermodel.UserListView;
import com.dimasdanz.keamananpintu.usermodel.UserModel;
import com.dimasdanz.keamananpintu.util.AsyncTaskListener;
import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class UserActivity extends FragmentActivity implements com.dimasdanz.keamananpintu.usermodel.UserDialogManager.DialogManagerListener, UserListView.EndlessListener, OnItemClickListener, AsyncTaskListener{
	UserListView userLv;
	UserAdapter userAdp;
	int current_page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		userLv = (UserListView) findViewById(R.id.userListView);
		userLv.setOnItemClickListener(this);
		new InitializeUserList().execute();
		new UserAsyncTask(this).execute();
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
				new UserDialogManager();
				UserDialogManager.newInstance(null).show(getSupportFragmentManager(), "UserForm");
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
					if(json.getInt("response") == 1){
						JSONArray userid = json.getJSONArray("userid");
						JSONArray username = json.getJSONArray("username");
						JSONArray userpass = json.getJSONArray("userpass");
						for (int i = 0; i < userid.length(); i++) {
							userList.add(new UserModel(userid.get(i).toString(), username.get(i).toString(), userpass.getString(i).toString()));
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
				CommonUtilities.dialogConnectionError(UserActivity.this);
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
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
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
				findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
				if(result != null){
					userAdp = new UserAdapter(UserActivity.this, result, R.layout.list_item_user);
					userLv.setLoadingView(R.layout.loading_layout);
					userLv.setLoadedView(R.layout.loaded_layout);
					userLv.setAdapter(userAdp);
					
					userLv.setListener(UserActivity.this);
				}else{
					result = new ArrayList<UserModel>();
					result.add(new UserModel("000", "User List Kosong", "000"));
					userAdp = new UserAdapter(UserActivity.this, result, R.layout.list_item_user);
					userLv.setLoadingView(R.layout.loading_layout);
					userLv.setLoadedView(R.layout.loaded_layout);
					userLv.setAdapter(userAdp);
					
					userLv.setListener(UserActivity.this);
					userLv.addNewData(null);
				}
				current_page += 1;
			}else{
				CommonUtilities.dialogConnectionError(UserActivity.this);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ArrayList<String> userData = new ArrayList<String>();
		userData.add(userAdp.getItem(position).getUserID());
		userData.add(userAdp.getItem(position).getName());
		userData.add(userAdp.getItem(position).getPassword());
		new UserDialogManager();
		UserDialogManager.newInstance(userData).show(getSupportFragmentManager(), "UserForm");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog,ArrayList<String> data) {
		// TODO Submit data to server
		
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Delete data maybe?
		
	}

	@Override
	public void onTaskProgress() {
		Log.d("Progress", "Progress");
	}

	@Override
	public void onTaskComplete() {
		Log.d("Complete", "Complete");
	}
}
