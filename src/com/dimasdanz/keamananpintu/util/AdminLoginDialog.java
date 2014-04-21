package com.dimasdanz.keamananpintu.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.keamananpintu.R;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AdminLoginDialog extends DialogPreference{
	private View mView;
	
	public AdminLoginDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(SharedPreferencesManager.getIsAdminPrefs(context)){
			setDialogMessage(R.string.string_admin_logout);
		}else{
			setDialogLayoutResource(R.layout.dialog_admin);
		}
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setPersistent(false);
        setDialogIcon(null);
	}
	
	@Override
	protected void onBindDialogView(View view) {
	    super.onBindDialogView(view);
	    this.mView = view;
	}
	
	@Override
	protected void onDialogClosed(boolean positiveResult) {
	    if (positiveResult) {
	    	if(SharedPreferencesManager.getIsAdminPrefs(getContext())){
	    		SharedPreferencesManager.setAsAdmin(getContext(), false, null);
	    		Toast.makeText(getContext(), R.string.toast_admin_logout_success, Toast.LENGTH_SHORT).show();
	    	}else{
	    		EditText username = (EditText) mView.findViewById(R.id.input_username);
		    	EditText password = (EditText) mView.findViewById(R.id.input_password);
		    	new sendAdminLogin().execute(username.getText().toString(), password.getText().toString());
	    	}
	    }
	}
	
	class sendAdminLogin extends AsyncTask<String, Void, Boolean>{
		@Override
		protected Boolean doInBackground(String... args) {
			JSONParser jsonParser = new JSONParser();
			JSONObject json = new JSONObject();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", args[0]));
			params.add(new BasicNameValuePair("password", args[1]));
			json = jsonParser.makeHttpRequest(ServerUtilities.getAdminLoginUrl(getContext()), "POST", params);
			if(json != null){
				try {
					if(json.getInt("response") == 1){
						persistBoolean(true);
						persistString(json.getString("username"));
						SharedPreferencesManager.setAsAdmin(getContext(), true, json.getString("username"));
						return true;
					}else{
						persistBoolean(false);
						return false;
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			}else{
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result){
			super.onPostExecute(result);
			if(result != null){
				if(result){
					Toast.makeText(getContext(), R.string.toast_admin_login_success, Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getContext(), R.string.toast_admin_login_incorrect, Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getContext(), R.string.toast_connection_error, Toast.LENGTH_SHORT).show();
			}
		}
	}
}
