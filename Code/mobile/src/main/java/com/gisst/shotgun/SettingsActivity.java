package com.gisst.shotgun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;


//Customize notification settings, start MobileDetectionService
public class SettingsActivity extends Activity {
    private int sleep_interval;
    private double speed_tolerance;
    private boolean firstBool = true;
    private boolean secondBool = true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        SeekBar sleepSeekBar = (SeekBar)findViewById(R.id.sleep_alert);

        final Switch switch1 = (Switch)findViewById(R.id.mySwitch1);
        final Switch switch2 = (Switch)findViewById(R.id.mySwitch2);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                firstBool = isChecked;
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                secondBool = isChecked;
            }
        });


        final TextView sleepBarValue = (TextView)findViewById(R.id.sleep);
        sleepSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sleep_interval = progress;
                sleepBarValue.setText(String.format("Every %d hrs", progress ));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        SeekBar speedSeekBar = (SeekBar)findViewById(R.id.speed_alert);
        final TextView speedBarValue = (TextView)findViewById(R.id.speed);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                speed_tolerance = progress;
                speedBarValue.setText(String.valueOf(speed_tolerance) + " %");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        final Button start_button = (Button) findViewById(R.id.start_button);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getApplicationContext(), MobileListenerService.class);
                serviceIntent.putExtra("speed_tolerance", speed_tolerance);
                serviceIntent.putExtra("sleep_interval", sleep_interval);
                startService(serviceIntent);

                // should be DrivingActivity, change it to VoiceActivity for testing
                Intent intent = new Intent(getApplicationContext(), VoiceActivity.class);
                intent.putExtra("sleep", sleep_interval);
                intent.putExtra("speed", speed_tolerance);
                intent.putExtra("firstbool", firstBool);
                intent.putExtra("secondbool", secondBool);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
