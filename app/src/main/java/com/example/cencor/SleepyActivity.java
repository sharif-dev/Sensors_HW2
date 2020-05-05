package com.example.cencor;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

public class SleepyActivity extends AppCompatActivity{

    private Button lock;
    public static final int RESULT_ENABLE = 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName componentName;
    SleepyService sleepyService;
    boolean isBound = false;

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleepy);

        devicePolicyManager = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        componentName = new ComponentName(this, LockAdmin.class);

        intent = new Intent(SleepyActivity.this, SleepyService.class);
        bindService(intent, sleepyConnection, Context.BIND_AUTO_CREATE);

        final SeekBar sk = (SeekBar) findViewById(R.id.sleepySeekBar);

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();
                sleepyService.setSleepThreshold(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch sb = (Switch) findViewById(R.id.switchSleepy);
        devicePolicyManager.removeActiveAdmin(componentName);
        Intent adminIntent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        adminIntent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        adminIntent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "some explanation about permission :-?");
        startActivityForResult(adminIntent, RESULT_ENABLE);
        sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sleepyService.setEnable();
                }else{
                    sleepyService.setUnable();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(componentName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(SleepyActivity.this, "You have enabled the admin device features", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SleepyActivity.this, "Problem to enable the admin device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private ServiceConnection sleepyConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SleepyService.LocalBinder binder = (SleepyService.LocalBinder) service;
            sleepyService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
