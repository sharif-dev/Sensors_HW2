package com.example.cencor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class AlarmActivity extends AppCompatActivity {

    TimePicker timePicker;
    TextView textView;

    Calendar calAlarm, calNow;

    int hour, min;

    AlarmManager alarmManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        textView = (TextView) findViewById(R.id.timeTextView);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                hour = hourOfDay;
                min = minute;

            }
        });
    }

    public void setTimer(View v){

        textView.setText("Time " + hour + ":" + min);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Date date = new Date();

        calAlarm = Calendar.getInstance();
        calNow = Calendar.getInstance();

        calNow.setTime(date);
        calAlarm.setTime(date);

        calAlarm.set(Calendar.HOUR_OF_DAY, hour);
        calAlarm.set(Calendar.MINUTE, min);
        calAlarm.set(Calendar.SECOND, 0);

        if(calAlarm.before(calNow)){
            calAlarm.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(AlarmActivity.this, AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 69696, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calAlarm.getTimeInMillis(), pendingIntent);
    }

    public void clearTimer(View v){
        textView.setText("Time ");

        Intent intent = new Intent(AlarmActivity.this, AlarmBroadCastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, 69696, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}
