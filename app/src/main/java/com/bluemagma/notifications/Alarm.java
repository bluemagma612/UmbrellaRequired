package com.bluemagma.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Bluemagma on 4/6/16.
 */
public class Alarm {
    private final String TAG = "NotificationApp";

    protected void setAlarm (Context context, int hour, int minute, String latitude, String longitude) {

        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long milliseconds = calendar.getTimeInMillis();

        alarmMgr.setInexactRepeating(AlarmManager.RTC,
                milliseconds,
                AlarmManager.INTERVAL_DAY,
                getMainActivityPendingIntent(context));
        Log.i(TAG,"alarm main activity set for" + milliseconds);

        alarmMgr.setInexactRepeating(AlarmManager.RTC,
                milliseconds,
                AlarmManager.INTERVAL_DAY,
                getBroadcastActivityPendingIntent(context,latitude,longitude));
        Log.i(TAG,"alarm broad cast activity set for" + milliseconds);

        ComponentName bootReceiver = new ComponentName(context, BootReceiver.class);
        PackageManager packageManager = context.getPackageManager();

        packageManager.setComponentEnabledSetting(bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    protected void cancelAlarm (Context context) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(getMainActivityPendingIntent(context));
        Log.i(TAG,"main activity pending intent cancelling");
        alarmMgr.cancel(getBroadcastActivityPendingIntent(context));
        Log.i(TAG,"broadcast activity pending intent cancelling");


        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    protected PendingIntent getMainActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1234, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.i(TAG,"main activity pending intent fired");
        return(pendingIntent);
    }

    protected PendingIntent getBroadcastActivityPendingIntent(Context context, String latitude, String longitude) {
        Intent intent = new Intent(context,AlarmBroadcastReceiver.class);
        intent.putExtra(NotifyService.LATITUDE, latitude);
        intent.putExtra(NotifyService.LONGITUDE, longitude);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Log.i(TAG,"broadcast activity pending intent fired");

        return(pendingIntent);
    }

    //overloaded to support cancelling an alarm without having to provide lat and long
    protected PendingIntent getBroadcastActivityPendingIntent(Context context) {
        Intent intent = new Intent(context,AlarmBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        Log.i(TAG,"broadcast activity pending intent fired");

        return(pendingIntent);
    }

}
