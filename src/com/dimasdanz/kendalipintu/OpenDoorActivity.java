/*******************************************************************************
 * Copyright (c) 2014 Dimas Rullyan Danu
 * 
 * Kendali Pintu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Kendali Pintu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Kendali Pintu. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.dimasdanz.kendalipintu;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.R;
import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;
import com.dimasdanz.kendalipintu.util.StaticString;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class OpenDoorActivity extends FragmentActivity {
	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String TAG = "NfcDemo";
	private static final String INPUT_SOURCE_OUTSIDE = "Android Masuk";
	private static final String INPUT_SOURCE_INSIDE = "Android Keluar";
	private TextView mTextView;
	private ProgressBar mProgressBar;
	private NfcAdapter mNfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_door);

		mTextView = (TextView) findViewById(R.id.txtOpenDoor);
		mProgressBar = (ProgressBar) findViewById(R.id.pbOpenDoor);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		
		mProgressBar.setVisibility(View.GONE);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		if (!mNfcAdapter.isEnabled()) {
			mTextView.setText(R.string.string_nfc_disabled);
		} else {
			mTextView.setText(R.string.string_nfc_tap);
		}

		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);
			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		}
	}

	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			mProgressBar.setVisibility(View.VISIBLE);
			mTextView.setText(R.string.loading_message);
		}
		
		@Override
		protected String doInBackground(Tag... args) {
			JSONParser jsonParser = new JSONParser();
			JSONObject json = new JSONObject();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Tag tag = args[0];
			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				return null;
			}
			NdefMessage ndefMessage = ndef.getCachedNdefMessage();
			NdefRecord[] records = ndefMessage.getRecords();
			for (NdefRecord ndefRecord : records) {
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(),	NdefRecord.RTD_TEXT)) {
					try {
						if(readText(ndefRecord).equals(StaticString.TAG_ENTER_NFC)){
							params.add(new BasicNameValuePair("username_id", SharedPreferencesManager.getUsernameIdPrefs(getApplicationContext())));
							params.add(new BasicNameValuePair("input_source", INPUT_SOURCE_OUTSIDE));
							json = jsonParser.makeHttpRequest(ServerUtilities.getOpenDoorUrl(getApplicationContext()), "POST", params);
							if(json != null){
								if(json.getBoolean("response")){
									return StaticString.TAG_ENTER_NFC;
								}else{
									return StaticString.TAG_ARDUINO_OFFLINE;
								}								
							}else{
								return StaticString.TAG_SERVER_OFFLINE;
							}					
						}else if(readText(ndefRecord).equals(StaticString.TAG_EXIT_NFC)){
							params.add(new BasicNameValuePair("username_id", SharedPreferencesManager.getUsernameIdPrefs(getApplicationContext())));
							params.add(new BasicNameValuePair("input_source", INPUT_SOURCE_INSIDE));
							json = jsonParser.makeHttpRequest(ServerUtilities.getOpenDoorUrl(getApplicationContext()), "POST", params);
							if(json != null){
								if(json.getBoolean("response")){
									return StaticString.TAG_EXIT_NFC;
								}else{
									return StaticString.TAG_ARDUINO_OFFLINE;
								}
							}else{
								return StaticString.TAG_SERVER_OFFLINE;
							}
						}else{
							return StaticString.TAG_INVALID_NFC;
						}
					} catch (UnsupportedEncodingException e) {
						Log.e(TAG, "Unsupported Encoding", e);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		private String readText(NdefRecord record) throws UnsupportedEncodingException {
			byte[] payload = record.getPayload();
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
			int languageCodeLength = payload[0] & 0063;
			return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		}

		@Override
		protected void onPostExecute(String result) {
			mProgressBar.setVisibility(View.GONE);
			if (result != null) {
				if(result.equals(StaticString.TAG_ENTER_NFC)){
					mTextView.setText(R.string.string_nfc_enter);
				}else if(result.equals(StaticString.TAG_EXIT_NFC)){
					mTextView.setText(R.string.string_nfc_exit);
				}else if(result.equals(StaticString.TAG_INVALID_NFC)){
					mTextView.setText(R.string.string_nfc_invalid);
				}else if(result.equals(StaticString.TAG_ARDUINO_OFFLINE)){
					mTextView.setText(R.string.string_arduino_offline);
				}else{
					mTextView.setText(R.string.string_server_offline);
				}
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.open_door, menu);
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

	@Override
	protected void onResume() {
		super.onResume();
		setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		stopForegroundDispatch(this, mNfcAdapter);
		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		handleIntent(intent);
	}

	public static void setupForegroundDispatch(final Activity activity,	NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}
		adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
	}

	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}
}
