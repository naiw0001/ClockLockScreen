package com.example.inwon.clocklockscreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by inwon on 2017-02-17.
 */

public class LockScreen extends AppCompatActivity {
    private TextView am, pm;
    private RelativeLayout locklayout;
    private int[] iiten = {R.id.ja, R.id.one, R.id.two, R.id.three, R.id.four, R.id.five1, R.id.five2, R.id.six1, R.id.six2, R.id.seven1, R.id.seven2,
            R.id.eight1, R.id.eight2, R.id.nine1, R.id.nine2, R.id.ten, R.id.han, R.id.du};
    private int[] iimin_units = {R.id.ill, R.id.yee2, R.id.sam2, R.id.sa2, R.id.oh3, R.id.yuk, R.id.chil, R.id.pal, R.id.gu};
    private int[] iimin_tens = {R.id.sip, R.id.yee, R.id.sam, R.id.sa, R.id.oh};
    private TextView[] tenstext;
    private TextView si, bun;
    private TextView[] mintext_units, mintext_tens;
    private String hour, min;
    private Date date;
    private TextView ja, jung, oh;
    private Thread thread;
    private boolean threadrun = true;
    private ImageView img;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lockscreen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        locklayout = (RelativeLayout) findViewById(R.id.locklayout);
        locklayout.setOnTouchListener(locktouch);

        SharedPreferences pref = getSharedPreferences("locksceen_clock",MODE_PRIVATE);
        int issw = pref.getInt("issw",0);
        String imgpath = getFilesDir().getAbsolutePath()+"/back_img";
        img = (ImageView)findViewById(R.id.img);

        if(issw == 1){
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            img.setImageBitmap(bm);
        }else {
            locklayout.setBackgroundColor(Color.TRANSPARENT);
            img.setImageBitmap(null);
        }

        tenstext = new TextView[iiten.length];

        for (int i = 0; i < iiten.length; i++) {
            tenstext[i] = (TextView) findViewById(iiten[i]);
        }
        mintext_units = new TextView[iimin_units.length];
        mintext_tens = new TextView[iimin_tens.length];

        for (int i = 0; i < iimin_units.length; i++) {
            mintext_units[i] = (TextView) findViewById(iimin_units[i]);
        }

        for (int i = 0; i < iimin_tens.length; i++) {
            mintext_tens[i] = (TextView) findViewById(iimin_tens[i]);
        }
        date = new Date(System.currentTimeMillis());

        hour = getHour();
        min = getMin();

        ja = (TextView) findViewById(R.id.ja);
        jung = (TextView) findViewById(R.id.jung);
        oh = (TextView) findViewById(R.id.oh2);

        si = (TextView) findViewById(R.id.si);
        bun = (TextView) findViewById(R.id.bun);
        si.setTextColor(Color.BLUE);

        am = (TextView) findViewById(R.id.AM);
        pm = (TextView) findViewById(R.id.PM);
        setHour();

        final Handler handler = new Handler();
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (threadrun) {
                    date = new Date(System.currentTimeMillis());
                    hour = getHour();
                    min = getMin();
                    Log.i("aaahour",hour);
                    Log.i("aaamin",min);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setHour();
                        }
                    });
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
                }
            }
        });
        thread.start();

    }

    private void setHour() {
        int ihour = Integer.parseInt(hour);
        if (ihour >= 00 && ihour <= 12) {
            am.setTextColor(Color.BLUE);
            pm.setTextColor(Color.GRAY);
        } else if (ihour >= 12 && ihour <= 24) {
            pm.setTextColor(Color.BLUE);
            am.setTextColor(Color.GRAY);
        }
        if (hour.equals("12") && min.equals("00")) {
            ja.setTextColor(Color.BLUE);
            jung.setTextColor(Color.BLUE);
            oh.setTextColor(Color.GRAY);
            si.setTextColor(Color.GRAY);
            bun.setTextColor(Color.GRAY);
        } else if (hour.equals("24") && min.equals("00")) {
            jung.setTextColor(Color.BLUE);
            oh.setTextColor(Color.BLUE);
            ja.setTextColor(Color.GRAY);
            si.setTextColor(Color.GRAY);
            bun.setTextColor(Color.GRAY);
        } else {
            ja.setTextColor(Color.GRAY);
            jung.setTextColor(Color.GRAY);
            oh.setTextColor(Color.GRAY);
            si.setTextColor(Color.BLUE);
            bun.setTextColor(Color.BLUE);
            for (int i = 0; i < 13; i++) { //0~12
                int i_tens = Integer.parseInt(hour);
                if (i_tens >= 12) i_tens -= 12;
                if (i_tens == 12 || i_tens == 00) {
                    inithourtext();
                    tenstext[15].setTextColor(Color.BLUE);
                    tenstext[17].setTextColor(Color.BLUE);
                    break;
                }
                if (i_tens == i) {
                    inithourtext();
                    if (i_tens < 5) {
                        tenstext[i].setTextColor(Color.BLUE);
                        break;
                    } else if (i_tens >= 5) { //5시
                        if (i_tens == 10) {
                            tenstext[15].setTextColor(Color.BLUE);
                            break;
                        } else if (i_tens == 11) {
                            tenstext[15].setTextColor(Color.BLUE);
                            tenstext[16].setTextColor(Color.BLUE);
                            break;
                        } else if (i_tens == 12) {
                            tenstext[15].setTextColor(Color.BLUE);
                            tenstext[17].setTextColor(Color.BLUE);
                            break;
                        } else {
                            int a = i_tens - 5;
                            tenstext[i + a].setTextColor(Color.BLUE);
                            tenstext[i + a + 1].setTextColor(Color.BLUE);
                            break;
                        }
                    }
                }
            }

            setMin();
        }

    }

    private void setMin() {
        String min_ten = min.substring(0, 1);
        String min_unit = min.substring(1, min.length());
        int imin_ten = Integer.parseInt(min_ten);
        int imin_unit = Integer.parseInt(min_unit);
        for (int i = 0; i <= 5; i++) {
            if (imin_ten == i) {
                initmintext();
                if (imin_ten == 0) {
                    break;
                } else if (imin_ten == 5) {
                    mintext_tens[4].setTextColor(Color.BLUE);
                    mintext_tens[0].setTextColor(Color.BLUE);
                    break;
                } else if (imin_ten == 1) {
                    mintext_tens[0].setTextColor(Color.BLUE);
                    break;
                } else {
                    mintext_tens[0].setTextColor(Color.BLUE);
                    mintext_tens[i - 1].setTextColor(Color.BLUE);
                    break;
                }
            }
        }
        for (int j = 0; j <= 9; j++) {
            bun.setTextColor(Color.BLUE);
            initmin_utext();
            if (imin_unit == j) {
                if (imin_unit == 0) {
                    if(imin_ten==0) {
                        bun.setTextColor(Color.GRAY);
                    }
                    break;
                } else if (imin_unit == 9) {
                    mintext_units[8].setTextColor(Color.BLUE);
                    break;
                } else {
                    mintext_units[j - 1].setTextColor(Color.BLUE);
                    break;
                }
            }
        }
    }

    private void inithourtext() {
        for (int i = 0; i < iiten.length; i++) {
            tenstext[i].setTextColor(Color.GRAY);
        }
    }

    private void initmintext() {
        for (int i = 0; i < iimin_tens.length; i++) {
            mintext_tens[i].setTextColor(Color.GRAY);
        }
    }

    private void initmin_utext() {
        for (int i = 0; i < iimin_units.length; i++) {
            mintext_units[i].setTextColor(Color.GRAY);
        }
    }

    private String getHour() {
        SimpleDateFormat format = new SimpleDateFormat("HH");
        String hour = format.format(date);
        return hour;
    }

    private String getMin() {
        SimpleDateFormat format = new SimpleDateFormat("mm");
        String min = format.format(date);
        return min;
    }

    @Override
    public void onBackPressed() {
    }


    float x, y;
    View.OnTouchListener locktouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = event.getX();
                    y = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float xx = event.getX();
                    float yy = event.getY();
                    float lagx, lagy, smx, smy;
                    boolean islagx = (xx < x) ? true : false;
                    boolean islagy = (yy < y) ? true : false;
                    if (islagx) {
                        lagx = x;
                        smx = xx;
                    } else {
                        lagx = xx;
                        smx = x;
                    }

                    if (islagy) {
                        lagy = y;
                        smy = yy;
                    } else {
                        lagy = yy;
                        smy = y;
                    }
                    x = lagx - smx;
                    y = lagy - smy;
                    if (x >= 400 || y >= 500) {
                        threadrun = false;
                        finish();
                        return false;
                    }

                    break;
            }
            return true;
        }
    };


}


