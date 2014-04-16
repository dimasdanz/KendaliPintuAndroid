package com.dimasdanz.keamananpintu.util;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class GcmBroadcastReceiver extends BroadcastReceiver{

	@Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),GcmIntentService.class.getName());
        context.startService(intent.setComponent(comp));
    }

}
