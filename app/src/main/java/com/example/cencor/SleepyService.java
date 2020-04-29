package com.example.cencor;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

public class SleepyService extends Service implements SensorEventListener {

    float zAcc;
    float zPreAcc;

    boolean enable = false;

    float sleepThreshold = 2;

    Sensor accelerometer;
    SensorManager sm;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        enable = true;
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        zAcc = event.values[2];

        if (isFacedDownwards() && enable)
        {
            executeSleepAction();
        }
    }

    private void executeSleepAction() {
        PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

        wakeLock.acquire();

        wakeLock.release();
    }

    private boolean isFacedDownwards() {

        return (zAcc + 10 < sleepThreshold);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onDestroy() {
        enable = false;
        super.onDestroy();
    }
}
