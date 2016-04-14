package com.bluemagma.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class NotifyService extends IntentService {

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";


    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i("NotifyService", "latitude = " + intent.getStringExtra(NotifyService.LATITUDE));
        Log.i("NotifyService", "longitude = " + intent.getStringExtra(NotifyService.LONGITUDE));

        WebServiceWeather weatherService = new WebServiceWeather();
        String weatherResponse = weatherService.getWeather(intent.getStringExtra(LATITUDE),
                intent.getStringExtra(LONGITUDE));


        //if (weatherResponse==weatherService.RAIN || weatherResponse==weatherService.SNOW) {
            //String weatherType = weatherResponse==weatherService.SNOW ? "snow" : "rain";

            Notifier notification = new Notifier();
            notification.createNotification(this, weatherResponse);
            Log.i("NotificationApp","notification created");
        //}
        AlarmBroadcastReceiver.completeWakefulIntent(intent);
    }
}
