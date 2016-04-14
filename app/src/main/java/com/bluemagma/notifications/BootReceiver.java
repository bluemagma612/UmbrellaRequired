package com.bluemagma.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public BootReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
//            Alarm alarm = new Alarm();
//            alarm.setAlarm(context);
//        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
