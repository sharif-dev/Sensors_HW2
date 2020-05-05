package com.example.cencor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

public class ShakyActivity extends AppCompatActivity {

    Intent intent;
    ShakyService shakyService;
    boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shaky);

        intent = new Intent(ShakyActivity.this, ShakyService.class);
        bindService(intent, shakyConnection, Context.BIND_AUTO_CREATE);

        final SeekBar sk = (SeekBar) findViewById(R.id.shakySeekBar);

        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();
                shakyService.setShakeThreshold(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Switch sb = (Switch) findViewById(R.id.switchShaky);

        sb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    shakyService.setEnable();
                }else{
                    shakyService.setUnable();
                }
            }
        });
    }

    private ServiceConnection shakyConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ShakyService.LocalBinder binder = (ShakyService.LocalBinder) service;
            shakyService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };
}
