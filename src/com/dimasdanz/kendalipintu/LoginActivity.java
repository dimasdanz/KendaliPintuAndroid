package com.dimasdanz.kendalipintu;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.util.CommonUtilities;
import com.dimasdanz.kendalipintu.util.LoginAuth;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;
import com.dimasdanz.kendalipintu.util.LoginAuth.LoginAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements LoginAuthListener{
	private ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.action_about) {
			//About Activity
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClickLogin(View v){
		EditText user_id = (EditText)findViewById(R.id.user_id);
		EditText user_pass = (EditText)findViewById(R.id.user_pass);
		new LoginAuth(this).execute(user_id.getText().toString(), user_pass.getText().toString());
	}
	
	public void onClickPrevious(View v){
		onBackPressed();
	}

	@Override
	public void onTaskProgress() {
		pDialog = new ProgressDialog(this);
		pDialog.setMessage("Logging in...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	public void onTaskComplete(String result){
		pDialog.dismiss();
		if(result != null){
			if(result == CommonUtilities.TAG_NO_ACCOUNT){
				Toast.makeText(this, "No Account", Toast.LENGTH_LONG).show();
			}else if(result == CommonUtilities.TAG_INCORRECT_PASSWORD){
				Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show();
			}else{
				SharedPreferencesManager.setLoggedIn(getApplicationContext(), true, result);
				SharedPreferencesManager.setFirstTimePrefs(getApplicationContext(), false);
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		}
	}
}