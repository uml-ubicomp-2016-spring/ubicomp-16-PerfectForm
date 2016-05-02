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
    private NotificationManager mNotificationManager;
    private int hours;
    private int minutes;
    AlarmManager alarm_manager;
    Calendar calendar;
    TimePicker start_time_picker;
    PendingIntent pending_intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        start_time_textview = (TextView) findViewById(R.id.start_time_textview);

        start_time_picker = (TimePicker) findViewById(R.id.start_time_picker);

        updateDisplay(12, 15);
        start_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hours = hourOfDay;
                minutes = minute;
                updateDisplay(hourOfDay, minute);
            }
        });
    }

    private void updateDisplay(int hourOfDay, int minute) {
        start_time_textview.setText(new StringBuilder().append(pad(hourOfDay)).append(":").append(pad(minute)));

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void schedule(View view) {
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, start_time_picker.getCurrentHour());
        calendar.set(Calendar.MINUTE, start_time_picker.getCurrentMinute());
        scheduleNotification(getNotification("Hope your ready!"), calendar.getTimeInMillis());

        String TimeToRun = start_time_textview.getText().toString();

        Intent i = new Intent(getApplicationContext(), WaitingActivity.class);
        i.putExtra("TimeToRun",TimeToRun);
        startActivity(i);
    }

    private void scheduleNotification(Notification notification, long delay) {

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, delay - 15000, pendingIntent);
    }

    private Notification getNotification(String content) {
        Intent intent_main_activity = new Intent(this.getApplicationContext(), ReadyToRunActivity.class);
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Time to run!");
        builder.setContentText(content);
        builder.setAutoCancel(true);
        builder.setContentIntent(pending_intent_main_activity);
        builder.setSmallIcon(R.drawable.ic_timetorun);
        return builder.build();
    }
}
