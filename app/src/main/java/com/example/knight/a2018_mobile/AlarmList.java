package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.design.widget.FloatingActionButton;

public class AlarmList extends AppCompatActivity {


    public FloatingActionButton fab;
    public ListView list;
    public AlarmAdapter alarmAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        alarmAdapter = new AlarmAdapter(getApplicationContext());

        fab = findViewById(R.id.fab);
        list = findViewById(R.id.list);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                startActivity(intent);
            }
        });

        list.setAdapter(alarmAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.setAdapter(new AlarmAdapter(getApplicationContext()));

    }
}
