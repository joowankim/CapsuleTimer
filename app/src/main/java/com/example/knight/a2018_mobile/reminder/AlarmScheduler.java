package com.example.knight.a2018_mobile.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.Calendar;


public class AlarmScheduler {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    //private static final long milDay = 86400000L;
    private static final long A_WEEK = 1000*60*60*24*7;
    private long currentTime;
    private boolean isRepeat;
    private boolean isSkip;
    private int hour;
    private int minute;




    // setAlarm - AddReminderActivity에서 call
    // 여기서 repeatTime은 스킵 눌렀을 때 몇분 후에 다시 울릴지에 대한 시간
    public void setAlarm(Context context, long alarmTime, Uri reminderTask, long repeatTime) {
        // alarm manager를 얻어옴
        isRepeat = false;
        isSkip = false;
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        // reminderTask -> 사용자가 설정한 alarm uri를 getRemiderPendingIntent method로 전달
        PendingIntent operation =
                ReminderAlarmService.getAlarmPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip);

        currentTime = System.currentTimeMillis();
        // 만약 현재 시간보다 알람을 설정한 시간이 작다면 알람이 바로 울려버리기 때문에 알람을 설정하지 않게함 (왜냐면 특정 날짜로 한 경우이기 때문)
        if(currentTime > alarmTime)
            return;

        // alarm이 설정 됨
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        } else if (Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------
    // repeatTime -> skip했을 때 얼마 후에 울릴지
    // bolList -> 각 toggle 버튼이 뭐가 눌렷는지 boolean arraylist임 (일 월 화 수 목 금 토 순서)
    // Calendar -> day를 뺀/ 년,월,시,분,초가 들어가있음
    // alarmTime -> 몇일인지를 뺀 나머지로 시간처리되어있음
    // 애초에 알람을 설정할 때 울릴 요일을 설정하고 몇시 몇분 에 울리도록 넘어온단 말이지 년 월에 상관없이 무슨요일에 정확히 몇시 몇분에 말이야

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long repeatTime, boolean[] bolList, Calendar c) {
        isRepeat = true;
        isSkip = false;
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        currentTime = System.currentTimeMillis();

        Log.i("설정한 시간: ", Integer.toString(hour));
        Log.i("설정한 분 :" , Integer.toString(minute));

        if(bolList[1]) { //일요일
            Log.i("일요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);

            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip, 1, c);

            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[2]) { //월요일
            Log.i("월요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,2, c);

            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[3]) { //화요일
            Log.i("화요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,3, c);

            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            Log.i("현재 시간: ", Long.toString(currentTime));
            Log.i("알람 시간: ", Long.toString(alarmTime));

            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[4]) { //수요일
            Log.i("수요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat,isSkip, 4, c);
            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[5]) { //목요일
            Log.i("목요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,5, c);
            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[6]) { //금요일
            Log.i("금요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,6, c);
            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[7]) { //토요일
            Log.i("토요일", "true");
            c.setTimeInMillis(System.currentTimeMillis());
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, minute);
            c.set(Calendar.SECOND, 0);
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,7, c);

            alarmTime = c.getTimeInMillis();
            if(alarmTime < currentTime)
                alarmTime += A_WEEK;
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
    }

    // 스킵을 누르면 현재 시간으로부터 몇 분 후에 다시 알람을 등록할 것인지
    // 현재 skip을 누른 횟수가 몇번째 인지 업데이트
    public void setSkipAlarm(Context context, long alarmTime, Uri reminderTask, long repeatTime, int repeat_time, int dayInt, Calendar c) {
        // alarm manager를 얻어옴
        isRepeat = true;
        isSkip = true;
        repeat_time++;
        Log.i("무슨요일일까용??", Integer.toString(dayInt));

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        currentTime = System.currentTimeMillis();

        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);

        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        // reminderTask -> 사용자가 설정한 alarm uri를 getRemiderPendingIntent method로 전달
        PendingIntent operation =
                ReminderAlarmService.getSkipAlarmPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip, dayInt, repeat_time, c);

//        currentTime = System.currentTimeMillis();
//        // 만약 현재 시간보다 알람을 설정한 시간이 작다면 알람이 바로 울려버리기 때문에 알람을 설정하지 않게함 (왜냐면 특정 날짜로 한 경우이기 때문)
//        if(currentTime > alarmTime)
//            return;

        Log.i("반복 알람이","설정됨");

        // alarm이 설정 됨
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        } else if (Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);
//            operation.s
        }
    }

    public void cancelAlarm(Context context, Uri reminderTask, long repeatTime, Calendar c) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, isSkip,1, c);

        manager.cancel(operation);
    }
}