package com.example.knight.a2018_mobile.reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.example.knight.a2018_mobile.AddReminderActivity;
import com.example.knight.a2018_mobile.R;
import com.example.knight.a2018_mobile.data.AlarmReminderContract;


public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    // AlarmScheduler - setAlarm method에서 call함
    // 전달받은 uri로 pendingIntent 만들어서 ReminderAlarmService를 call하는 pendingIntent를 반환
    public static PendingIntent getReminderPendingIntent(Context context, Uri uri) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    // pendingIntent에 의해 reminderAlarmService가 호출되면 onHandleIntent가 처리함
    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 사용자가 설정한 알람 시간을 uri로 가져옴
        Uri uri = intent.getData();

        //Display a notification to view the task details
        Intent action = new Intent(this, AddReminderActivity.class);
        action.setData(uri);
        PendingIntent operation = TaskStackBuilder.create(this)
                .addNextIntentWithParentStack(action)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        if(uri != null){
            //uri에 해당하는 cursor를 가져옴
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        String repeat_no ="";
        try {
            // cursor가 null이 아니면 맨 처음으로 옮겨놓은다음
            // 지정한 알람 시간에 맞는 제목을 description에 저장
            if (cursor != null && cursor.moveToFirst()) {
                description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
                repeat_no = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            }
        } finally {
            //그 후 cursor는 null이 아니면 없앰
            if (cursor != null) {
                cursor.close();
            }
        }

//        // show alarm check activity
//        Intent check = new Intent(this, AlarmCheckActivity.class);
//        startActivity(check);

        // Noti를 생성함 (Notichannel을 통해서)
        // 밑이랑 동일한 코드지만 channel을 통해 부르게 함 (Oreo version부터 적용)
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationHelper notificationHelper = new NotificationHelper(this);
            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(getString(R.string.capsule_timer), description);
            notificationHelper.getManager().notify(NOTIFICATION_ID, nb.build());
        } else{
            Notification note = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.capsule_timer))
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .build();
                 manager.notify(NOTIFICATION_ID, note);
        }

        intent = new Intent(this, AlarmCheckActivity.class);
        Bundle b = new Bundle();
        b.putString("repeat", repeat_no);
        b.putString("title", description);
        intent.putExtras(b);
        intent.setData(uri);
        startActivity(intent);


    }
}