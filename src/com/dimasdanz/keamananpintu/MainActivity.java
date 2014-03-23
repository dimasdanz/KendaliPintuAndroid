package com.dimasdanz.keamananpintu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClickButtonStatusPerangkat(View v){
		Intent intent = new Intent(getApplicationContext(), DeviceStatusActivity.class);
        startActivity(intent);
	}
	
	public void onClickButtonLog(View v){
		Intent intent = new Intent(getApplicationContext(), LogActivity.class);
        startActivity(intent);
	}
	
	public void onClickButtonPengguna(View v){
		Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
	}
	
	public void onClickButtonPengaturan(View v){
		Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
	}

}
