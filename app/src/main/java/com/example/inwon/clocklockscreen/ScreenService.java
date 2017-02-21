package com.example.inwon.clocklockscreen;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by inwon on 2017-02-17.
 */

public class ScreenService extends Service{

    private ScreenReceiver receiver = null;
    private String text = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(receiver,filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if(intent != null){
            if(intent.getAction()==null){
                registerRestartAlarm(true);
                startForeground(1,new Notification());
                receiver = new ScreenReceiver();
                IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
                registerReceiver(receiver,filter);
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            unregisterReceiver(receiver);
        }
        registerRestartAlarm(false);
    }

    /**
     *
     * @param isOn
     * 30분에 한번씩 알람깨우기
     * 서비스 죽지않게 하기
     */
    public void registerRestartAlarm(boolean isOn){
        Intent intent = new Intent(ScreenService.this,RestartReceiver.class);
        intent.setAction(RestartReceiver.ACTION_RESTART_SERVICE);
        PendingIntent sender = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        AlarmManager am =(AlarmManager)getSystemService(ALARM_SERVICE);
        if(isOn){
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+1000,300000,sender);
        }else {
            am.cancel(sender);
        }
    }
}
