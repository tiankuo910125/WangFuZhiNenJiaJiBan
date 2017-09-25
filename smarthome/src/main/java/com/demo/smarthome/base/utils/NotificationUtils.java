package com.demo.smarthome.base.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.demo.smarthome.R;


public class NotificationUtils {
    private static final String TAG = NotificationUtils.class.getSimpleName();
    private static NotificationManager notificationManager;

    public static void generateNotification(Context context,String title, String message, Intent intent) {
        sendNotificationNew(context, title, message, intent);
    }

    public static void sendNotificationNew(final Context context, String title, final String msg, final Intent intent) {
        Log.i(TAG, String.format("NotificationGenerator,  title=[%s], msg=[%s]",  title, msg));

        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        if (TextUtils.isEmpty(title)) {
            title = context.getString(R.string.app_name);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title)
                .setContentText(msg).setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(msg)).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);
        if (intent != null)
        {
            mBuilder.setContentIntent(getPendingIntent(context, intent));
        }
        notificationManager.notify(0, mBuilder.build());
    }

    private static PendingIntent getPendingIntent(final Context context, final Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
    }

}