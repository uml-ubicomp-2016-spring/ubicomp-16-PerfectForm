package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Vector;

import org.w3c.dom.Text;

/**
 * Created by Coldashes on 3/6/2016.
 */
public class SensorAccerometer extends Activity implements SensorEventListener {

    private TextView mXValueView, mYValueView, mZValueView;
    private long mLastUpdate;
    static int i = 0;
    static double di = 0;
    static Boolean startRecording = false;
    static DataPoint[] ArrayX = new DataPoint[200];
    static DataPoint[] ArrayY = new DataPoint[200];
    static DataPoint[] ArrayZ = new DataPoint[200];


    static int a = 0, b = 1;
    private static final int UPDATE_THRESHOLD = 20;
    private SensorManager mSensorManager;
    private Sensor mAcellerometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mXValueView = (TextView) findViewById(R.id.x_value_view);
        mYValueView = (TextView) findViewById(R.id.y_value_view);
        mZValueView = (TextView) findViewById(R.id.z_value_view);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        if (null == (mAcellerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)))
            finish();

        final Button displayBtn = (Button) findViewById(R.id.displayBtn);
        displayBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayGraph(0);
            }
        });
        final Button clearBtn = (Button) findViewById(R.id.clearBtn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displayGraph(1);
            }
        });
    }

    protected void onResume() {
        super.onResume();

        mSensorManager.registerListener(this, mAcellerometer, SensorManager.SENSOR_DELAY_UI);
        mLastUpdate = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (startRecording) {
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                long actualTime = System.currentTimeMillis();

                if (actualTime - mLastUpdate > UPDATE_THRESHOLD) {
                    double x = event.values[0], y = event.values[1], z = event.values[2];

                    mXValueView.setText(String.valueOf(x));
                    mYValueView.setText(String.valueOf(y));
                    mZValueView.setText(String.valueOf(z));

                    if (i < 200) {
                        di++;

                        DataPoint pX = new DataPoint(di, x);
                        ArrayX[i] = pX;

                        DataPoint pY = new DataPoint(di, y);
                        ArrayY[i] = pY;

                        DataPoint pZ = new DataPoint(di, z);
                        ArrayZ[i] = pZ;

                        i++;
                    } else {
                        startRecording = false;
                        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                        // Vibrate for 500 milliseconds
                        v.vibrate(500);
                    }

                    mLastUpdate = System.currentTimeMillis();
                }
            }
        }
    }

    private double highPass(double current, double gravity) {
        return current - gravity;
    }

    private void displayGraph(int num) {
        GraphView graphX = (GraphView) findViewById(R.id.graphX);
        GraphView graphY = (GraphView) findViewById(R.id.graphZ);
        GraphView graphZ = (GraphView) findViewById(R.id.graphY);

        if (num == 0) {
            LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<DataPoint>(ArrayX);
            LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<DataPoint>(ArrayY);
            LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<DataPoint>(ArrayZ);
            seriesX.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(SensorAccerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                }
            });
            seriesY.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(SensorAccerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                }
            });
            seriesZ.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(SensorAccerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                }
            });
            graphX.addSeries(seriesX);
            graphY.addSeries(seriesY);
            graphZ.addSeries(seriesZ);

        } else {
            i = 0;
            di = 0;
            graphX.removeAllSeries();
            graphY.removeAllSeries();
            graphZ.removeAllSeries();
        }
    }

    public void startButton(View view) {
        startRecording = true;
    }

    public void stopButton(View view) {
        startRecording = false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }
}
