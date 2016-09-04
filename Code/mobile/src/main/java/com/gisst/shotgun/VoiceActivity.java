package com.gisst.shotgun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

//Interact with the phone and answer simple questions to indicate alertness
//This is a feature to-be implemented in the future

public class VoiceActivity extends Activity {
    private GoogleApiClient mApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiClient = new GoogleApiClient.Builder( this ).addApi(Wearable.API).build();
        setContentView(R.layout.activity_voice);
        ImageView imageView = (ImageView) findViewById(R.id.voice);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DrivingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
