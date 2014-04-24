package com.dimasdanz.kendalipintu;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.dimasdanz.kendalipintu.opendoor.BarcodePreview;
import com.dimasdanz.kendalipintu.util.JSONParser;
import com.dimasdanz.kendalipintu.util.ServerUtilities;
import com.dimasdanz.kendalipintu.util.SharedPreferencesManager;
import com.dimasdanz.kendalipintu.util.StaticString;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Size;

import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import net.sourceforge.zbar.Config;

public class BarcodeOpenDoorActivity extends Activity {
	private Camera mCamera;
    private BarcodePreview mPreview;
    private Handler autoFocusHandler;

    ImageScanner scanner;

    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    } 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_barcode_open_door);

		autoFocusHandler = new Handler();
        mCamera = getCameraInstance();

        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new BarcodePreview(this, mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.barcode_open_door, menu);
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
	
    public void onPause() {
        super.onPause();
        releaseCamera();
    }
    
    public void onResume() {
    	super.onResume();
    	if (mCamera == null) {
            previewing = true;
            mCamera = getCameraInstance();
            mPreview = new BarcodePreview(this, mCamera, previewCb, autoFocusCB);
        }
    }
    
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
        }
        return c;
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
    	public void run() {
    		if (previewing){
    			mCamera.autoFocus(autoFocusCB);
    		}
    	}
    };

    PreviewCallback previewCb = new PreviewCallback() {
    	public void onPreviewFrame(byte[] data, Camera camera) {
    		Camera.Parameters parameters = camera.getParameters();
    		Size size = parameters.getPreviewSize();
    		Image barcode = new Image(size.width, size.height, "Y800");
    		barcode.setData(data);
    		
    		int result = scanner.scanImage(barcode);
    		
    		if (result != 0) {
    			SymbolSet syms = scanner.getResults();
    			for (Symbol sym : syms) {
    				if(sym.getData().equals(StaticString.TAG_BARCODE_ENTER)){
    					Log.d("Result", "Masuk");
    					previewing = false;
    	    			mCamera.setPreviewCallback(null);
    	    			mCamera.stopPreview();
    	    			releaseCamera();
    	    			new BarcodeSendData().execute(StaticString.TAG_BARCODE_ENTER);
    				}else if(sym.getData().equals(StaticString.TAG_BARCODE_EXIT)){
    					Log.d("Result", "Keluar");
    					previewing = false;
    	    			mCamera.setPreviewCallback(null);
    	    			mCamera.stopPreview();
    	    			releaseCamera();
    	    			new BarcodeSendData().execute(StaticString.TAG_BARCODE_EXIT);
    				}else{
    					Toast.makeText(BarcodeOpenDoorActivity.this, R.string.string_barcode_invalid, Toast.LENGTH_SHORT).show();
    					Log.d("Result", "Invalid");
    				}
    			}
    		}
    	}
    };

    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
    	public void onAutoFocus(boolean success, Camera camera) {
    		autoFocusHandler.postDelayed(doAutoFocus, 1000);
    	}
    };
    
    class BarcodeSendData extends AsyncTask<String, Void, String>{
    	JSONParser jsonParser = new JSONParser();
		JSONObject json = new JSONObject();
    	TextView txtInfo = (TextView)findViewById(R.id.txtBarcodeInfo);
    	List<NameValuePair> params = new ArrayList<NameValuePair>();
    	
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
    		findViewById(R.id.layoutCamera).setVisibility(View.GONE);
    		findViewById(R.id.layoutResult).setVisibility(View.VISIBLE);
    	}
    	
		@Override
		protected String doInBackground(String... args) {
			if(args[0].equals(StaticString.TAG_BARCODE_ENTER)){
				params.add(new BasicNameValuePair("username_id", SharedPreferencesManager.getUsernameIdPrefs(getApplicationContext())));
				params.add(new BasicNameValuePair("input_source", StaticString.INPUT_SOURCE_OUTSIDE));
				json = jsonParser.makeHttpRequest(ServerUtilities.getOpenDoorUrl(getApplicationContext()), "POST", params);
				if(json != null){
					try {
						if(json.getBoolean("response")){
							return StaticString.TAG_BARCODE_ENTER;
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
			}else if(args[0].equals(StaticString.TAG_BARCODE_EXIT)){
				params.add(new BasicNameValuePair("username_id", SharedPreferencesManager.getUsernameIdPrefs(getApplicationContext())));
				params.add(new BasicNameValuePair("input_source", StaticString.INPUT_SOURCE_INSIDE));
				json = jsonParser.makeHttpRequest(ServerUtilities.getOpenDoorUrl(getApplicationContext()), "POST", params);
				if(json != null){
					try {
						if(json.getBoolean("response")){
							return StaticString.TAG_BARCODE_EXIT;
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
			}else{
				return StaticString.TAG_SERVER_OFFLINE;
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			findViewById(R.id.pbBarcode).setVisibility(View.GONE);
			if(result != null){
				if(result.equals(StaticString.TAG_BARCODE_ENTER)){
					txtInfo.setText(R.string.string_barcode_enter);
				}else if(result.equals(StaticString.TAG_BARCODE_EXIT)){
					txtInfo.setText(R.string.string_barcode_exit);
				}else if(result.equals(StaticString.TAG_ARDUINO_OFFLINE)){
					txtInfo.setText(R.string.string_arduino_offline);
				}else{
					txtInfo.setText(R.string.string_server_offline);
				}
			}
		}
    }
}
