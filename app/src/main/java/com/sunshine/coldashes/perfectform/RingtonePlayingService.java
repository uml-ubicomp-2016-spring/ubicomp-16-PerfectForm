package com.sunshine.coldashes.perfectform;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sunshine.coldashes.perfectform.MainActivity;
import com.sunshine.coldashes.perfectform.R;

import java.security.Provider;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * Created by Bobby on 4/23/2016. */
public class RingtonePlayingService extends Service{

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i("LocalService","Received Start id" + startId + ":" + intent);

        //This fetches the extra string from the alarm on and off values
        String state = intent.getExtras().getString("extra");
        //This fetches the song choice integer value
        Integer song_choice = intent.getExtras().getInt("song_choice");

        Log.e("Ringtone extra is",state);
        Log.e("The Song choice is",song_choice.toString());

        //Notification
        //Set up the notification service
        NotificationManager notify_manager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        //Set up an intent that goes to the main activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);
        //set up a pending intent
        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        //make the notification parameter
        Notification notification_popup = new Notification.Builder(this)
                .setContentTitle("Time for your scheduled run!")
                .setContentText("Get going on your run!")
                .setContentIntent(pending_intent_main_activity)
                .setAutoCancel(true)
                .build();


        //This converts the extra strings from the intent
        //To start the IDs, values 0 or 1

        assert state != null;
        switch (state){
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is:",state);
                break;
            default:
                startId = 0;
                break;
        }


        if(!this.isRunning && startId == 1) {
            Log.e("There is no sound", "and you want it to start playing");

            this.isRunning = true;
            this.startId = 0;

            //This sets up the start command
            notify_manager.notify(0, notification_popup);
        }
        //If else statements
        //If there is no music playing,and the user pressed "alarm off
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //Tell the user we stopped
        Log.e("on Destroy called","on destroy called");

        super.onDestroy();
        this.isRunning = false;
    }

}


