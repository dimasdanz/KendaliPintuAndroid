package com.dimasdanz.keamananpintu;

import com.dimasdanz.keamananpintu.util.SharedPreferencesManager;
import com.dimasdanz.keamananpintu.util.UniversalDialogManager;
import com.dimasdanz.keamananpintu.util.UniversalDialogManager.DialogManagerListener;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

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
	
	public void onClickHostnameSetting(View v){
		new UniversalDialogManager();
		UniversalDialogManager.
			newInstance(2, SharedPreferencesManager.getHostnamePrefs(this).substring(7)).
			show(getSupportFragmentManager(), "InputHostname");
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			SharedPreferencesManager.setNotificationPrefs(this, true);
			Toast.makeText(this, R.string.toast_notification_setting_on, Toast.LENGTH_SHORT).show();
		}else{
			SharedPreferencesManager.setNotificationPrefs(this, false);
			Toast.makeText(this, R.string.toast_notification_setting_off, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String data) {
		SharedPreferencesManager.setHostnamePrefs(this, data);
		hostnameText.setText(SharedPreferencesManager.getHostnamePrefs(this));
	}


}
