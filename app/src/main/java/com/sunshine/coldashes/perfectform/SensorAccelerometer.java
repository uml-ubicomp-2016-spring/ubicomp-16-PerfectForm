package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;

/**
 * Created by Coldashes on 3/6/2016.
 */
public class SensorAccelerometer extends Activity implements SensorEventListener {

    private TextView mXValueView, mYValueView, mZValueView;
    private long mLastUpdate;
    static int i = 0;
    static int timesRun = 0;
    static double di = 0;
    static Boolean startRecording = false;
    static DataPoint[] ArrayX = new DataPoint[600];
    static DataPoint[] ArrayY = new DataPoint[600];
    static DataPoint[] ArrayZ = new DataPoint[600];
    private EditText size_edittext;
    static int a = 0, b = 1;
    private static final int UPDATE_THRESHOLD = 1000;
    private SensorManager mSensorManager;
    private Sensor mAcellerometer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
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

    @Override
    protected void onStop() {
        super.onStop();


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

                    if (i < 600) {
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
                        v.vibrate(1000);
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
                    Toast.makeText(SensorAccelerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                }
            });
            seriesY.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(SensorAccelerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
                }
            });
            seriesZ.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(SensorAccelerometer.this, "Series1: On Data Point clicked: " + dataPoint, Toast.LENGTH_LONG).show();
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


    public void next(View view) {
        Intent myIntent = new Intent(this, LightSensor.class);
        startActivity(myIntent);
    }

    public void save(View view) {
        timesRun++;
        String AccelerometerDataX = "AccelerometerDataX" + timesRun;
        String AccelerometerDataY = "AccelerometerDataY" + timesRun;
        String AccelerometerDataZ = "AccelerometerDataZ" + timesRun;

        File fileX = new File(getApplicationContext().getFilesDir(), AccelerometerDataX);
        fileX.setReadable(true, false);

        File fileY = new File(getApplicationContext().getFilesDir(), AccelerometerDataY);
        fileY.setReadable(true, false);

        File fileZ = new File(getApplicationContext().getFilesDir(), AccelerometerDataZ);
        fileZ.setReadable(true, false);

        int x = 0;
        //Toast newToast = Toast.makeText(this, firstX, Toast.LENGTH_LONG);
        //newToast.show();
        String newLine = "\n";
        try {
            FileOutputStream outputStreamX;
            FileOutputStream outputStreamY;
            FileOutputStream outputStreamZ;
            outputStreamX = openFileOutput(AccelerometerDataX, Context.MODE_WORLD_READABLE);
            outputStreamY = openFileOutput(AccelerometerDataY, Context.MODE_WORLD_READABLE);
            outputStreamZ = openFileOutput(AccelerometerDataZ, Context.MODE_WORLD_READABLE);
            do{
               Double tempX = ArrayX[x].getY();
                Double tempY = ArrayY[x].getY();
                Double tempZ = ArrayZ[x].getY();
               outputStreamX.write(tempX.toString().getBytes());
               outputStreamX.write(newLine.getBytes());
                outputStreamY.write(tempY.toString().getBytes());
                outputStreamY.write(newLine.getBytes());
                outputStreamZ.write(tempZ.toString().getBytes());
                outputStreamZ.write(newLine.getBytes());

                x++;
            } while(x < 600);
            outputStreamX.close();
            outputStreamY.close();
            outputStreamZ.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //type this to get the file
        //adb pull /data/data/com.sunshine.coldashes.perfectform/files/AccelerometerData /home/ronald/Desktop
        String filepath = fileX.getPath();
        Toast thePath = Toast.makeText(this, filepath, Toast.LENGTH_LONG);
        thePath.show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int num) {

    }
}