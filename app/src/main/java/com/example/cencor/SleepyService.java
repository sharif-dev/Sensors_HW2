package com.example.cencor;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.Nullable;

public class SleepyService extends Service implements SensorEventListener {

    float zAcc;
    float zPreAcc;

    boolean enable = false;

    float sleepThreshold = 1;

    Sensor accelerometer;
    SensorManager sm;

    private final IBinder connectBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        SleepyService getService(){
            return SleepyService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return connectBinder;
    }


    public void setEnable(){
        enable = true;
    }

    public void setUnable(){
        enable = false;
    }

    public boolean getEnable(){
        return enable;
    }

    public void setSleepThreshold(int threshold){
        sleepThreshold = 11 - threshold/10;
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
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        devicePolicyManager.lockNow();
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
