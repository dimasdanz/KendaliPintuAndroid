package com.dimasdanz.keamananpintu;

import com.dimasdanz.keamananpintu.devicestatusmodel.DeviceStatusModel;
import com.dimasdanz.keamananpintu.devicestatusmodel.DeviceStatusLoadData;
import com.dimasdanz.keamananpintu.devicestatusmodel.DeviceStatusLoadData.DeviceStatusLoadDataListener;
import com.dimasdanz.keamananpintu.devicestatusmodel.DeviceStatusSendData;
import com.dimasdanz.keamananpintu.devicestatusmodel.DeviceStatusSendData.DeviceStatusSendDataListener;
import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.UniversalDialogManager;
import com.dimasdanz.keamananpintu.util.UniversalDialogManager.DialogManagerListener;

import android.os.Bundle;
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

public class DeviceStatusActivity extends FragmentActivity implements DialogManagerListener, OnCheckedChangeListener, DeviceStatusLoadDataListener, DeviceStatusSendDataListener {
	private EditText textAttempts;
	private Switch statusSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_status);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		textAttempts = (EditText)findViewById(R.id.txtAttempts);
		statusSwitch = (Switch)findViewById(R.id.swStatus);
		
		new DeviceStatusLoadData(this).execute();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.device_status, menu);
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
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		new DeviceStatusSendData(this, 0).execute(String.valueOf(isChecked));
	}
	
	public void onClickButtonUnlock(View v){
		new DeviceStatusSendData(this, 2).execute();
	}
	
	public void onClickAttemptsSetting(View v){
		new UniversalDialogManager();
		DialogFragment dialogInputAttempts = UniversalDialogManager.newInstance(1, textAttempts.getText().toString());
		dialogInputAttempts.show(getSupportFragmentManager(), "InputPasswordAttempts");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String data) {
		new DeviceStatusSendData(this, 1).execute(data);
		textAttempts.setText(data);
	}
	
	@Override
	public void onLoadDataProgress() {
		setView(0);
	}

	@Override
	public void onLoadDataComplete(DeviceStatusModel results) {
		if(results != null){
			if(results.getCondition()){
				setView(1);
			}else{
				setView(2);
				textAttempts.setText(String.valueOf(results.getPasswordAttempts()));
				statusSwitch.setChecked(results.getStatus());
				statusSwitch.setOnCheckedChangeListener(this);
			}
		}else{
			CommonUtilities.dialogConnectionError(this);
		}
	}

	@Override
	public void onSendDataComplete(int result) {
		switch (result) {
		case 0:
			Toast.makeText(this, R.string.toast_device_locked, Toast.LENGTH_SHORT).show();
			new DeviceStatusLoadData(this).execute();
			break;
		case 1:
			Toast.makeText(this, R.string.toast_device_status_on, Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Toast.makeText(this, R.string.toast_device_status_off, Toast.LENGTH_SHORT).show();
			break;
		case 3:
			Toast.makeText(this, R.string.toast_device_attempts_change, Toast.LENGTH_SHORT).show();
			break;
		case 4:
			Toast.makeText(this, R.string.toast_device_unlocked, Toast.LENGTH_SHORT).show();
			new DeviceStatusLoadData(this).execute();
			break;
		case 5:
			Toast.makeText(this, R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	
	public void setView(int type){
		switch (type) {
		case 0:
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_device_locked).setVisibility(View.GONE);
			findViewById(R.id.layout_condition).setVisibility(View.GONE);
			findViewById(R.id.layout_attempts).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		case 1:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_locked).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_condition).setVisibility(View.GONE);
			findViewById(R.id.layout_attempts).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_locked).setVisibility(View.GONE);
			findViewById(R.id.layout_condition).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_attempts).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_status).setVisibility(View.VISIBLE);
			break;
		default:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_locked).setVisibility(View.GONE);
			findViewById(R.id.layout_condition).setVisibility(View.GONE);
			findViewById(R.id.layout_attempts).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		}
	}
}
