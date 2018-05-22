package com.example.knight.a2018_mobile.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

    public Uri mCurrentReminderUri;

    private static final long A_WEEK = 1000*60*60*24*7;

    private int index = 1;
    private int flag = 0;
    private int day_int = 0;
    private int repeat_number = 0;
    private int mHour, mMinute;

    private String repeat_no;
    private String title;
    private Uri uri;

    private Calendar mCalendar = Calendar.getInstance();

    private boolean isRepeat;
    private boolean isSkip;

    private long repeatTime;
    private long alarmTime;

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
        mHour = b.getInt("hour");
        mMinute = b.getInt("minute");
        repeatTime = b.getLong("repeatTime");
        isRepeat = b.getBoolean("isRepeat"); // 주단위 반복인지 아닌지
        isSkip = b.getBoolean("isSkip");
        if(isRepeat) day_int = b.getInt("day_int");  // 무슨요일에 설정된 알람인지
        if(isSkip) index = b.getInt("repeat_times"); // skip을 몇번 반복했는지

        repeat_number = Integer.parseInt(repeat_no);

        Log.i("repeat_no ", repeat_no); // 반복 횟수
        Log.i("약 제목: ", title);
        Log.i("알람 시간: ", Integer.toString(mHour));
        Log.i("알람 분: ", Integer.toString(mMinute));
        Log.i("반복할 시간: ", Long.toString(repeatTime));
        Log.i("뭔요일??: ", Integer.toString(day_int));
        //Log.i("몇번째 반복?: ", Integer.toString(index));

        check_btn = findViewById(R.id.check_btn);
        skip_btn = findViewById(R.id.skip_btn);

        mCalendar.setTimeInMillis(System.currentTimeMillis());
        mCalendar.set(Calendar.DAY_OF_WEEK, day_int);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);
        alarmTime = mCalendar.getTimeInMillis();

        check_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

//                Calendar now = Calendar.getInstance();
//
//                medicineReportProvider temp = new medicineReportProvider();
//
//                temp.itemInsert(uri, title, now.getTimeInMillis());
//
//                flag = 1;

                AlarmManager manager = AlarmManagerProvider.getAlarmManager(getApplicationContext());
                PendingIntent operation =
                        ReminderAlarmService.getAlarmRepeatPendingIntent(getApplicationContext(), uri, repeatTime, isRepeat, isSkip, day_int, mCalendar);
                alarmTime += A_WEEK;

                Log.i("알람 재설정 시간: ", Long.toString(alarmTime));
                Log.i("무슨요일의 알람?: ", Integer.toString(day_int));

                manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);

                finish();
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int minute;
                int repeat;

                repeat = (int)(repeatTime/60000L); // 다시 분으로 만듦

                Calendar now = Calendar.getInstance();
                minute = now.get(Calendar.MINUTE);
                minute = minute + repeat;
                now.set(Calendar.MINUTE, minute);

                Log.i("minute", Integer.toString(repeat));


                if(index <= repeat_number) {
                    Log.i("index값은 :" , Integer.toString(index));
                    Log.i("repeat number는: ", Integer.toString(repeat_number));
                    Log.i("무슨요일입니까?", Integer.toString(day_int));
                    new AlarmScheduler().setSkipAlarm(getApplicationContext(), now.getTimeInMillis(), uri, repeatTime, index, day_int, mCalendar);
                } else{
                    index = 1;
                    // 여기 해야 되고
                    // 알람취소말고 다음날 혹은 지정한 요일로 다시 알람 세팅해야 할듯
                    // 필요한거 -> request code / a_week / 요일정보 / 시간 정보 /
                    // 일단 취소로 해놓음
                    Log.i("알람","재설정");
                    AlarmManager manager = AlarmManagerProvider.getAlarmManager(getApplicationContext());
                    PendingIntent operation =
                            ReminderAlarmService.getAlarmRepeatPendingIntent(getApplicationContext(), uri, repeatTime, isRepeat, isSkip, day_int, mCalendar);
                    alarmTime += A_WEEK;

                    Log.i("알람 재설정 시간: ", Long.toString(alarmTime));
                    Log.i("무슨요일의 알람?: ", Integer.toString(day_int));

                    manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
                }
                finish();
            }
        });

    }
}
