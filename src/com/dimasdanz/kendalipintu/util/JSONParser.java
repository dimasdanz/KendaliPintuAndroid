package com.dimasdanz.kendalipintu.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {
	static InputStream is = null;
	static JSONObject jObj = null;
	static String json = "";
	int timeout = 5000;

	public JSONParser() {

	}

	public JSONObject makeHttpRequest(String url, String method,List<NameValuePair> params) {
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setSoTimeout(httpParameters, timeout);
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
			if (method == "POST") {
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			} else if (method == "GET") {
				DefaultHttpClient httpClient = new DefaultHttpClient(httpParameters);
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}
		} catch (SocketTimeoutException e) {
			Log.e("SocketTimeoutException", "SocketTimeoutException");
			e.printStackTrace();
			return null;
		}catch (UnsupportedEncodingException e) {
			Log.e("UnsupportedEncodingException", "UnsupportedEncodingException");
			e.printStackTrace();
			return null;
		} catch (ClientProtocolException e) {
			Log.e("ClientProtocolException", "ClientProtocolException");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.e("IOException", "IOException");
			e.printStackTrace();
			return null;
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Buffer Error :  " + e.toString());
			return null;
		}

		try {
			jObj = new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
			return null;
		}
		return jObj;
	}
}
