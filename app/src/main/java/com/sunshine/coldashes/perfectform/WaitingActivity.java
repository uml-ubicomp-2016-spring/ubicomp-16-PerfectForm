package com.sunshine.coldashes.perfectform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by ronald on 4/7/16 opens up while it is not time to run.
 */
public class WaitingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        // setup toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //recieve the time the run should begin
        Bundle bundle = getIntent().getExtras();
        String scheduledTime = bundle.getString("TimeToRun");

        //set the text that the run will begin
        TextView scheduled = (TextView) findViewById(R.id.scheduledtime_textview);

        // make sure not to get nullpointer exception when getting text
        try {
        String temp = scheduled.getText().toString();
        temp += scheduledTime;
        scheduled.setText(temp);
            getSupportActionBar().setTitle("Running Buddy");
        } catch(NullPointerException e) {
            Log.d("WaitingActivity:","NullPointerException getSupportActionBar");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.appbar, menu);
        String title = "Running Buddy";
        int groupID = Menu.NONE;
        int itemID = Menu.FIRST;
        int order = 103;
        menu.add(groupID, itemID, order, title);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle overflow menu
        switch (item.getItemId()) {
            // jump to running activity
            case R.id.action_run:
                Intent newIntent = new Intent(this, run.class);
                startActivity(newIntent);
                return true;
            // set schedule run activity schedules alarm
            case R.id.action_alarm:
                Intent alarmIntent = new Intent(this, ScheduleAlarm.class);
                startActivity(alarmIntent);
                return true;
            // analyze has all sensors we used for testing purposes
            case R.id.action_analyze:
                Intent myIntent = new Intent(this, SensorAccelerometer.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void Reschedule(View view) {
        // change to the schedule activity
        Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
        startActivity(i);
    }
}
