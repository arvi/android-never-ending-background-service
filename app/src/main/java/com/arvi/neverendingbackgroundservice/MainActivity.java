package com.arvi.neverendingbackgroundservice;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    private static final String TAG = MainActivity.class.getSimpleName();

    public Context getCtx() {
        return ctx;
    }

    private Button finishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        finishButton = findViewById(R.id.finishButton);

        ctx = this;
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if( ! isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Test destroy activity");

                // test if force destroy will call activity's ondestroy method and restart the service
                // output: yep, calling destroy and restarting service
                finish();
            }
        });
    }

    private boolean isMyServiceRunning(Class <?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning? " + true + "");
                return true;
            }
        }

        Log.i(TAG, "isMyServiceRunning? " + false + "");
        return false;
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        // ondestroy service not being called
        stopService(mServiceIntent);

        super.onDestroy();
    }
}
