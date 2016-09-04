package com.gisst.shotgun;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DrivingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            int sleep_interval = extras.getInt("sleep");
            double speed_tolerance = extras.getDouble("speed");
            boolean firstBool = extras.getBoolean("firstbool");
            boolean secondBool = extras.getBoolean("secondbool");

            final TextView sleep = (TextView) findViewById(R.id.sleep_interval);
            sleep.setText(Integer.toString(sleep_interval) + "Hrs");

            final TextView speed = (TextView) findViewById(R.id.speed_tolerance);
            speed.setText(Double.toString(speed_tolerance) + "%");

            final TextView onOff1 = (TextView)findViewById(R.id.text_message_status);
            final TextView onOff2 = (TextView)findViewById(R.id.media_status);

            if (firstBool) {
                onOff1.setText("ON");
            } else {
                onOff1.setText("OFF");
            }

            if (secondBool) {
                onOff2.setText("ON");
            } else {
                onOff2.setText("OFF");
            }
        }
        final Button end_button = (Button) findViewById(R.id.end_button);
        end_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(getApplicationContext(), MobileListenerService.class);
                stopService(serviceIntent);

                Intent homeIntent= new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            }
        });
    }

}
