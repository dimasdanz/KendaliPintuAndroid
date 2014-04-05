package com.dimasdanz.keamananpintu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dimasdanz.keamananpintu.logmodel.LogAdapter;
import com.dimasdanz.keamananpintu.logmodel.LogLoadData;
import com.dimasdanz.keamananpintu.logmodel.LogLoadData.LogLoadDataListener;
import com.dimasdanz.keamananpintu.logmodel.LogLoadDetail;
import com.dimasdanz.keamananpintu.logmodel.LogLoadDetail.LogLoadDetailListener;
import com.dimasdanz.keamananpintu.logmodel.LogModel;
import com.dimasdanz.keamananpintu.util.CommonUtilities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class LogActivity extends FragmentActivity implements OnGroupExpandListener, LogLoadDataListener, LogLoadDetailListener{
	LogAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<LogModel>> listDataChild;
    List<LogModel> loading;
    String pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		listDataChild = new HashMap<String, List<LogModel>>();
		listDataHeader = new ArrayList<String>();
		listAdapter = new LogAdapter(getApplicationContext(), listDataHeader, listDataChild);
		loading = new ArrayList<LogModel>();
		
		expListView = (ExpandableListView) findViewById(R.id.expListViewLog);
        expListView.setAdapter(listAdapter);
		expListView.setOnGroupExpandListener(LogActivity.this);
		
		new LogLoadData(this).execute();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_refresh:
			new LogLoadData(this).execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGroupExpand(int groupPosition) {
		pos = listDataHeader.get(groupPosition);
		new LogLoadDetail(this).execute(pos);
	}

	@Override
	public void onLoadDataProgress() {
		findViewById(R.id.layout_progress_bar).setVisibility(View.VISIBLE);
		findViewById(R.id.expListViewLog).setVisibility(View.GONE);
	}

	@Override
	public void onLoadDataComplete(ArrayList<String> result) {
		findViewById(R.id.layout_progress_bar).setVisibility(View.GONE);
		findViewById(R.id.expListViewLog).setVisibility(View.VISIBLE);
		if(result != null){			
			listDataHeader.addAll(result);
			
			loading.add(new LogModel("Loading", ""));
			for (int i = 0; i < listDataHeader.size(); i++){
				listDataChild.put(listDataHeader.get(i), loading);
			}
		}else{
			CommonUtilities.dialogConnectionError(LogActivity.this);
		}
	}

	@Override
	public void onLoadDetailComplete(List<LogModel> LogModel){
		if(LogModel != null){
			listDataChild.put(pos, LogModel);
			listAdapter.refreshView();
		}
	}
}
