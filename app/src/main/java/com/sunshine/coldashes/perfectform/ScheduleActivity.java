package com.sunshine.coldashes.perfectform;

import android.app.Activity;
<<<<<<< HEAD
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
=======
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
>>>>>>> origin/master

/**
 * Created by ronald on 4/9/16.
 */
public class ScheduleActivity extends Activity {

    private TextView start_time_textview;
<<<<<<< HEAD
    private NotificationManager mNotificationManager;
    private int hours;
    private int minutes;
    private Chronometer chrono;
=======

>>>>>>> origin/master
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

<<<<<<< HEAD
        chrono = (Chronometer) findViewById(R.id.timer_chrono);
        start_time_textview = (TextView) findViewById(R.id.start_time_textview);
        Intent intent = new Intent(this, MainActivity.class);
=======
        start_time_textview = (TextView) findViewById(R.id.start_time_textview);
>>>>>>> origin/master

        TimePicker start_time_picker = (TimePicker) findViewById(R.id.start_time_picker);
        start_time_picker.setCurrentHour(12);
        start_time_picker.setCurrentMinute(15);

        updateDisplay(12, 15);
        start_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
<<<<<<< HEAD
                hours = hourOfDay;
                minutes = minute;
=======
>>>>>>> origin/master
                updateDisplay(hourOfDay, minute);
            }
        });
    }

    private void updateDisplay(int hourOfDay, int minute) {
        start_time_textview.setText(new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)));
<<<<<<< HEAD

=======
>>>>>>> origin/master
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void schedule(View view) {
<<<<<<< HEAD

        Intent resultIntent = new Intent(this, WaitingActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        long futureInMillis = SystemClock.elapsedRealtime() + 100000;

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, resultPendingIntent);

        String TimeToRun = start_time_textview.getText().toString();

        Intent i = new Intent(getApplicationContext(), WaitingActivity.class);
        i.putExtra("TimeToRun",TimeToRun);
        startActivity(i);
=======
        //
>>>>>>> origin/master
    }
}
