package com.dimasdanz.keamananpintu.util;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class GcmIntentService extends IntentService {

	public GcmIntentService() {
		super("GcmIntentService");
	}

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (!extras.isEmpty()){
        	String time = (String) extras.get("notification_time");
        	String name = (String) extras.get("notification_message");
        	if(SharedPreferencesManager.getNotificationPrefs(getApplicationContext())){
        		CommonUtilities.generateNotification(getApplicationContext(), name);
        	}
        }
    }
}
