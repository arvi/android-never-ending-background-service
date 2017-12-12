package com.arvi.neverendingbackgroundservice;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by arvi on 12/11/17.
 */

public class SensorService extends Service {
    private Context ctx;
    TimerCounter tc;
    private int counter = 0;
    private static final String TAG = SensorService.class.getSimpleName();

    public SensorService() {

    }

    public SensorService(Context applicationContext) {
        super();
        ctx = applicationContext;
        Log.i(TAG, "SensorService class");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");

        tc = new TimerCounter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand()");


        tc.startTimer(counter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "serviceOnDestroy()");

        super.onDestroy();


        Intent broadcastIntent = new Intent("com.arvi.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        tc.stopTimerTask();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "serviceonTaskRemoved()");
/*
        if("huawei".equalsIgnoreCase(android.os.Build.MANUFACTURER) && !sp.getBoolean("protected",false)) {
            AlertDialog.Builder builder  = new AlertDialog.Builder(this);
            builder.setTitle(R.string.huawei_title).setMessage(R.string.huawei_text)
                    .setPositiveButton(R.string.go_to_protected, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                            startActivity(intent);
                            sp.edit().putBoolean("protected",true).commit();
                        }
                    }).create().show();
        } */

        // workaround for kitkat: set an alarm service to trigger service again
        Intent intent = new Intent(getApplicationContext(), SensorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);

        super.onTaskRemoved(rootIntent);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }
}
