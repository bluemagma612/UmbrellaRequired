package com.bluemagma.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Bluemagma on 4/8/16.
 */
public class Notifier extends NotificationCompat {

    private int notificationID = 100;

    protected void createNotification(Context context, String weatherType) {

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context);
        nBuilder.setContentTitle("Take an umbrella!");
        nBuilder.setContentText("It's going to "+weatherType+" today.");
        //TODO find umbrella icon and import it
        nBuilder.setSmallIcon(R.drawable.ic_launcher);

        nBuilder.setAutoCancel(true);

        //add a notification action
        nBuilder.setContentIntent(getMainActivityPendingIntent(context));

        //post notification
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationID, nBuilder.build());
    }

    protected PendingIntent getMainActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return(pendingIntent);
    }
}
