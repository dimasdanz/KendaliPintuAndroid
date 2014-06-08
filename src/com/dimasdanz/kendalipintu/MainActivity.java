/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	private NfcAdapter mNfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(SharedPreferencesManager.getFirstTimePrefs(getApplicationContext())){
			Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
			startActivity(intent);
		}else if(!SharedPreferencesManager.getLoggedInPrefs(getApplicationContext())){
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
	        startActivity(intent);
		}
		mNfcAdapter = NfcAdapter.getDefaultAdapter(getApplicationContext());
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
		}else if (id == R.id.logout) {
			SharedPreferencesManager.setLoggedIn(getApplicationContext(), false, "000");
			SharedPreferencesManager.setFirstTimePrefs(getApplicationContext(), true);
			Intent intent = new Intent(getApplicationContext(), SetupActivity.class);
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
		if (mNfcAdapter == null || !mNfcAdapter.isEnabled()) {
			Intent intent = new Intent(getApplicationContext(), BarcodeOpenDoorActivity.class);
	        startActivity(intent);
		}else{
			Intent intent = new Intent(getApplicationContext(), NFCOpenDoorActivity.class);
	        startActivity(intent);
		}
	}
	
	public void onClickButtonBukaPintuJarakJauh(View v){
		Intent intent = new Intent(getApplicationContext(), RemoteOpenDoor.class);
        startActivity(intent);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
