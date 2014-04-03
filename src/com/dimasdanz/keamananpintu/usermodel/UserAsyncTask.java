package com.dimasdanz.keamananpintu.usermodel;

import java.util.List;

import com.dimasdanz.keamananpintu.util.AsyncTaskListener;

import android.app.Activity;
import android.os.AsyncTask;

public class UserAsyncTask extends AsyncTask<List<UserModel>, Void, Boolean>{
	private AsyncTaskListener mListener;
	
	public UserAsyncTask(Activity act) {
		this.mListener = (AsyncTaskListener)act;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mListener.onTaskProgress();
	}
	
	@Override
	protected Boolean doInBackground(List<UserModel>... params) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		mListener.onTaskComplete();
	}
}
