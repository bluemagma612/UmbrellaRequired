package com.bluemagma.notifications;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * Created by Bluemagma on 4/8/16.
 */
public class WebServiceWeather {

    public static final String CLEAR = "Clear";
    public static final String RAIN = "Rain";
    public static final String SNOW = "Snow";
    public static final String DONTKNOW = "Dont know";

    public WebServiceWeather() {
    }

    public String getWeather (String... params) {
        HttpURLConnection urlConnection = null;
        String useUmbrellaStr = DONTKNOW;

        try {
            URL url = new URL("http://api.openweathermap.org/" +
                    "data/2.5/forecast/daily" +
                    "?lat=" + params[0] +
                    "&lon=" + params[1] +
                    "&mode=json" +
                    "&units=metric" +
                    "&cnt=1" +
                    "&APPID=fe31fcccd44febb8bafa451f0efe7381");

            urlConnection = (HttpURLConnection) url.openConnection();
            useUmbrellaStr = useUmbrella(urlConnection.getInputStream());
            Log.i("NotificationApp","url = " + url);
        }
        catch (IOException e) {
            Log.e("NotificationApp", "Error ", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return useUmbrellaStr;
    }

    protected String useUmbrella(InputStream in) {
        //read and parse InputStream
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            Log.i("NotificationApp", "parsed string from json" + stringBuilder.toString());

            //JSON needs to be parsed here
            JSONObject forecastJson = new JSONObject(stringBuilder.toString());
            JSONArray weatherArray = forecastJson.getJSONArray("list");
            JSONObject todayForecast = weatherArray.getJSONObject(0);
            if (todayForecast.has("snow")) {
                return(SNOW);
            } else if (todayForecast.has("rain")) {
                return(RAIN);
            } else {
                return(CLEAR);
            }

        } catch (Exception e) {
            Log.e("NotificationApp", "Error", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e("NotificationApp", "Error closing stream", e);
                }
            }
        }
        return DONTKNOW;
    }

}
