package com.clubscaddy.BackGroundServies;

/**
 * Created by administrator on 2/12/16.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message


        Intent intent1 = new Intent(context , BackgroundService.class);
        context.startService(intent1);
    }
}
