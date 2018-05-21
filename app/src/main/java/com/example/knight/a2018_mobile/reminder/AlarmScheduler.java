package com.example.knight.a2018_mobile.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;

import java.util.Calendar;


public class AlarmScheduler {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    private static final long milDay = 86400000L;
    private static final long A_WEEK = 1000*60*60*24*7;
    private long currentTime;
    private boolean isRepeat;



    // setAlarm - AddReminderActivity에서 call
    // 여기서 repeatTime은 스킵 눌렀을 때 몇분 후에 다시 울릴지에 대한 시간
    public void setAlarm(Context context, long alarmTime, Uri reminderTask, long repeatTime) {
        // alarm manager를 얻어옴
        isRepeat = false;
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        // reminderTask -> 사용자가 설정한 alarm uri를 getRemiderPendingIntent method로 전달
        PendingIntent operation =
                ReminderAlarmService.getAlarmPendingIntent(context, reminderTask, repeatTime, isRepeat);

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
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        if(bolList[1]) { //일요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 1);
            c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[2]) { //월요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 2);
            c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[3]) { //화요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 3);
            c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[4]) { //수요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 4);
            c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[5]) { //목요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 5);
            c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[6]) { //금요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 6);
            c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
        if(bolList[7]) { //토요일
            PendingIntent operation =
                    ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 7);
            c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            alarmTime = c.getTimeInMillis();
            manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, A_WEEK, operation);
        }
    }

    public void cancelAlarm(Context context, Uri reminderTask, long repeatTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getAlarmRepeatPendingIntent(context, reminderTask, repeatTime, isRepeat, 1);

        manager.cancel(operation);
    }

}