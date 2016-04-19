package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;


import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ronald on 4/7/16.
 */
public class WeatherActivity extends Activity{
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static final String SUFFIX_URL = "&units=imperial";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String test = "RRRRRRR";

        //refresh_button.setOnClickListener(onClickListener);

        //Button button = (Button)findViewById(R.id.refresh_button);
        //button.setOnClickListener(this);

        setContentView(R.layout.activity_weather);
        findViewById(R.id.refresh_button).setOnClickListener(refresh_OnClickListener);

        String output = getWeatherData("London,uk&appid=2900f6dcae9280512952aac3a316d4b0");

        System.out.println(output);

        String main = "Main: ";

        try
        {
            JSONObject parser = new JSONObject(output);

            JSONObject sys  = parser.getJSONObject("weather");
            main += sys.getString("main");

            //JSONObject main  = parser.getJSONObject("main");
            //temperature = main.getString("temp");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, main, duration);
        toast.show();

        setContentView(R.layout.activity_weather);
        TextView textView = (TextView) findViewById(R.id.town_textview);
        textView.setText(test);

    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
    }

    private static final String tag = "myActivity";


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



    /*private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(final View v) {
            switch(v.getId()){
                case R.id.refresh_button:
                    String weatherData = getWeatherData("London,uk&appid=2900f6dcae9280512952aac3a316d4b0");
                    setContentView(R.layout.activity_weather);
                    TextView textView = (TextView) findViewById(R.id.town_textview);
                    textView.setText("it worked dude.");
                    break;
            }

        }
    };*/


    //On click listener for button1
    final View.OnClickListener refresh_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            //Inform the user the button has been clicked
            //Toast.makeText(this, "Button1 clicked.", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_weather);
            TextView textView = (TextView) findViewById(R.id.town_textview);
            textView.setText("button has been clicked");

        }
    };


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

            //Log.d("ADebugTag2", "Value: " + "in getweatherdata");
            Log.d("ADebugTag", "Value: " + "weatherdata");


            // Let's read the response

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


    public byte[] getImage(String code) {
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
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
}

