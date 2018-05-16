package com.example.knight.a2018_mobile.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.example.knight.a2018_mobile.AlertReceiver;


public class AlarmScheduler {

    /**
     * Schedule a reminder alarm at the specified time for the given task.
     *
     * @param context Local application or activity context

     * @param reminderTask Uri referencing the task in the content provider
     */

    // setAlarm - AddReminderActivity에서 call
    public void setAlarm(Context context, long alarmTime, Uri reminderTask) {
        // alarm manager를 얻어옴
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);
        AlarmManager checkManager = AlarmManagerProvider.getAlarmManager(context);

        // reminderTask -> 사용자가 설정한 alarm uri를 getRemiderPendingIntent method로 전달
        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        //  Alarm Check activity 위한 pending intent 만듦
        Intent check_intent = new Intent(context, AlertReceiver.class);
        PendingIntent for_check = PendingIntent.getBroadcast(context, 1, check_intent, 0);


        // alarm이 설정 됨
        if (Build.VERSION.SDK_INT >= 23) {
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation);
            checkManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, for_check);

        } else if (Build.VERSION.SDK_INT >= 19) {
            manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation);
           // manager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, for_check);
        } else {
            manager.set(AlarmManager.RTC_WAKEUP, alarmTime, operation);
            checkManager.set(AlarmManager.RTC_WAKEUP, alarmTime, for_check);
        }
    }

    public void setRepeatAlarm(Context context, long alarmTime, Uri reminderTask, long RepeatTime) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        // call Alarm Check
        Intent check_intent = new Intent(context, AlarmCheckActivity.class);
        PendingIntent for_check = PendingIntent.getBroadcast(context, 1, check_intent, 0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation);
       // manager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime,RepeatTime, for_check);


    }

    public void cancelAlarm(Context context, Uri reminderTask) {
        AlarmManager manager = AlarmManagerProvider.getAlarmManager(context);

        PendingIntent operation =
                ReminderAlarmService.getReminderPendingIntent(context, reminderTask);

        manager.cancel(operation);

    }

}