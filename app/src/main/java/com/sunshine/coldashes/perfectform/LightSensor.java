package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ronald on 3/30/16 used this file just to learn how lightsensor operates.
 */
public class LightSensor extends Activity implements SensorEventListener {

    private Sensor mLightSensor;
    private SensorManager mSensorManager;
    private TextView Light_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightsensor);

        // gets the light textview to show how bright in the room
        Light_textview = (TextView) findViewById(R.id.light_textview);

        // make sure to alert the user if the light sensor was not found
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLightSensor == null)
            Toast.makeText(LightSensor.this,"No Light Sensor! quit-", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register listener when returning to the app
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            // value[0] holds the value sensor has stored
            Light_textview.setText("LIGHT: " + event.values[0]);
        }
    }

    public void next(View view) {
        // just move onto the certain activity when button is pressed
        Intent myIntent = new Intent(this, GyroscopeSensor.class);
        startActivity(myIntent);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {
    //empty
    }
}