package com.example.cencor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void AlarmOnClick(View view){
        Intent intent = new Intent(this, AlarmActivity.class);
        startActivity(intent);
    }

    public void ShakyOnClick(View view){
        Intent intent = new Intent(this, ShakyActivity.class);
        startActivity(intent);
    }

    public void SleepyOnclick(View view){
        Intent intent = new Intent(this, SleepyActivity.class);
        startActivity(intent);
    }
}
