package com.gisst.shotgun;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

//Test interaction between phone and watch, by pressing button the phone, and displaying image on the watch
public class TestInteractionActivity extends Activity {

    private GoogleApiClient mApiClient;
    private static final String YELLOW = "/yellow";
    private static final String RED = "/red";
    private static final String SLEEP = "/sleep";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApiClient = new GoogleApiClient.Builder( this ).addApi(Wearable.API).build();
        setContentView(R.layout.activity_test);
        Button yellow_button = (Button) findViewById(R.id.yellow);
        Button red_button = (Button) findViewById(R.id.red);
        Button sleep_button = (Button) findViewById(R.id.sleep);
        yellow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(YELLOW, "0");
            }
        });
        red_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(RED, "1");
            }
        });
        sleep_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(SLEEP, "2");
            }
        });
        mApiClient.connect();
    }

    private void sendMessage( final String path, final String text ) {
        System.out.println("Sending message");
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mApiClient).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            mApiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }
}
