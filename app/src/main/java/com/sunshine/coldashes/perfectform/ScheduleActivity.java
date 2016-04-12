package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by ronald on 4/9/16.
 */
public class ScheduleActivity extends Activity {

    private TextView start_time_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        start_time_textview = (TextView) findViewById(R.id.start_time_textview);

        TimePicker start_time_picker = (TimePicker) findViewById(R.id.start_time_picker);
        start_time_picker.setCurrentHour(12);
        start_time_picker.setCurrentMinute(15);

        updateDisplay(12, 15);
        start_time_picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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
        //
    }
}
