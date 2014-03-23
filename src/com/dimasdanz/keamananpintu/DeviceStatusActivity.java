package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.DialogManager;
import com.dimasdanz.keamananpintu.util.DialogManager.DialogManagerListener;
import com.dimasdanz.keamananpintu.util.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ProgressDialog;
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

public class DeviceStatusActivity extends FragmentActivity implements DialogManagerListener, OnCheckedChangeListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_status);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		new GetDeviceStatus().execute();
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
		new sendDatatoServer(0).execute(Boolean.toString(isChecked));
		Log.d("onCheckedChanged", Boolean.toString(isChecked));		
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, String value) {
		EditText textAttempts = (EditText) findViewById(R.id.txtAttempts);
		Log.d("PasswordAttempts", value);
		new sendDatatoServer(1).execute(value);
		textAttempts.setText(value);
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog, int value) {
		if(value == 0){
			this.finish();
		}
	}
	
	public void onClickButtonUnlock(View v){
		new sendDatatoServer(2).execute();
		Log.d("onClickButtonUnlock", "Clicked");
	}
	
	public void onClickAttemptsSetting(View v){
		new DialogManager();
		DialogFragment dialogInputAttempts = DialogManager.newInstance(2);
		dialogInputAttempts.show(getSupportFragmentManager(), "InputPasswordAttempts");
	}
	
	class GetDeviceStatus extends AsyncTask<Void, Void, Boolean> {
		boolean condition;
		boolean status;
		String password_attempts;
		ProgressDialog pDialog = new ProgressDialog(DeviceStatusActivity.this);
		JSONParser jsonParser = new JSONParser();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			findViewById(R.id.layout_activity_device_status).setVisibility(View.GONE);
			pDialog.setMessage(getApplicationContext().getString(R.string.loading_message));
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Boolean doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.deviceStatusUrl(getApplicationContext()),"GET", params);
			if(json != null){
				try {
					password_attempts = json.getString("password_attempts");
					condition = json.getBoolean("condition");
					status = json.getBoolean("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.d("Device Status", json.toString());
				return true;
			}else{
				Log.d("Device Status", "Failed");
				return false;
			}
		}

		protected void onPostExecute(Boolean result) {
			if(!result){
				new DialogManager();
				DialogFragment dialogConError = DialogManager.newInstance(0);
				dialogConError.show(getSupportFragmentManager(), "DeviceStatusConnectionError");
			}
			
			findViewById(R.id.layout_activity_device_status).setVisibility(View.VISIBLE);
			
			if(condition){
				findViewById(R.id.layout_device_locked).setVisibility(View.VISIBLE);
				findViewById(R.id.layout_condition).setVisibility(View.GONE);
				findViewById(R.id.layout_attempts).setVisibility(View.GONE);
				findViewById(R.id.layout_status).setVisibility(View.GONE);
			}else{
				findViewById(R.id.layout_device_locked).setVisibility(View.GONE);
				findViewById(R.id.layout_condition).setVisibility(View.VISIBLE);
				findViewById(R.id.layout_attempts).setVisibility(View.VISIBLE);
				findViewById(R.id.layout_status).setVisibility(View.VISIBLE);
				
				EditText textAttempts = (EditText)findViewById(R.id.txtAttempts);
				textAttempts.setText(password_attempts);
				
				Switch statusSwitch = (Switch)findViewById(R.id.swStatus);
				statusSwitch.setChecked(status);
				statusSwitch.setOnCheckedChangeListener(DeviceStatusActivity.this);
			}
			pDialog.dismiss();
		}
	}

	
	class sendDatatoServer extends AsyncTask<String, String, Integer> {
		private int num;
		JSONParser jsonParser = new JSONParser();
		
		protected sendDatatoServer(Integer i){
			this.num = i;
		}
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		protected Integer doInBackground(String... args) {
			JSONObject json = null;
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			switch (num) {
			case 0:
				params.add(new BasicNameValuePair("status", args[0]));
				json = jsonParser.makeHttpRequest(CommonUtilities.changeDeviceStatus(getApplicationContext()), "POST", params);
				break;
			case 1:
				params.add(new BasicNameValuePair("password_attempts", args[0]));
				json = jsonParser.makeHttpRequest(CommonUtilities.changeDeviceAttempts(getApplicationContext()), "POST", params);
				break;
			case 2:
				json = jsonParser.makeHttpRequest(CommonUtilities.unlockDevice(getApplicationContext()), "POST", params);
				break;
			default:
				break;
			}
			
			if(json != null){
				Log.d("Server Response", json.toString());
				try {
					return json.getInt("response");
				} catch (JSONException e) {
					return 5;
				}
			}else{
				return 5;
			}			
		}
		
		/* Response Cheat Sheet
		 * Response = 0 -> Device Locked
		 * Response = 1 -> Change Device Status ON
		 * Response = 2 -> Change Device Status OFF
		 * Response = 3 -> Change Password Attempts
		 * Response = 4 -> Device Unlocked 
		 */
		protected void onPostExecute(Integer result) {
			switch (result) {
			case 0:
				Toast.makeText(DeviceStatusActivity.this, R.string.toast_device_locked, Toast.LENGTH_SHORT).show();
				new GetDeviceStatus().execute();
				break;
			case 1:
				Toast.makeText(DeviceStatusActivity.this, R.string.toast_device_status_on, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(DeviceStatusActivity.this, R.string.toast_device_status_off, Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(DeviceStatusActivity.this, R.string.toast_device_attempts_change, Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(DeviceStatusActivity.this, R.string.toast_device_unlocked, Toast.LENGTH_SHORT).show();
				new GetDeviceStatus().execute();
				break;
			case 5:
				new DialogManager();
				DialogFragment dialogConError = DialogManager.newInstance(0);
				dialogConError.show(getSupportFragmentManager(), "DeviceStatusConnectionError");
				break;
			default:
				break;
			}
		}
	}
}
