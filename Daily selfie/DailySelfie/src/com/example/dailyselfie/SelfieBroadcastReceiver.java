package com.example.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SelfieBroadcastReceiver extends BroadcastReceiver {

	private static final int NOTIFICATION_ID = 123;

	@Override
	public void onReceive(Context context, Intent intent) {
		generateNotification(context, intent);
	}

	@SuppressWarnings("deprecation")
	public void generateNotification(Context context, Intent intent) {
		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification note = new Notification(R.drawable.selfie, "Daily Selfie", System.currentTimeMillis());
		note.flags |= Notification.FLAG_AUTO_CANCEL;

		PendingIntent bintent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

		note.setLatestEventInfo(context, "Daily Selfie", "Time for another selfie.", bintent);

		notifManager.notify(NOTIFICATION_ID, note);
	}

}