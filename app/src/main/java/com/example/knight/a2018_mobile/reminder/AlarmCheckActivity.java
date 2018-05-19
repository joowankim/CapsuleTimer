package com.example.knight.a2018_mobile.reminder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.knight.a2018_mobile.R;

import java.util.Calendar;

/**
 * Created by leejisung on 2018-05-14.
 */

public class AlarmCheckActivity extends AppCompatActivity{

    private Button check_btn;
    private Button skip_btn;

    private int index = 0;
    private int flag = 0;

    private String repeat;
    private String title;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_check);

        Intent intent = getIntent();
        uri = intent.getData();
        Bundle b = new Bundle();
        b = intent.getExtras();


        repeat = b.getString("repeat");
        title = b.getString("title");

        check_btn = findViewById(R.id.check_btn);
        skip_btn = findViewById(R.id.skip_btn);

        check_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Calendar now = Calendar.getInstance();



                flag = 1;
                finish();
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int minute;

                Calendar now = Calendar.getInstance();
                minute = now.get(Calendar.MINUTE);
                minute = minute + Integer.parseInt(repeat);
                now.set(Calendar.MINUTE, minute);

                Log.i("minute", Integer.toString(now.get(Calendar.MINUTE)));

                if(index == 3){
                    flag = 0; // 약 안먹음
                }
                else index++;

                new AlarmScheduler().setAlarm(getApplicationContext(), now.getTimeInMillis(), uri);


                finish();
            }
        });

    }
}
