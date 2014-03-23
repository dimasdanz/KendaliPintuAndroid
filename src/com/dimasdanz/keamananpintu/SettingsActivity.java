package com.dimasdanz.keamananpintu;

import com.dimasdanz.keamananpintu.util.DialogManager;
import com.dimasdanz.keamananpintu.util.DialogManager.DialogManagerListener;
import com.dimasdanz.keamananpintu.util.SharedPreferencesManager;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class SettingsActivity extends FragmentActivity implements OnCheckedChangeListener, DialogManagerListener{
	private EditText hostnameText;
	private Switch notificationSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		hostnameText = (EditText)findViewById(R.id.hostname_text);
		notificationSwitch = (Switch)findViewById(R.id.notifSwitch);
		hostnameText.setText(SharedPreferencesManager.getHostnamePrefs(this));
		notificationSwitch.setChecked(SharedPreferencesManager.getNotificationPrefs(this));
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		notificationSwitch.setOnCheckedChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickHostnameSetting(View v){
		new DialogManager();
		DialogFragment dialog = DialogManager.newInstance(1);
        dialog.show(getSupportFragmentManager(), "HostnameDialog");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			SharedPreferencesManager.setNotificationPrefs(this, true);
			Toast.makeText(this, getApplicationContext().getString(R.string.toast_notification_setting_on), Toast.LENGTH_SHORT).show();
		}else{
			SharedPreferencesManager.setNotificationPrefs(this, false);
			Toast.makeText(this, getApplicationContext().getString(R.string.toast_notification_setting_off), Toast.LENGTH_SHORT).show();
		}		
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String string) {
		SharedPreferencesManager.setHostnamePrefs(getApplicationContext(), string);
		hostnameText.setText(SharedPreferencesManager.getHostnamePrefs(getApplicationContext()));
		Log.i("Ok", string);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog, int value) {
		Log.i("No", "No");
	}
}
