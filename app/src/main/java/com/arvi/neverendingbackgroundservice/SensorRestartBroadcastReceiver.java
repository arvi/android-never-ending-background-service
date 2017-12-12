package com.arvi.neverendingbackgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by arvi on 12/11/17.
 */

public class SensorRestartBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = SensorRestartBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Service Stops, let's restart again.");
        context.startService(new Intent(context, SensorService.class));
    }
}
