package com.example.knight.a2018_mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by leejisung on 2018-05-06.
 */
//여기서 노티를 콜
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("Alarm", "Your Alarm is working");
        notificationHelper.getManager().notify(1,nb.build());
    }
}
