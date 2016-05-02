package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;


import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by paul on 4/22/16. edited by Ron
 * This file was created before then, we just had to reimplement
 * in another version of the project so the rest of the program beyond this point
 * was pasted in from another version of the project in order to complete
 * the merge.
 */
public class WeatherActivity extends Activity {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?zip=";
    private static final String SUFFIX_URL = "&units=imperial";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    private String zip;
    private ImageView weathericon;


    /*
    We use this private class asynchronously so that we can wait to receive internet data
    and not hold up loading the rest of the weather activity while we do so.
     */
    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        /*
        These are the variables we use to hold each piece of desired json data
        from the openweathermap api call so that we may later use the variables
        to display their specific piece of weather information to the screen
        in the form of textviews.
         */
        String town = "";
        String weather = "";
        String description = "";
        double temp = 0.0;
        double windSpeed = 0.0;
        long sunrise = 0;
        long sunset = 0;

        String sunriseDate = "";
        String sunsetDate = "";


        /*
        This is what we are using the asynchronous task for,
        here we are trying to grab all the weather data we need
        which should always work unless there's an issue with the
        api key so we have a catch just in case.
         */
        @Override
        protected String doInBackground(String[] params) {

            try {
                Intent intent = getIntent();
                String zip = intent.getExtras().getString("zip");

                JSONObject parser = new JSONObject(getWeatherData(zip));
                JSONObject object;
                town = parser.getString("name");

                JSONArray jsonArray = parser.optJSONArray("weather");

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                weather += jsonObject.getString("main");
                description += jsonObject.getString("description");

                object = parser.getJSONObject("main");
                temp = object.getDouble("temp");

                object = parser.getJSONObject("wind");
                windSpeed = object.getDouble("speed");

                object = parser.getJSONObject("sys");
                sunrise = object.getInt("sunrise");

                sunriseDate += new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (sunrise*1000));

                sunset = object.getInt("sunset");

                sunsetDate += new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (sunset*1000));

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return "Data fetch was attempted.";
        }

        /*
        This occurs after the background process has finished executing.
        Here we are simply setting the weather icon to the correct image
        to represent the current conditions, ie. sun for sunny day, rain cloud
        for rainy weather, etc. We also set the textviews based on the data
        we fetched in the background process so that the user may see
        the temperuture, windspeed, etc.
         */
        @Override
        protected void onPostExecute(String message) {
            TextView txt = (TextView) findViewById(R.id.town_textview);
            txt.setText(town);


            txt = (TextView) findViewById(R.id.description_textview);
            txt.setText(description);

            if(description.toLowerCase().contains("sky")) {
                weathericon.setBackgroundResource(R.drawable.sunny);
            } else if (description.toLowerCase().contains("clouds")) {
                weathericon.setBackgroundResource(R.drawable.cloudy);
            } else if (description.toLowerCase().contains("rain")) {
                weathericon.setBackgroundResource(R.drawable.rainy);
            } else if (description.toLowerCase().contains("thunderstorm")) {
                weathericon.setBackgroundResource(R.drawable.thunder);
            } else if (description.toLowerCase().contains("snow")) {
                weathericon.setBackgroundResource(R.drawable.snow);
            } else if (description.toLowerCase().contains("mist")) {
                weathericon.setBackgroundResource(R.drawable.mist);
            } else {
                weathericon.setBackgroundResource(R.drawable.missing);
            }

            txt = (TextView) findViewById(R.id.tempurature_textview);
            txt.setText(String.valueOf(temp));

            txt = (TextView) findViewById(R.id.wind_textview);
            txt.setText(String.valueOf(windSpeed));

            txt = (TextView) findViewById(R.id.sunrise_textview);
            txt.setText(sunriseDate);

            txt = (TextView) findViewById(R.id.sunset_textview);
            txt.setText(sunsetDate);

            makeSuggestion(sunsetDate, sunriseDate, description, temp, windSpeed);
        }
    }

    /*
    Called on launch of the activity we set up all the information to call the
    background asynchronous processing which will do the bulk of the work
    for this activity because we are almost completely reliant on internet
    data for this activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        setContentView(R.layout.activity_weather);

        String main = "Main: ";
        String base = "";

        Bundle bundle = getIntent().getExtras();
        String zip = bundle.getString("zip");

        setContentView(R.layout.activity_weather);
        TextView textView = (TextView) findViewById(R.id.town_textview);
        textView.setText(main);
        weathericon = (ImageView) findViewById(R.id.weathericon);
        SendfeedbackJob job = new SendfeedbackJob();
        main += job.execute(base);
    }


    /*
    The following functions before the schedule function
    are modified versions taken from the surviving with android
    tutorial and lesson sequence. These functions are only used to provide tools to
    fectch the http data from the internet and allow for targeting specific json data
    once we have relocated to the correct scope in order to inspect them.

    This lesson was a great jumping off point for someeone who had never programmed
    for android before and it helped to teach exactly how we can access certain pieces
    of data remotely over the internet.
     */

    /*
    JSON tools used to extract certain fields in the our current scope
    and set them to the appropriate data type.
     */
    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

    /*
    Retrieve weather data from the internet, provides exception handling.
     */
    public String getWeatherData(String location) {
        HttpURLConnection con = null;
        InputStream is = null;
        StringBuffer buffer = new StringBuffer();
        try {
            con = (HttpURLConnection) (new URL(BASE_URL + location + SUFFIX_URL)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            Log.d("ADebugTag", "Value: " + "weatherdata");


            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null) {
                buffer.append(line + "\r\n");
            }

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return buffer.toString();
    }

    /*
    Load extra information for which weather icons to use.
     */
    public byte[] getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while (is.read(buffer) != -1) {
                baos.write(buffer);
            }

            return baos.toByteArray();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }

    /*
    We use this to transition into the next view so that the user
    may choose when to schedule their run based off the weather data
    and recommendations we feed to them.
     */
    public void schedule(View view) {
        Intent i = new Intent(this, ScheduleActivity.class);
        startActivity(i);
    }

    /*
    Here we extrapolate on all the weather data that we retrieved in order
    to make smart suggestions based off of what weather conditions are the
    most or least conducive to running. Suggestions are also made to provide
    advice to improve their running experience if the conditions are suboptimal.

    Suggestions are then placed near the bottom of this activity's screen.
    */
    public void makeSuggestion(String timeOfSunset, String timeOfSunrise, String theWeather, double theTemp, double theWindspeed) {
        String suggestion = "";

        boolean cloudy = false;
        boolean rainy = false;
        boolean thunderstorm = false;
        boolean snow = false;
        boolean mist = false;
        boolean sunny = false;

        if(theWeather.toLowerCase().contains("sky")) {
            sunny = true;
        } else if (theWeather.toLowerCase().contains("clouds")) {
            cloudy = true;
        } else if (theWeather.toLowerCase().contains("rain")) {
            rainy = true;
        } else if (theWeather.toLowerCase().contains("thunderstorm")) {
            thunderstorm = true;
        } else if (theWeather.toLowerCase().contains("snow")) {
            snow = true;
        } else if (theWeather.toLowerCase().contains("mist")) {
            mist = true;
        }

        if(thunderstorm || snow) {
            suggestion += "Probably not a good time for a run... but if you insist\n";
        }

        if(rainy) {
            suggestion += "Its raining wear a rain jacket or wait till later!\n";
        }

        if(mist) {
            suggestion += "Its foggy try to wear flourescent colors!\n";
        }


        if(theTemp > 80 && sunny) {
            suggestion += "It's a HOT one! Make sure you wear shorts and a T-shirt and maybe run as the sunrises or while it is setting.\n";
        } else if(theTemp > 80 && cloudy) {
            suggestion += "It's a HOT one! Since its cloudy anytime should be a good time\n";
        } else if(theTemp < 40) {
            suggestion += "It's a COLD one! Try to wear layers\n";
        }  else if(theTemp > 40 && cloudy){
            suggestion += "Wow would be perfect day for a run if the sun was out! Anytime would be ok though!\n";
        } else {
            suggestion += "Wow seems like a nice day to run! If you would prefer it cooler make sure you run early or as the sun sets!\n";
        }

        if(theWindspeed > 20) {
            suggestion += "Its a windy day make sure to wear a windbreaker!\n";
        } else if(theWindspeed > 10) {
            suggestion += "Warning slight breeze may want a thin jacket.\n";
        } else {
            suggestion += "";
        }

        TextView txt = (TextView) findViewById(R.id.suggestion_textview);
        txt.setText(suggestion);
    }
}

