package com.example.inwon.clocklockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by inwon on 2017-02-18.
 */

public class RestartReceiver extends BroadcastReceiver{
    static public final String ACTION_RESTART_SERVICE = "Restart";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_RESTART_SERVICE)){
            Intent i = new Intent(context,ScreenService.class);
            context.startService(i);
        }
    }
}
