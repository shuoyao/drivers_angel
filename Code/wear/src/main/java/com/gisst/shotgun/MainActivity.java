package com.gisst.shotgun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);
//        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
//        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
//            @Override
//            public void onLayoutInflated(WatchViewStub stub) {
//                mTextView = (TextView) stub.findViewById(R.id.text);
//            }
//        });
        Intent intent = new Intent(getBaseContext(), WatchListenerService.class);
        startService(intent);
        finish();
//        ImageView imageView = (ImageView) findViewById(R.id.main);
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
