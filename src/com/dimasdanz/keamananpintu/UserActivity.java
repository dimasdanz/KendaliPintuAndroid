package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.List;

import com.dimasdanz.keamananpintu.usermodel.UserAdapter;
import com.dimasdanz.keamananpintu.usermodel.UserDialogManager.UserDialogManagerListener;
import com.dimasdanz.keamananpintu.usermodel.UserLoadData;
import com.dimasdanz.keamananpintu.usermodel.UserLoadData.UserLoadDataListener;
import com.dimasdanz.keamananpintu.usermodel.UserDialogManager;
import com.dimasdanz.keamananpintu.usermodel.UserListView;
import com.dimasdanz.keamananpintu.usermodel.UserModel;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class UserActivity extends FragmentActivity implements UserDialogManagerListener, UserListView.EndlessListener, OnItemClickListener, UserLoadDataListener{
	UserListView userLv;
	UserAdapter userAdp;
	Integer page = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		List<UserModel> initData = new ArrayList<UserModel>();
		initData.add(new UserModel("0", "null", "0"));
		
		userAdp = new UserAdapter(UserActivity.this, initData, R.layout.list_item_user);
		
		userLv = (UserListView) findViewById(R.id.userListView);
		userLv.setOnItemClickListener(this);
		
		userLv.setLoadingView(R.layout.loading_layout);
		userLv.setLoadedView(R.layout.loaded_layout);
		userLv.setAdapter(userAdp);
		
		userLv.setListener(UserActivity.this);
		
		userAdp.clear();
		new UserLoadData(this, page).execute();
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
	
	
	@Override
	public void loadData() {
		new UserLoadData(this, page).execute();
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
	public void onTaskProgress(){
		if(page == 1){
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
		}else{
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
		}
	}

	@Override
	public void onTaskComplete(List<UserModel> userModel){
		if(userModel != null){
			userLv.addNewData(userModel);
			page += 1;
		}else{
			userLv.addNewData(null);
		}
	}
}
