package com.sunshine.coldashes.perfectform;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ronald on 4/7/16.
 */
public class ReadyToRunActivity extends AppCompatActivity {
    private EditText zipcode_edittext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_readytorun);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setTitle("Running Buddy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
            case R.id.action_analyze:
                Intent myIntent = new Intent(this, SensorAccelerometer.class);
                startActivity(myIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void run (View view) {
        // takes to the run activity to start the run
        Intent i = new Intent(getApplicationContext(), run.class);
        startActivity(i);
    }

    public void reschedule (View view) {
        // allows to change time to run
        Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
        startActivity(i);
    }
}
