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

/**
 * Created by Coldashes NOT BEING USED ONLY FOR DEBUG.
 */
public class GyroscopeSensor extends Activity implements SensorEventListener {
    private static final int UPDATE_THRESHOLD = 5000;
    private SensorManager mSensorManager;
    private Sensor mGyroscope;
    private long mLastUpdate;
    private TextView gyrotext;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        gyrotext = (TextView) findViewById(R.id.gyroscope_textview);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }
    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_UI);
        mLastUpdate = System.currentTimeMillis();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }
        long actualTime = System.currentTimeMillis();

        if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {
            //else it will output the Roll, Pitch and Yawn values
            float x,y,z;
            x = event.values[2];
            y = event.values[1];
            z = event.values[0];
            String resultX = String.format("%.2f", x);
            String resultY = String.format("%.2f", y);
            String resultZ = String.format("%.2f", z);

            gyrotext.setText("X (Roll) :" + resultX + "\n" +
                    "Y (Pitch) :" + resultY + "\n" +
                    "Z (Yaw) :" + resultZ);
        }
    }

    public void next(View view) {
        Intent myIntent = new Intent(this, LightSensor.class);
        startActivity(myIntent);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }
}