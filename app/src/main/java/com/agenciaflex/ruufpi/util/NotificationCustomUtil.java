package com.agenciaflex.ruufpi.util;

/**
 * Created by alanssantos on 5/12/15.
 */
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;

import com.agenciaflex.ruufpi.MainActivity;
import com.agenciaflex.ruufpi.R;

import java.util.List;


public class NotificationCustomUtil {
    private static NotificationManager mNotificationManager;

    public static void sendNotification(Context context, String title, String author, String message) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> services = activityManager
                .getRunningTasks(Integer.MAX_VALUE);
        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString()
                .equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }

        if (isActivityFound) {
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setContentTitle(title)
                    .setContentText(author);

            mBuilder.setContentIntent(contentIntent);

            Notification notification = mBuilder.build();
            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            mNotificationManager.notify(2, notification);
        } else {
            // write your code to build a notification.
            // return the notification you built here
        }


    }
}

