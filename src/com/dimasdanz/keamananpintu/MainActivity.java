package com.dimasdanz.keamananpintu;

import com.dimasdanz.keamananpintu.util.SharedPreferencesManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if(!SharedPreferencesManager.getLoggedInPrefs(this)){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	        startActivity(intent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
		if(SharedPreferencesManager.getIsAdminPrefs(this)){
			Intent intent = new Intent(getApplicationContext(), LogActivity.class);
	        startActivity(intent);
		}else{
			Toast.makeText(this, R.string.toast_admin_privilege, Toast.LENGTH_SHORT).show();
		}
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
		//TODO You know what to do
	}

	
}
