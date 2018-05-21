package com.example.knight.a2018_mobile.reminder;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.knight.a2018_mobile.data.AlarmReminderContract;


public class ReminderAlarmService extends IntentService {
    private static final String TAG = ReminderAlarmService.class.getSimpleName();

    private static final int NOTIFICATION_ID = 42;

    Cursor cursor;
    //This is a deep link intent, and needs the task stack
    // AlarmScheduler - setAlarm method에서 call함
    // 전달받은 uri로 pendingIntent 만들어서 ReminderAlarmService를 call하는 pendingIntent를 반환
    public static PendingIntent getAlarmPendingIntent(Context context, Uri uri, long repeatTime, boolean isRepeat) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.putExtra("repeatTime", repeatTime); // skip때 얼마 후에 울릴지
        action.putExtra("isRepeat", isRepeat); // 월화수목금토일 눌렷는지 아닌지
        action.setData(uri);
        return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //This is a deep link intent, and needs the task stack
    // AlarmScheduler - setAlarm method에서 call함
    // 전달받은 uri로 pendingIntent 만들어서 ReminderAlarmService를 call하는 pendingIntent를 반환
    // 여기서 request code를 구분해서 알람을 요일마다 따로 설정을 해줘야하는거 같은데
    public static PendingIntent getAlarmRepeatPendingIntent(Context context, Uri uri, long repeatTime, boolean isRepeat, int dayInt) {
        Intent action = new Intent(context, ReminderAlarmService.class);
        action.putExtra("repeatTime", repeatTime); // skip때 얼마 후에 울릴지
        action.putExtra("isRepeat", isRepeat); // 월화수목금토일 눌렷는지 아닌지
        action.setData(uri);
        return PendingIntent.getService(context, dayInt, action, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public ReminderAlarmService() {
        super(TAG);
    }

    // pendingIntent에 의해 reminderAlarmService가 호출되면 onHandleIntent가 처리함
    @Override
    protected void onHandleIntent(Intent intent) {

        //NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        long repeatTime;
        boolean isRepeat;

        // 사용자가 설정한 알람 시간을 uri로 가져옴
        Uri uri = intent.getData();
        repeatTime = intent.getLongExtra("repeatTime", 0);
        isRepeat = intent.getBooleanExtra("isRepeat", false);

        Log.i("repeatTime 확인: ", Long.toString(repeatTime));
        Log.i("isRepeat 확인: ", Boolean.toString(isRepeat));

        //Display a notification to view the task details
//        Intent action = new Intent(this, AddReminderActivity.class);
//        action.setData(uri);
//        PendingIntent operation = TaskStackBuilder.create(this)
//                .addNextIntentWithParentStack(action)
//                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Grab the task description
        if(uri != null){
            //uri에 해당하는 cursor를 가져옴
            cursor = getContentResolver().query(uri, null, null, null, null);
        }

        String description = "";
        String repeat_no ="";
        int sunday = 0;
        int monday =0;
        int tuesday = 0;
        int wednesday = 0;
        int thursday = 0;
        int friday = 0;
        int saturday = 0;

        try {
            // cursor가 null이 아니면 맨 처음으로 옮겨놓은다음
            // 지정한 알람 시간에 맞는 제목을 description에 저장
            if (cursor != null && cursor.moveToFirst()) {
                description = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
                repeat_no = AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
                sunday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_SUNDAY));
                monday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_MONDAY));
                tuesday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_TUESDAY));
                wednesday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_WEDNESDAY));
                thursday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_THURSDAY));
                friday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_FRIDAY));
                saturday = Integer.parseInt(AlarmReminderContract.getColumnString(cursor, AlarmReminderContract.AlarmReminderEntry.KEY_SATURDAY));
            }
        } finally {
            //그 후 cursor는 null이 아니면 없앰
            if (cursor != null) {
                cursor.close();
            }
        }

        Log.i("Sunday = " , Integer.toString(sunday));
        Log.i("monday = " , Integer.toString(monday));
        Log.i("tuesday = " , Integer.toString(tuesday));
        Log.i("wednesday = " , Integer.toString(wednesday));
        Log.i("thursday = " , Integer.toString(thursday));
        Log.i("friday = " , Integer.toString(friday));
        Log.i("saturday = " , Integer.toString(saturday));


        intent = new Intent(this, AlarmCheckActivity.class);
        Bundle b = new Bundle();
        b.putString("title", description); // 제목
        b.putString("repeat_no", repeat_no); // 몇번이나 다시 울리게 할지
        b.putLong("repeatTime", repeatTime); // skip 누르고 얼마 후에 울릴지
        intent.putExtras(b);
        intent.setData(uri);
        startActivity(intent);

        // 여기서 등록된 알람이 울릴때 오늘날짜인지 아닌지 확인해서 오늘날짜면 밑 코드를 실행하고 아니면 return을 해야할듯
//        if(isRepeat){ // 요일마다 알람 울리게
//
//            intent = new Intent(this, AlarmCheckActivity.class);
//            Bundle b = new Bundle();
//            b.putString("repeat_no", repeat_no);
//            b.putString("title", description);
//            b.putLong("repeatTime", repeatTime);
//            intent.putExtras(b);
//            intent.setData(uri);
//            startActivity(intent);
//
//
//        } else{ // 특정 날짜에만 울리게
//            intent = new Intent(this, AlarmCheckActivity.class);
//            Bundle b = new Bundle();
//            b.putString("repeat_no", repeat_no);
//            b.putString("title", description);
//            b.putLong("repeatTime", repeatTime);
//            intent.putExtras(b);
//            intent.setData(uri);
//            startActivity(intent);
//        }

//        // show alarm check activity
//        Intent check = new Intent(this, AlarmCheckActivity.class);
//        startActivity(check);

        // Noti를 생성함 (Notichannel을 통해서)
        // 밑이랑 동일한 코드지만 channel을 통해 부르게 함 (Oreo version부터 적용)
//        if (Build.VERSION.SDK_INT < 26) {
//            Notification note = new NotificationCompat.Builder(this)
//                    .setContentTitle(getString(R.string.capsule_timer))
//                    .setContentText(description)
//                    .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
//                    .setContentIntent(operation)
//                    .setAutoCancel(true)
//                    .build();
//            manager.notify(NOTIFICATION_ID, note);
//
//        } else{
//            NotificationHelper notificationHelper = new NotificationHelper(this);
//            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(getString(R.string.capsule_timer), description);
//            notificationHelper.getManager().notify(NOTIFICATION_ID, nb.build());
//        }
    }
}