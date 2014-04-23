package com.dimasdanz.kendalipintu;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(SharedPreferencesManager.getFirstTimePrefs(getApplicationContext())){
			Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
			startActivity(intent);
		}else if(!SharedPreferencesManager.getLoggedInPrefs(this)){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	        startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
	        startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickButtonStatusPerangkat(View v){
		if(SharedPreferencesManager.getIsAdminPrefs(this)){
			Intent intent = new Intent(getApplicationContext(), DeviceStatusActivity.class);
	        startActivity(intent);
		}else{
			Toast.makeText(this, R.string.toast_admin_privilege, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickButtonLog(View v){
		Intent intent = new Intent(getApplicationContext(), LogActivity.class);
        startActivity(intent);
	}
	
	public void onClickButtonPengguna(View v){
		if(SharedPreferencesManager.getIsAdminPrefs(this)){
			Intent intent = new Intent(getApplicationContext(), UserActivity.class);
	        startActivity(intent);
		}else{
			Toast.makeText(this, R.string.toast_admin_privilege, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onClickButtonPengaturan(View v){
		Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
	}
	
	public void onClickButtonBukaPintu(View v){
		Intent intent = new Intent(getApplicationContext(), OpenDoorActivity.class);
        startActivity(intent);
	}

	
}
