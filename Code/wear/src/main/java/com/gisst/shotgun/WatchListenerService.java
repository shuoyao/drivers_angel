package com.gisst.shotgun;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by sophia on 12/01/15.
 */

public class WatchListenerService extends WearableListenerService {
    private GoogleApiClient mApiClient;

    //WatchListenerService listens for GPS activity
    private static final String SPEED_ACTIVITY = "/speed_activity";
    private static final String SPEED_EXTREME_ACTIVITY = "/speed_extreme_activity";

    //WatchListenerService listens for Sleep activity
    private static final String SLEEP_ACTIVITY = "/sleep_activity";

    private static final String YELLOW = "/yellow";
    private static final String RED = "/red";
    private static final String SLEEP = "/sleep";

    @Override
    public void onCreate() {
        super.onCreate();
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                    }
                })
                .build();

        mApiClient.connect();

        Wearable.MessageApi.addListener(mApiClient, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiClient.disconnect();
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        //when listens SENSOR_ACTIVITY, start ImageActivity
        if( messageEvent.getPath().equalsIgnoreCase(YELLOW) ) {
            System.out.println("yellow");
            String s_value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, SpeedingActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("S_VALUE", s_value);
            startActivity(intent);
        }

        if( messageEvent.getPath().equalsIgnoreCase(RED) ) {
            String s_value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, ExtremeSpeedingActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("S_VALUE", s_value);
            startActivity(intent);
        }

        if( messageEvent.getPath().equalsIgnoreCase(SLEEP) ) {
            String s_value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, SleepyActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("S_VALUE", s_value);
            startActivity(intent);
        }



    }


}
