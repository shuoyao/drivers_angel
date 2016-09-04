package com.gisst.shotgun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.WindowManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sophia on 12/1/15.
 */
public class SleepyActivity extends Activity{
    long[] vibrationPattern = {0, 1000, 1000, 1000, 1000, 1000};
    int repeat = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepy);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON;
        params.screenBrightness = 1;
        getWindow().setAttributes(params);

        Timer vibrationTimer = new Timer();
        vibrationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vibrator.vibrate(vibrationPattern, repeat);
            }
        }, 20);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();
            }
        }, 5000);
//        vibrator.vibrate(vibrationPattern, repeat);


//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), DefaultActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });

    }
}

