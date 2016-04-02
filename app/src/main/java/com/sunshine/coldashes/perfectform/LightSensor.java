package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;

/**
 * Created by ronald on 3/30/16.
 */
public class LightSensor extends Activity implements SensorEventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightsensor);

    }
     @Override
    public void onSensorChanged(SensorEvent event) {
    }

        @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }
}