package com.gisst.shotgun;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;


public class MobileListenerService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private int sleepy_interval;
    private double speed_tolerance;
    private static final int SPEED_LIMIT_PERIOD = 10000;
    private static final int SPEED_PERIOD = 1000;
    private int lastKnownSpeedLimit = -1;
    private GoogleApiClient mGoogleApiClient;
    private SpeedLimitGetter speedGetter;
    private Location lastKnownLocation;
    private CountDownTimer sleepTimer;
    private CountDownTimer speedTimer;

    private static final String YELLOW = "/yellow";
    private static final String RED = "/red";
    private static final String SLEEP = "/sleep";


    @Override
    public void onConnected(Bundle connectionHint) {
        System.out.println("onConnected");
        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("Connection to location service failed");
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection to location service suspended");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            System.out.println("null found");
            return START_STICKY;
        }
        mGoogleApiClient.connect();
        final Bundle extras = intent.getExtras();
        sleepy_interval = extras.getInt("sleep_interval");
        speed_tolerance = extras.getDouble("speed_tolerance");
        speedGetter = new SpeedLimitGetter();
        createAndStartSleepTimer();
        createAndStartSpeedTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (sleepTimer != null) {
            sleepTimer.cancel();
        }
        if (speedTimer != null) {
            speedTimer.cancel();
        }
        super.onDestroy();
    }


    private void createAndStartSleepTimer() {
        sleepTimer = new CountDownTimer(sleepy_interval * 3600 * 1000, 10000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                sendMessage(SLEEP, "1");
                createAndStartSleepTimer();
            }
        };
        if (sleepy_interval > 0) {
            sleepTimer.start();
        }
    }

    private void createAndStartSpeedTimer() {
        speedTimer = new CountDownTimer(SPEED_LIMIT_PERIOD, SPEED_PERIOD) {
            @Override
            public void onTick(long millisUntilFinished) {
                // TODO: get location from api using getLastLocation() and get current speed
                lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (lastKnownLocation == null) {
                    return;
                }

                double lat = lastKnownLocation.getLatitude();
                double lon = lastKnownLocation.getLongitude();

                int curr_speed = speedGetter.get_curr_speed(lat, lon);
                int max_tolerance = (int) (lastKnownSpeedLimit * (100 + speed_tolerance) / 100);
                if (curr_speed != -1 && lastKnownSpeedLimit != -1) {
                    if (curr_speed > lastKnownSpeedLimit && curr_speed < max_tolerance) {
                        sendMessage(YELLOW, "1");
                    } else if (curr_speed >= max_tolerance) {
                        sendMessage(RED, "1");
                    }
                }

            }

            @Override
            public void onFinish() {
                // TODO: get location from api using getLastLocation() and get current speed limit
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(
                                mGoogleApiClient);

                        if (lastKnownLocation == null) {
                            return;
                        }

                        double lat = lastKnownLocation.getLatitude();
                        double lon = lastKnownLocation.getLongitude();
                        lastKnownSpeedLimit = speedGetter.get_speed_limit(lat, lon);
                    }
                }).start();
                createAndStartSpeedTimer();
            }
        };
        speedTimer.start();
    }


    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
