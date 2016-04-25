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
 * Created by ronald on 4/7/16.
 */
public class MainActivity extends AppCompatActivity {
    private EditText zipcode_edittext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        zipcode_edittext = (EditText) findViewById(R.id.zipcode_edittext);


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

    public void changeToWeather (View view) {
        String theZip = zipcode_edittext.getText().toString();

        //Toast testzip = Toast.makeText(getApplicationContext(),theZip,Toast.LENGTH_LONG);
        //testzip.show();

        theZip+=",us&appid=2900f6dcae9280512952aac3a316d4b0";
        Intent i = new Intent(getApplicationContext(), WeatherActivity.class);
        i.putExtra("zip",theZip);
        startActivity(i);
    }
}
