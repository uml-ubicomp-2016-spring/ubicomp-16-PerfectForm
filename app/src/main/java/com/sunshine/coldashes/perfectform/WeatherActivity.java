package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

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
public class WeatherActivity extends Activity {
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?zip=";
    private static final String SUFFIX_URL = "&units=imperial";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";


    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        String town = "";
        String weather = "";
        String description = "";
        double temp = 0.0;
        int humidityPercent = 0;
        double windSpeed = 0.0;
        int sunrise = 0;
        int sunset = 0;

        String sunriseDate = "";
        String sunsetDate = "";

        /* Not always available for all readings
        int tempmin = 0;
        int tempmax = 0;*/


        @Override
        protected String doInBackground(String[] params) {

            try {
                JSONObject parser = new JSONObject(getWeatherData("01854,us&appid=2900f6dcae9280512952aac3a316d4b0"));
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

        @Override
        protected void onPostExecute(String message) {
            //process message
            TextView txt = (TextView) findViewById(R.id.town_textview);
            txt.setText(town); // txt.setText(result);

            txt = (TextView) findViewById(R.id.condition_textview);
            txt.setText(weather); // txt.setText(result);

            txt = (TextView) findViewById(R.id.description_textview);
            txt.setText(description); // txt.setText(result);

            txt = (TextView) findViewById(R.id.tempurature_textview);
            txt.setText(String.valueOf(temp)); // txt.setText(result);
            //txt.setText(String.valueOf(temp)); // txt.setText(result);


            txt = (TextView) findViewById(R.id.wind_textview);
            txt.setText(String.valueOf(windSpeed)); // txt.setText(result);

            txt = (TextView) findViewById(R.id.sunrise_textview);
            txt.setText(sunriseDate); // txt.setText(result);

            txt = (TextView) findViewById(R.id.sunset_textview);
            txt.setText(sunsetDate); // txt.setText(result);

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        String test = "RRRRRRR";

        //refresh_button.setOnClickListener(onClickListener);

        //Button button = (Button)findViewById(R.id.refresh_button);
        //button.setOnClickListener(this);

        setContentView(R.layout.activity_weather);
        //findViewById(R.id.refresh_button).setOnClickListener(refresh_OnClickListener);

        //String output = getWeatherData("London,uk&appid=2900f6dcae9280512952aac3a316d4b0");

        //System.out.println(output);
        //Log.d("ADebugTag", "Value: " + output);

        String main = "Main: ";
        String base = "London,uk&appid=2900f6dcae9280512952aac3a316d4b0";

        SendfeedbackJob job = new SendfeedbackJob();
        main += job.execute(base);


        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, main, duration);
        toast.show();

        setContentView(R.layout.activity_weather);
        TextView textView = (TextView) findViewById(R.id.town_textview);
        textView.setText(main);

    }

    /*public void onViewCreated(View view, Bundle savedInstanceState)
    {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }
    }*/

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
    /*final View.OnClickListener refresh_OnClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            //Inform the user the button has been clicked
            //Toast.makeText(this, "Button1 clicked.", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_weather);
            TextView textView = (TextView) findViewById(R.id.town_textview);
            textView.setText("button has been clicked");

        }
    };*/


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


    /*
    private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            int count = urls.length;
            long totalSize = 0;
            for (int i = 0; i < count; i++) {
                totalSize += Downloader.downloadFile(urls[i]);
                publishProgress((int) ((i / (float) count) * 100));
                // Escape early if cancel() is called
                if (isCancelled()) break;
            }
            return totalSize;
        }

        protected void onProgressUpdate(Integer... progress) {
            setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Long result) {
            showDialog("Downloaded " + result + " bytes");
        }
    }
    */


}

