package com.example.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * @brief make alarm with data that user settings and check whether alarm is repeat type or not
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */
//씀
public class AlarmService extends Service {

    public AlarmManager alarm;
    public Context context;

    public AlarmService(Context context) {
        this.context = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.knight.alarm.AlarmRinging");

        context.registerReceiver(new MyBroadcastReceiver(), intentFilter);
        alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * @brief make alarm with data that read from database
     */
    public void setFromDB() {
        DB db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        try {
            JSONArray alarmSet = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
            for (int json = 0; json < alarmSet.length(); json++) {
                JSONObject alarmInfo = alarmSet.getJSONObject(json);
                Module.settingAlarm(context, alarmInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief when save button is clicked in alarm setting screen, make alarm with data that user input
     * @param alarms
     */
    public void setFromButton(JSONArray alarms) {
        try {
            Log.d("JSON", alarms.toString());
            JSONObject alarmInfo = alarms.getJSONObject(0);
            Module.settingAlarm(context, alarmInfo);
        } catch(Exception e){
            e.printStackTrace();
        }
    }



    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}