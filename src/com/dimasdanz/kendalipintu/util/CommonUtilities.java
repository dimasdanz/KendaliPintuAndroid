package com.dimasdanz.kendalipintu.util;

import com.dimasdanz.kendalipintu.LogActivity;
import com.dimasdanz.kendalipintu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

public final class CommonUtilities {
	private static int msgCounter = 0;
	private static Spannable[] name = new Spannable[6];
	
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
	
	public static void generateNotification(Context context, String message, String time, String info){
		try {
			NotificationManager manager;
			int notificationID = 73;
			
			Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_stat_notification);
			
			Notification.Builder builder = new Notification.Builder(context);
			Intent resultIntent = new Intent(context, LogActivity.class);		
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			
			stackBuilder.addParentStack(LogActivity.class);
			stackBuilder.addNextIntent(resultIntent );
			
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			Spannable sb = new SpannableString(message+" "+time+"-"+info);
			sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			
			builder.setAutoCancel(true);
			builder.setDefaults(Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND);
			builder.setContentTitle(context.getString(R.string.notification_title));
			builder.setContentText(sb);
			builder.setTicker(context.getString(R.string.notification_ticker));
			builder.setNumber(++msgCounter);
			builder.setSmallIcon(R.drawable.ic_stat_notification);
			builder.setLargeIcon(largeIcon);
			builder.setContentIntent(resultPendingIntent);
			
			if(msgCounter > 1){
				Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
				if(msgCounter > 6){
					name[0] = new SpannableString("...");
					name[1] = name[2];
					name[2] = name[3];
					name[3] = name[4];
					name[4] = name[5];
					name[5] = sb;
				}else{
					name[msgCounter-1] = sb;
				}
				
				inboxStyle.setBigContentTitle(context.getString(R.string.notification_title));
				inboxStyle.setSummaryText(context.getString(R.string.app_name));
				
				for(int i=name.length; i > 0; i--){
					inboxStyle.addLine(name[i-1]);
				}
				
				builder.setStyle(inboxStyle);
				builder.setContentText(msgCounter+" "+context.getString(R.string.notification_title));
			}else{
				name[0] = sb;
			}
			manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(notificationID, builder.build());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
	}
	
	public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
