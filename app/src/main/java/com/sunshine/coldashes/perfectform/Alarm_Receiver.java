package com.sunshine.coldashes.perfectform;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Bobby on 4/23/2016. Unused
 */
public class Alarm_Receiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e("Receiver Message","Confirm");

        //This fetches extra string from the intent
        //This shows the alarm on, alarm off toggle
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("The key is:", get_your_string);

        //These intents fetch the extras from the long
        Integer get_your_song_choice = intent.getExtras().getInt("song_choice");
        Log.e("The song choice is ", get_your_song_choice.toString());


    }
}
