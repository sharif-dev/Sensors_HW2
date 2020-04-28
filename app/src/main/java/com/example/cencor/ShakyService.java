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

public class ShakyService extends Service implements SensorEventListener {

    float xAcc, yAcc, zAcc;
    float xPreAcc, yPreAcc, zPreAcc;

    boolean enable = false;
    boolean firstUpdate = true;
    boolean shakeInitiated = false;

    float shakeThreshold = 12.5f;

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
        updateAccelParameters(event.values[0], event.values[1], event.values[2]);

        if(!shakeInitiated && isAccelerationChanged() && enable){
            shakeInitiated = true;
        } else if (shakeInitiated && isAccelerationChanged() && enable)
        {
            executeShakeAction();
        }
    }

    private void executeShakeAction() {
        PowerManager powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");

        //acquire will turn on the display
        wakeLock.acquire();

        //release will release the lock from CPU, in case of that, screen will go back to sleep mode in defined time bt device settings
        wakeLock.release();
    }

    private boolean isAccelerationChanged() {

        float deltaX = Math.abs(xPreAcc - xAcc);
        float deltaY = Math.abs(yPreAcc - yAcc);
        float deltaZ = Math.abs(zPreAcc - zAcc);

        return ((deltaX > shakeThreshold && deltaY > shakeThreshold) || (deltaZ > shakeThreshold && deltaY > shakeThreshold) || (deltaX > shakeThreshold && deltaZ > shakeThreshold));
    }

    private void updateAccelParameters(float xNewAcc, float yNewAcc, float zNewAcc) {
        if(firstUpdate)
        {
            xPreAcc = xNewAcc;
            yPreAcc = yNewAcc;
            zPreAcc = zNewAcc;
            firstUpdate = false;
        }else{
            xPreAcc = xAcc;
            yPreAcc = yAcc;
            zPreAcc = zAcc;
        }

        xAcc = xNewAcc;
        yAcc = xNewAcc;
        zAcc = zNewAcc;
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
