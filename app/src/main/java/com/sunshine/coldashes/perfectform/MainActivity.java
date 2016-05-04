package com.sunshine.coldashes.perfectform;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by ronald on 4/7/16 waits for user to enter zip
 * and passes to weather class on button press.
 */
public class MainActivity extends AppCompatActivity {
    private EditText zipcode_edittext;  // used to recieve zip

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup toolbar setting overflow is for shortcuts to activities for debug
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // finds the edittext so can gettext later
        zipcode_edittext = (EditText) findViewById(R.id.zipcode_edittext);

        getSupportActionBar().setTitle("Running Buddy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // creates the menu to jump to certain activities for debug
        getMenuInflater().inflate(R.menu.appbar, menu);
        String title = "Running Buddy";
        int groupID = Menu.NONE;
        int itemID = Menu.FIRST;
        int order = 103;
        menu.add(groupID,itemID,order,title);
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

    public void changeToWeather (View view) {
        String theZip = zipcode_edittext.getText().toString();
        // passes the zip over with api key
        theZip+=",us&appid=1a026939c8842301f733bbc75d1fae78";
        Intent i = new Intent(getApplicationContext(), WeatherActivity.class);
        i.putExtra("zip",theZip);
        startActivity(i);
    }
}
