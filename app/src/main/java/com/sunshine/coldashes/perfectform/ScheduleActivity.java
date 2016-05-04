package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ronald on 4/9/16.
 */
public class ScheduleActivity extends Activity {

    private TextView start_time_textview;
    private int hours;
    private int minutes;
    Calendar calendar;
    TimePicker start_time_picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        start_time_textview = (TextView) findViewById(R.id.start_time_textview);

        start_time_picker = (TimePicker) findViewById(R.id.start_time_picker);

        //sets the time to default 12:15
        updateDisplay(12, 15);

        // creates a listener to adjust when the time picker is interacted with
        start_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;
                // updates textview so string is formatted correctly
                updateDisplay(hourOfDay, minute);
            }
        });
    }

    // updates textview so string is formatted correctly
    private void updateDisplay(int hourOfDay, int minute) {
        start_time_textview.setText(new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)));

    }

    // makes sure time is formatted :0X
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    // sets the calendar to the time specified by the user and delays notification
    public void schedule(View view) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, start_time_picker.getCurrentHour());
        calendar.set(Calendar.MINUTE, start_time_picker.getCurrentMinute());

        // time in millis is used for the delay
        scheduleNotification(getNotification("Hope your ready!"), calendar.getTimeInMillis());

        String TimeToRun = start_time_textview.getText().toString();

        // when scheduled run wait till the notification
        Intent i = new Intent(getApplicationContext(), WaitingActivity.class);
        i.putExtra("TimeToRun",TimeToRun);
        startActivity(i);
    }

    private void scheduleNotification(Notification notification, long delay) {

        // build the notification
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // sets the alarm based off the delay of the calendar and subtract 15 second bc
        // calendar in millis was not as accurate as would have hoped
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, delay - 15000, pendingIntent);
    }

    private Notification getNotification(String content) {
        // create the pending intent that will take the user to run activity when notification is clicked
        Intent intent_main_activity = new Intent(this.getApplicationContext(), ReadyToRunActivity.class);
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, PendingIntent.FLAG_UPDATE_CURRENT);

        // build the notification for use later
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Time to run!");
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setContentIntent(pending_intent_main_activity);
        builder.setSmallIcon(R.drawable.ic_timetorun);
        return builder.build();
    }
}
