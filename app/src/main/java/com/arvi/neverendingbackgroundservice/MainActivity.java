package com.arvi.neverendingbackgroundservice;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREF), MODE_PRIVATE);

        if("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER) && ! sharedPreferences.getBoolean(getString(R.string.PREF_IS_PROTECTED_APPS), false)) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(this);
            builder.setTitle(R.string.huawei_title).setMessage(R.string.huawei_text)
                    .setPositiveButton(R.string.go_to_protected, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                            startActivity(intent);
                            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.SHARED_PREF), MODE_PRIVATE).edit();
                            editor.putBoolean(getString(R.string.PREF_IS_PROTECTED_APPS),true).commit();
                        }
                    }).create().show();
        }

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
