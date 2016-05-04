package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
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
import java.util.ArrayList;

/**
 * Created by Coldashes on 3/6/2016 used this for testing but not for final prototype.
 */
public class SensorAccelerometer extends Activity implements SensorEventListener {

    private TextView mXValueView, mYValueView, mZValueView, mTimer;
    private EditText mTimeofRun;
    private long lastupdate = 0;
    static int i = 0;
    static int samples = 0;
    static int timesRun = 0;
    static double di = 0;
    static Boolean startRecording = false;

    // used before the arraylist was implemented
    static DataPoint[] ArrayX = new DataPoint[600];
    static DataPoint[] ArrayY = new DataPoint[600];
    static DataPoint[] ArrayZ = new DataPoint[600];

    // when to get another sensor event
    private static final int UPDATE_THRESHOLD = 1000;
    private SensorManager mSensorManager;
    private Sensor mAcellerometer;

    // was used in order to dynamically create depending on lenghth of run
    private ArrayList<Double> listX = new ArrayList<Double>();
    private ArrayList<Double> listY = new ArrayList<Double>();
    private ArrayList<Double> listZ = new ArrayList<Double>();
    private int ticks = 0;
    private float points = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        // retrieving text fields that would be updated
        mTimeofRun = (EditText) findViewById(R.id.timeofrun);
        mXValueView = (TextView) findViewById(R.id.x_value_view);
        mYValueView = (TextView) findViewById(R.id.y_value_view);
        mZValueView = (TextView) findViewById(R.id.z_value_view);
        mTimer = (TextView) findViewById(R.id.timer_textview);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // make sure device has accelerometer
        if (null == (mAcellerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)))
            finish();

        // get acces to the buttons
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

        // makes sure that when user comes back to the app record data again
        mSensorManager.registerListener(this, mAcellerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        // when user puts this in background dont keep polling sensor
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //any time the sensor event is recived and recording the data store it
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION && startRecording) {
            long actualTime = System.currentTimeMillis();

            //if 1 second has not passed and have < 50 samples
            double x = event.values[0], y = event.values[1], z = event.values[2];

            // update the text
            mXValueView.setText(String.valueOf(x));
            mYValueView.setText(String.valueOf(y));
            mZValueView.setText(String.valueOf(z));

            // make sure only getting 16 samples a second
            if (samples < 16) {
                listX.add(x);
                listY.add(y);
                listZ.add(z);
                // if at 16 dont get anymore till pass one second since last update
            } else if (samples == 16 && (actualTime - lastupdate) > UPDATE_THRESHOLD) {
                lastupdate = actualTime;
                samples = -1;
            }

            samples++;
        }
    }

    private double highPass(double current, double gravity) {
        return current - gravity;
    }

    // puts the data into the grapview does not work since moved to arraylist
    private void displayGraph(int num) {
        GraphView graphX = (GraphView) findViewById(R.id.graphX);
        GraphView graphY = (GraphView) findViewById(R.id.graphZ);
        GraphView graphZ = (GraphView) findViewById(R.id.graphY);

        if (num == 0) {
            LineGraphSeries<DataPoint> seriesX = new LineGraphSeries<DataPoint>(ArrayX);
            LineGraphSeries<DataPoint> seriesY = new LineGraphSeries<DataPoint>(ArrayY);
            LineGraphSeries<DataPoint> seriesZ = new LineGraphSeries<DataPoint>(ArrayZ);

            // used to be able to click on point and see data but was not needed in actual prototype
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
            // removes the data from the graph
            i = 0;
            di = 0;
            graphX.removeAllSeries();
            graphY.removeAllSeries();
            graphZ.removeAllSeries();
        }
    }

    public void startButton(View view) {
        startRecording = true;
        int timercount = Integer.parseInt(mTimeofRun.getText().toString());
        // used to have to know how many datapoints would be needed
        // so asked user for how long they wanted to run not an issue in prototype
        new CountDownTimer(timercount * 1000, 1000) {

            // everytime a minute passed check the data and decide if they are doing well
            public void onTick(long millisUntilFinished) {
                mTimer.setText("seconds remaining: " + millisUntilFinished / 1000);
                ticks++;
                if (ticks % 60 == 0) {
                    int size = listX.size();
                    boolean perfect = true;
                    // didnt work but fixed it in the run activity needed 0 to be i so that would
                    //check new data every minute
                    int i = 0 * 960; // 960 = 16 samples/second * 60 seconds/min
                    while (i < size) {
                        if (listX.get(i) < -20) {
                            perfect = false;
                            points -= .01;
                        } else if (listX.get(i) > 35) {
                            perfect = false;
                            points -= .01;
                        }
                        if (listY.get(i) < -20) {
                            perfect = false;
                            points -= .01;
                        } else if (listY.get(i) > 30) {
                            perfect = false;
                            points -= .01;
                        }
                        if (listZ.get(i) < -20) {
                            perfect = false;
                            points -= .01;
                        } else if (listZ.get(i) > 30) {
                            perfect = false;
                            points -= .01;
                        }
                        i++;
                    }
                    // no values over thresholds then you gained a point
                    if (perfect)
                        points += 1;
                }
            }

            public void onFinish() {
                mTimer.setText(String.valueOf(points));
                // when timer ran out vibrate and stop saving data
                startRecording = false;
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1000);
            }
        }.start();

    }

    public void stopButton(View view) {
        // change bool to stop recording
        startRecording = false;
    }


    public void next(View view) {
        Intent myIntent = new Intent(this, LightSensor.class);
        startActivity(myIntent);
    }

    public void save(View view) {
        timesRun++;
        //name to saved the data under
        String AccelerometerDataX = "AccelerometerDataX" + timesRun;
        String AccelerometerDataY = "AccelerometerDataY" + timesRun;
        String AccelerometerDataZ = "AccelerometerDataZ" + timesRun;

        // create the files for the datapoints
        File fileX = new File(getApplicationContext().getFilesDir(), AccelerometerDataX);
        fileX.setReadable(true, false);

        File fileY = new File(getApplicationContext().getFilesDir(), AccelerometerDataY);
        fileY.setReadable(true, false);

        File fileZ = new File(getApplicationContext().getFilesDir(), AccelerometerDataZ);
        fileZ.setReadable(true, false);

        int x = 0;
        String newLine = "\n";
        try {
            //open a streams
            FileOutputStream outputStreamX;
            FileOutputStream outputStreamY;
            FileOutputStream outputStreamZ;
            outputStreamX = openFileOutput(AccelerometerDataX, Context.MODE_WORLD_READABLE);
            outputStreamY = openFileOutput(AccelerometerDataY, Context.MODE_WORLD_READABLE);
            outputStreamZ = openFileOutput(AccelerometerDataZ, Context.MODE_WORLD_READABLE);
            do {
                // save the data from arraylist to temp vars then write them to files
                Double tempX = listX.get(x);
                Double tempY = listY.get(x);
                Double tempZ = listZ.get(x);
                outputStreamX.write(tempX.toString().getBytes());
                outputStreamX.write(newLine.getBytes());
                outputStreamY.write(tempY.toString().getBytes());
                outputStreamY.write(newLine.getBytes());
                outputStreamZ.write(tempZ.toString().getBytes());
                outputStreamZ.write(newLine.getBytes());
                x++;
                //till done
            } while (x < listX.size());
            //close the streams
            outputStreamX.close();
            outputStreamY.close();
            outputStreamZ.close();
        } catch (Exception e) {
            // if theres an issue print stacktrace
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
