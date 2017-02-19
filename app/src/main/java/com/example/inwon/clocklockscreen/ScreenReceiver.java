package com.example.inwon.clocklockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by inwon on 2017-02-17.
 */

public class ScreenReceiver extends BroadcastReceiver{
    private TelephonyManager telephonyManager = null;
    private boolean isTelePhone = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            if (telephonyManager == null) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

            if(isTelePhone){ // true
                Intent i = new Intent(context, LockScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        }


    }
            // 전화 상태 check
    private PhoneStateListener phoneListener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber){
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE :
                    isTelePhone = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING :
                    isTelePhone = false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK :
                    isTelePhone = false;
                    break;
            }
        }
    };
}
