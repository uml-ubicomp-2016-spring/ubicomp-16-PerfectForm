package com.example.bobby.perfectform;

import com.example.andriodlightsensor.R;


import android.app.Activity;
import android.os.Bundle;


import android.widget.Button;
import android.content.Context;
import android.widget.ProgressBar;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;


public class AndriodLightSensorActivity extends Activity {

    ProgressBar lightMeter;
    TextView textMax, textReading;
    float counter;
    Button read;
    TextView display;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        counter = 0;
        read = (Button) findViewById(R.id.bStart);
        display = (TextView) findViewById(R.id.tvDisplay);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lightMeter = (ProgressBar)findViewById(R.id.lightmeter);
        textMax = (TextView)findViewById(R.id.max);
        textReading = (TextView)findViewById(R.id.reading);

        SensorManager sensorManager
                = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor
                = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        if (lightSensor == null){
            Toast.makeText(AndriodLightSensorActivity.this,
                    "The light sensor does not seem like it is working.",
                    Toast.LENGTH_LONG).show();
        }else {
            float max = lightSensor.getMaximumRange();
            lightMeter.setMax((int) max);
            textMax.setText("It seems really bright out, wear some sunglasses and perhaps bring some water for your run!" + String.valueOf(max));
            sensorManager.registerListener(lightSensorEventListener,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    SensorEventListener lightSensorEventListener
            = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType()==Sensor.TYPE_LIGHT){
                final float currentReading = event.values[0];
                lightMeter.setProgress((int)currentReading);
                textReading.setText("Current Reading(Lux): " + String.valueOf(currentReading));
                read.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        display.setText("It doesn't seem too bright, if it is too dark, run with a reflexive vest or refrain from running." + String.valueOf(currentReading));
                    }
                });

            }
        }

    };
}
