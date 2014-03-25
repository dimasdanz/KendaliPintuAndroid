package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.logmodel.LogExpandableListAdapter;
import com.dimasdanz.keamananpintu.logmodel.LogModel;
import com.dimasdanz.keamananpintu.util.CommonUtilities;
import com.dimasdanz.keamananpintu.util.JSONParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class LogActivity extends Activity implements OnGroupExpandListener{
	LogExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<LogModel>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		expListView = (ExpandableListView) findViewById(R.id.expListViewLog);
		new getLogDate().execute();
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		new getLogDetail().execute(listDataHeader.get(groupPosition));
	}
	
	class getLogDate extends AsyncTask<Void, Void, Boolean> {
		JSONParser jsonParser = new JSONParser();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
			findViewById(R.id.expListViewLog).setVisibility(View.GONE);
		}

		protected Boolean doInBackground(Void... args) {			
			listDataHeader = new ArrayList<String>();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getLogDate(getApplicationContext()),"GET", params);
			if(json != null){
				try {
					JSONArray date = json.getJSONArray("date");
					for (int i = 0; i < date.length(); i++) {
						listDataHeader.add(date.get(i).toString());
					}
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				return true;
			}else{
				Log.d("Log", "Failed");
				return false;
			}
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
				findViewById(R.id.expListViewLog).setVisibility(View.VISIBLE);
				listDataChild = new HashMap<String, List<LogModel>>();
				List<LogModel> loading = new ArrayList<LogModel>();
				loading.add(new LogModel("Loading", ""));
				
				for (int i = 0; i < listDataHeader.size(); i++) {
					listDataChild.put(listDataHeader.get(i), loading);
				}
				
				listAdapter = new LogExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
		        
		        expListView.setAdapter(listAdapter);
		        expListView.setOnGroupExpandListener(LogActivity.this);
			}else{
				
			}
		}
	}
	
	class getLogDetail extends AsyncTask<String, Void, Boolean> {
		List<LogModel> detail = new ArrayList<LogModel>();
		JSONParser jsonParser = new JSONParser();

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(String... args) {			
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("date", args[0]));
			JSONObject json = jsonParser.makeHttpRequest(CommonUtilities.getLogDetail(getApplicationContext()),"POST", params);
			if(json != null){
				try {
					JSONArray name = json.getJSONArray("name");
					JSONArray time = json.getJSONArray("time");
					for (int i = 0; i < name.length(); i++) {
						detail.add(new LogModel(name.get(i).toString(), time.get(i).toString()));
					}
					listDataChild.put(args[0], detail);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				return true;
			}else{
				Log.d("Log", "Failed");
				return false;
			}
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				listAdapter.refreshView();
			}else{
				
			}
		}
	}
}
