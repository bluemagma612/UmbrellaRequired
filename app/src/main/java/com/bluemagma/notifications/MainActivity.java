package com.bluemagma.notifications;

import android.location.Location;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "NotificationApp";
    protected GoogleApiClient mGoogleApiClient;
    protected String latitude;
    protected String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG,"onCreate builder");

        Button alarmButton = (Button) findViewById(R.id.btnAlarm);
        alarmButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 setAlarm();
             }
        });

        Button cancelAlarmButton = (Button) findViewById(R.id.btnCancelAlarm);
        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

    }

    protected void setAlarm() {

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        minute  = minute + 1;


        Alarm alarm = new Alarm();
        alarm.setAlarm(this, hour, minute, latitude, longitude);
        Log.i(TAG, "Setting alarm for " + hour + minute);
    }

    protected void cancelAlarm() {
        Alarm alarm = new Alarm();
        alarm.cancelAlarm(this);
        Log.i(TAG, "Requesting alarm cancelled");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG,"onConnected start");
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        latitude = String.valueOf(mCurrentLocation.getLatitude());
        longitude = String.valueOf(mCurrentLocation.getLongitude());
        Log.i(TAG,"lat = " + latitude);
        Log.i(TAG,"lng =" + longitude);
        Log.i(TAG,"Location acquired, calling webservice task");

        WebServiceTask webserviceTask = new WebServiceTask();
        webserviceTask.execute(latitude,longitude);
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG,"onConnectedFailed");
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        Log.i(TAG,"Google APi Client connected");
    }
    private class WebServiceTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.i(TAG,"webservice task doInBackground");

            WebServiceWeather weatherService = new WebServiceWeather();
            String weatherResponse = weatherService.getWeather(params[0],params[1]);
            switch (weatherResponse) {
                case WebServiceWeather.CLEAR:
                    return("No");
                case WebServiceWeather.RAIN:
                case WebServiceWeather.SNOW:
                    return("Yes");
                default:
                    return("Don't know!");
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView textview = (TextView) findViewById(R.id.txtUmbrella);
            textview.setText("Take an umbrella? " + s);
            Log.i(TAG,"onPostExecute textview text updated");
        }
    }

}
