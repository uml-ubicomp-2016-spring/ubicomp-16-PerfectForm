package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ronald on 3/30/16.
 */
public class LightSensor extends Activity implements SensorEventListener {

    private Sensor mLightSensor;
    private SensorManager mSensorManager;
    private TextView Light_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightsensor);

        Light_textview = (TextView) findViewById(R.id.light_textview);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (mLightSensor == null)
            Toast.makeText(LightSensor.this,"No Light Sensor! quit-", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            Light_textview.setText("LIGHT: " + event.values[0]);
        }
    }

    public void next(View view) {
        Intent myIntent = new Intent(this, GyroscopeSensor.class);
        startActivity(myIntent);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }
}