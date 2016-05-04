package com.sunshine.coldashes.perfectform;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sunshine.coldashes.perfectform.Alarm_Receiver;
import com.sunshine.coldashes.perfectform.R;

import java.util.Calendar;

// made by bobby unused but basis for schedule acitivity
public class ScheduleAlarm extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //Create alarm manager
    AlarmManager alarm_manager;
    TimePicker alarm_timepicker;
    TextView update_text;
    Context context;
    PendingIntent pending_intent;
    int choose_song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        this.context = this;

        //initializing Alarm manager
        alarm_manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        //initializing text update box
        update_text = (TextView) findViewById(R.id.update_text);

        //initializing timepicker
        alarm_timepicker = (TimePicker) findViewById(R.id.timePicker);

        //create an instance of a calendar
        final Calendar calendar = Calendar.getInstance();

        //create an intent to the alarm receiver class
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);



        //initialize start button
        Button alarm_on = (Button) findViewById(R.id.alarm_on);
        //create an onClick listener to start the alarm

        assert alarm_on != null;

        alarm_on.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //setting calendar instance with the hour and minute the runner picked
                // on the time picker
                calendar.set(Calendar.HOUR_OF_DAY, alarm_timepicker.getHour());
                calendar.set(Calendar.MINUTE, alarm_timepicker.getMinute());

                //get the string values of the hour and minute
                int hour = alarm_timepicker.getHour();
                int minute = alarm_timepicker.getMinute();

                //convert the int values to strings
                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                //convert 24-hour time to 12-hour time
                if (hour > 12) {
                    hour_string = String.valueOf(hour - 12);
                }

                if (minute < 10) {
                    //minutes cannot be greater than :59
                    minute_string = "0" + String.valueOf(minute);
                }

                //method that changes the update text Textbox
                set_alarm_text("Alarm set to: " + hour_string + ":" + minute_string);

                //put in extra string into my_intent
                //tells the clock that you pressed the "alarm on" button
                my_intent.putExtra("extra", "alarm on");

                //put in an extra long my_intent
                //tells the clock that you want a certain value from the drop down menu/spinner
                my_intent.putExtra("song_choice", String.valueOf(choose_song));
                Log.e("The song id is", String.valueOf(choose_song));

                //create a pending intent that delays the intent
                pending_intent = PendingIntent.getBroadcast(ScheduleAlarm.this, 0, my_intent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
            }
        });

        Button alarm_off = (Button) findViewById(R.id.alarm_off);

        assert alarm_off != null;
        alarm_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //This method changes & updates the text box
                set_alarm_text("Alarm off!");
                //This cancels the alarm
                alarm_manager.cancel(pending_intent);

                //tells clock you pressed the "alarm off" button
                my_intent.putExtra("extra","alarm off");
                my_intent.putExtra("song choice",choose_song);

                //End the ringtone
                sendBroadcast(my_intent);
            }
        });


    }

    private void set_alarm_text(String output) {

        update_text.setText(output);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)

        //Output whatever user id selects
        choose_song = (int) id;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}
