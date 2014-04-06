package com.dimasdanz.keamananpintu.util;

import com.dimasdanz.keamananpintu.LogActivity;
import com.dimasdanz.keamananpintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

public final class CommonUtilities {
	private static int msgCounter = 0;
	
	public static void dialogConnectionError(final Context context){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, 4);
		builder.setIcon(android.R.drawable.ic_dialog_info);
    	builder.setTitle(R.string.title_dialog_server_error);
    	builder.setMessage(R.string.message_dialog_connection_error);
    	builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
        	public void onClick(DialogInterface dialog, int id) {
        		Activity a = (Activity) context;
        		a.finish();
        	}
        });
    	AlertDialog alert = builder.create();
    	alert.show();
	}
	
	public static void resetNotificationCounter(){
		msgCounter = 0;
	}
	
	public static void generateNotification(Context context, String message){
		NotificationManager manager;
		int notificationID = 73;
		
		Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_stat_notification);
		
		Notification.Builder builder = new Notification.Builder(context);
		Intent resultIntent = new Intent(context, LogActivity.class);		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		
		stackBuilder.addParentStack(LogActivity.class);
		stackBuilder.addNextIntent(resultIntent );
		
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		builder.setAutoCancel(true);
		builder.setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND);
		builder.setContentTitle("Log");
		builder.setContentText(message);
		builder.setTicker("New Log");
		builder.setNumber(++msgCounter);
		builder.setSmallIcon(R.drawable.ic_stat_notification);
		builder.setLargeIcon(largeIcon);
		builder.setContentIntent(resultPendingIntent);
		
		manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(notificationID, builder.build());
	}
}
