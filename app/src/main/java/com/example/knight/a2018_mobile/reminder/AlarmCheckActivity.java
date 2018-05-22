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

    private int index = 1;
    private int flag = 0;

    private String repeat_no;
    private String title;
    private Uri uri;

    private long repeatTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_check);

        Intent intent = getIntent();
        uri = intent.getData();
        Bundle b;
        b = intent.getExtras();


        repeat_no = b.getString("repeat_no");
        title = b.getString("title");
        repeatTime = b.getLong("repeatTime");

        Log.i("repeat_no ", repeat_no); // 반복 횟수
        Log.i("약 제목: ", title);
        Log.i("반복할 시간: ", Long.toString(repeatTime));

        check_btn = findViewById(R.id.check_btn);
        skip_btn = findViewById(R.id.skip_btn);

        check_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                // 약 먹었을 때 여기 해야 함

//                Calendar now = Calendar.getInstance();
//
//                medicineReportProvider temp = new medicineReportProvider();
//
//                temp.itemInsert(uri, title, now.getTimeInMillis());
//
//                flag = 1;
                finish();
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int minute;
                int repeat;
                int repeat_time;

                repeat = (int)(repeatTime/60000L); // 다시 분으로 만듦
                repeat_time = Integer.parseInt(repeat_no);

                Calendar now = Calendar.getInstance();
                minute = now.get(Calendar.MINUTE);
                minute = minute + repeat;
                now.set(Calendar.MINUTE, minute);

                Log.i("minute", Integer.toString(repeat));

                // 여기서 에러 뜸
                if(index <= repeat_time) {
                    new AlarmScheduler().setAlarm(getApplicationContext(), now.getTimeInMillis(), uri, repeatTime);
                    index++;
                } else{
                    index = 0;
                    // 여기 해야 되고
                    // 알람취소말고 다음날 혹은 지정한 요일로 다시 알람 세팅해야 할듯
                    // 필요한거 -> request code / a_week / 요일정보 / 시간 정보 /
                    // 일단 취소로 해놓음
                    new AlarmScheduler().cancelAlarm(getApplicationContext(), uri, repeatTime);
                }
                finish();
            }
        });

    }
}
