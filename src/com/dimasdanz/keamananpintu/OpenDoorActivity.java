package com.dimasdanz.keamananpintu;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

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
import android.widget.TextView;
import android.widget.Toast;

public class OpenDoorActivity extends FragmentActivity {
	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String TAG = "NfcDemo";
	private TextView mTextView;
	private NfcAdapter mNfcAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_open_door);

		mTextView = (TextView) findViewById(R.id.txtOpenDoor);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			finish();
			return;

		}

		if (!mNfcAdapter.isEnabled()) {
			mTextView.setText("NFC is disabled.");
		} else {
			mTextView.setText("NFC is enabled");
		}

		handleIntent(getIntent());
	}

	private void handleIntent(Intent intent) {
		String action = intent.getAction();
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
			Log.d(TAG, "Test");
			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)){
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);
			}else{
				Log.d(TAG, "Wrong mime type: " + type);
			}
		}
	}
	
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {
	    @Override
	    protected String doInBackground(Tag... params) {
	        Tag tag = params[0];
	        Ndef ndef = Ndef.get(tag);
	        if (ndef == null) {
	            return null;
	        }
	        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
	        NdefRecord[] records = ndefMessage.getRecords();
	        for (NdefRecord ndefRecord : records) {
	            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
	                try {
	                    return readText(ndefRecord);
	                } catch (UnsupportedEncodingException e) {
	                    Log.e(TAG, "Unsupported Encoding", e);
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
	        if (result != null) {
	            mTextView.setText("Read content: " + result);
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

	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

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
