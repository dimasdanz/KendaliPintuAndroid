package com.dimasdanz.kendalipintu;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.devicestatusmodel.DeviceStatusLoadData;
import com.dimasdanz.kendalipintu.devicestatusmodel.DeviceStatusSendData;
import com.dimasdanz.kendalipintu.devicestatusmodel.DeviceStatusLoadData.DeviceStatusLoadDataListener;
import com.dimasdanz.kendalipintu.devicestatusmodel.DeviceStatusSendData.DeviceStatusSendDataListener;
import com.dimasdanz.kendalipintu.util.CommonUtilities;
import com.dimasdanz.kendalipintu.util.StaticString;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;

public class DeviceStatusActivity extends FragmentActivity implements OnCheckedChangeListener, DeviceStatusLoadDataListener, DeviceStatusSendDataListener {
	private Switch statusSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_status);
		getActionBar().setDisplayHomeAsUpEnabled(true);
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
		new DeviceStatusSendData(this).execute(isChecked);
	}

	@Override
	public void onLoadDataProgress() {
		setView(0);
	}

	@Override
	public void onLoadDataComplete(String results) {
		if(results != null){
			if(results.equals(StaticString.TAG_STATUS_ACTIVE)){
				setView(2);
				statusSwitch.setChecked(true);
				statusSwitch.setOnCheckedChangeListener(this);
			}else if(results.equals(StaticString.TAG_STATUS_INACTIVE)){
				setView(2);
				statusSwitch.setChecked(false);
				statusSwitch.setOnCheckedChangeListener(this);
			}else{
				setView(1);
			}
		}else{
			CommonUtilities.dialogConnectionError(this);
		}
	}

	@Override
	public void onSendDataComplete(Boolean result) {
		if(result != null){
			if(result){
				Toast.makeText(this, R.string.toast_device_status_on, Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(this, R.string.toast_device_status_off, Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(this, R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setView(int type){
		switch (type) {
		case 0:
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_device_offline).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		case 1:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_offline).setVisibility(View.VISIBLE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		case 2:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_offline).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.VISIBLE);
			break;
		default:
			findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
			findViewById(R.id.layout_device_offline).setVisibility(View.GONE);
			findViewById(R.id.layout_status).setVisibility(View.GONE);
			break;
		}
	}	
}
