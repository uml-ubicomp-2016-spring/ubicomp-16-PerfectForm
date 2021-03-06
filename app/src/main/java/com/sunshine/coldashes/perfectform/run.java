package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Coldashes on 4/26/2016.
 */
public class run extends Activity implements SensorEventListener {
    // used to keep track of time passed
    private Chronometer elapsedtime;

    private boolean first = true, startRecording = false, isPaused = true;

    ArrayList<Float> pointsArr = new ArrayList<Float>();
    private Button isPaused_btn;
    private long lastupdate = 0;

    private static final int UPDATE_THRESHOLD = 1000;

    private static int i = 0;

    private ArrayList<Double> paceArray = new ArrayList<Double>();

    private ArrayList<Double> listX = new ArrayList<Double>();
    private ArrayList<Double> listY = new ArrayList<Double>();
    private ArrayList<Double> listZ = new ArrayList<Double>();

    static int samples = 0;

    private Sensor stepcounter, mAccelerometer, mLightSensor;
    private SensorManager mSensorManager;
    private TextView steps, ticks_tv, points_tv;

    private int hour, min;
    private int fast = 0, slow = 0, good = 1;

    public double temp = 0;

    private double stepstaken = 0;

    private ImageView light, pace_imageview, form_imageview;

    private int ticks = 0;

    private float points = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);

        // get access to needed image and textviews
        pace_imageview = (ImageView) findViewById(R.id.pace_imageview);
        form_imageview = (ImageView) findViewById(R.id.form_imageview);
        steps = (TextView) findViewById(R.id.steps_textView);
        light = (ImageView) findViewById(R.id.light_imageview);
        ticks_tv = (TextView) findViewById(R.id.ticks_textView);
        points_tv = (TextView) findViewById(R.id.points_textView);
        elapsedtime = (Chronometer) findViewById(R.id.runtimer);
        isPaused_btn = (Button) findViewById(R.id.timerBtn);

        // get the needed sensors
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        stepcounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        // keeps track of elapsed time since hit start
        elapsedtime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                ticks++;
                // converts ticks to minutes
                if (ticks == 60) {
                    min++;
                    ticks = 0;
                    // check how many steps in a min and calc points
                    checksteps();
                    calculatepoints();
                }
                //convert mins to hours
                if (min == 60) {
                    hour++;
                    min = 0;
                }
                // update the textview
                ticks_tv.setText(new StringBuilder().append(pad(hour)).append(":").append(pad(min)).append(":").append(pad(ticks)));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSensorManager.registerListener(this, stepcounter, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void checksteps() {
        // decides how your pace is every min based on steps/min
        if (stepstaken < 160) {
            paceArray.add(stepstaken);
            slow++;
            pace_imageview.setImageResource(R.drawable.ic_slow);
        } else if (stepstaken > 200) {
            paceArray.add(stepstaken);
            fast++;
            pace_imageview.setImageResource(R.drawable.ic_fast);
        } else {
            good++;
            paceArray.add(stepstaken);
            pace_imageview.setImageResource(R.drawable.ic_good);
        }
    }

    private void calculatepoints() {
        int size = listX.size();
        boolean perfect = true;
        // i is the amount of mins and 960 = 16 sampls/min * 60seconds
        i = i * 960;
        // check the new data every min compared to bounds decided based on the graphs
        while (i < size) {
            if (listX.get(i) < -20) {
                perfect = false;
                points -= .1;
            } else if (listX.get(i) > 35) {
                perfect = false;
                points -= .1;
            }
            if (listY.get(i) < -20) {
                perfect = false;
                points -= .1;
            } else if (listY.get(i) > 30) {
                perfect = false;
                points -= .1;
            }
            if (listZ.get(i) < -20) {
                perfect = false;
                points -= .1;
            } else if (listZ.get(i) > 30) {
                perfect = false;
                points -= .1;
            }
            i++;
        }
        // change the image based on how on the points
        if (perfect) {
            points++;
            form_imageview.setImageResource(R.drawable.ic_goodform);
        } else {
            form_imageview.setImageResource(R.drawable.ic_badform);
        }
        // save the points over time
        pointsArr.add(points);
        points_tv.setText(String.format("%.2f", points));
    }

    // used to format time :0X
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // changes text on the start button and boolean recording value
    public void startPause(View view) {
        if (isPaused_btn.getText().toString().toLowerCase().equals("start")) {
            startRecording = true;
        }
        if (isPaused) {
            startRecording = true;
            isPaused = false;
            isPaused_btn.setText("Pause");
            elapsedtime.start();

        } else {
            startRecording = false;
            isPaused = true;
            elapsedtime.setBase(SystemClock.elapsedRealtime());// - current
            isPaused_btn.setText("Resume");
            elapsedtime.stop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // when the acitivity is resumed listen to sensor events
        mSensorManager.registerListener(this, stepcounter, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mLightSensor, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        // unregister all listeners when app isnt in foreground
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER && startRecording) {
            // if user hit start save how many steps recorded before was clicked
            if (first) {
                temp = event.values[0];
            }
            first = false;

            //subtract steps before the user started their run
            stepstaken = event.values[0] - temp;
            steps.setText(String.valueOf((long) stepstaken));
        }

        // if low light change light icon
        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            if (event.values[0] <= 2) {
                light.setImageResource(R.drawable.ic_dark);

            } else {
                light.setImageResource(R.drawable.ic_lightgood);
            }
        }

        // make sure we are getting 16 samples per second and save them to the arraylist
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && startRecording) {
            long actualTime = System.currentTimeMillis();

            double x = event.values[0], y = event.values[1], z = event.values[2];

            if (samples < 16) {
                listX.add(x);
                listY.add(y);
                listZ.add(z);
            } else if (samples == 16 && (actualTime - lastupdate) > UPDATE_THRESHOLD) {
                lastupdate = actualTime;
                samples = -1;
            }

            samples++;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }

    // when done with run button clicked
    public void finish(View view) {
        // save and pass the arraylists over to the results class
        Intent done = new Intent(this, Results.class);
        done.putExtra("paceArray", paceArray);
        done.putExtra("pointsArr", pointsArr);

        String paceCond = "";
        // decides what your pace was most often
        int max = findMax(slow, fast, good);

        // if there is a tie between any then you will recieve good icon
            if(max == good)
                paceCond = "good";
            else if(max == fast)
                paceCond = "fast";
            else if(max == slow)
                paceCond = "slow";

        done.putExtra("paceCond", paceCond);
        startActivity(done);
    }

    // figures out what your pace was most often
    private int findMax(int... vals) {
        int max = -1;
        int iteration = 0;
        for (int d : vals) {
            if (d > max)
                max = d;
        }
        return max;
    }
}