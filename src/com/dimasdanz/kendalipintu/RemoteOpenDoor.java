package com.dimasdanz.kendalipintu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;
import com.dimasdanz.kendalipintu.util.StaticString;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RemoteOpenDoor extends FragmentActivity {
	ProgressBar pb;
	TextView txtInfo;
	Button btnRemote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remote_open_door);
		txtInfo = (TextView)findViewById(R.id.txtRemoteInfo);
		pb = (ProgressBar)findViewById(R.id.pbRemote);
		btnRemote = (Button)findViewById(R.id.btnRemoteOpenDoor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.remote_open_door, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickRemoteOpenDoor(View v){
		new RemoteSendData().execute();
	}
	
	class RemoteSendData extends AsyncTask<Void, Void, String>{
		JSONParser jsonParser = new JSONParser();
		JSONObject json = new JSONObject();
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		btnRemote.setVisibility(View.GONE);
    		txtInfo.setVisibility(View.VISIBLE);
    		pb.setVisibility(View.VISIBLE);
    		txtInfo.setText(R.string.loading_message);
    	}
    	
		@Override
		protected String doInBackground(Void... args) {
			params.add(new BasicNameValuePair("username_id", SharedPreferencesManager.getUsernameIdPrefs(getApplicationContext())));
			params.add(new BasicNameValuePair("input_source", StaticString.INPUT_SOURCE_REMOTE));
			json = jsonParser.makeHttpRequest(ServerUtilities.getOpenDoorUrl(getApplicationContext()), "POST", params);
			if(json != null){
				try {
					if(json.getBoolean("response")){
						return StaticString.TAG_DOOR_OPENED;
					}else{
						return StaticString.TAG_ARDUINO_OFFLINE;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return StaticString.TAG_SERVER_OFFLINE;
				}								
			}else{
				return StaticString.TAG_SERVER_OFFLINE;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (result != null) {
				if(result.equals(StaticString.TAG_DOOR_OPENED)){
					txtInfo.setText(R.string.string_door_opened);
				}else if(result.equals(StaticString.TAG_ARDUINO_OFFLINE)){
					txtInfo.setText(R.string.string_arduino_offline);
				}else{
					txtInfo.setText(R.string.string_server_offline);
				}
			}
    		pb.setVisibility(View.GONE);
		}
	}
}
