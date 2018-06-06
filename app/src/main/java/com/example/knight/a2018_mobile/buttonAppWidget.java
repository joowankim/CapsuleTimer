package com.example.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @brief Implementation of Button App Widget functionality
 * @author Kim joo wan
 * @date 2018.05.29
 * @version 1.0.0.1
 */

public class buttonAppWidget extends AppWidgetProvider {

    DB db1, db2;
    private String medicine_name = "";
    Calendar c;

    @Override
    public void onReceive(Context context, Intent reIntent) {
        super.onReceive(context, reIntent);

        db1 = new DB(context, "Alarm.db", null, 1);
        db1.getWritableDatabase();

        try {
            JSONArray result = new JSONArray(db1.mySelect("medicine_alarm", "*", "1 = 1"));
            JSONObject tmp = result.getJSONObject(0);
            medicine_name += tmp.getString("medicine_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String action = reIntent.getAction();
        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            Bundle extras = reIntent.getExtras();
            //Bundle 은 Key-Value 쌍으로 이루어진 일종의 해쉬맵 자료구조
            //한 Activity에서 Intent 에 putExtras로 Bundle 데이터를 넘겨주고,
            //다른 Activity에서 getExtras로 데이터를 참조하는 방식입니다.
            if(extras!=null)
            {
                int [] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if(appWidgetIds!=null && appWidgetIds.length>0)
                    this.onUpdate(context,AppWidgetManager.getInstance(context),appWidgetIds);
            }
        }//업데이트인 경우
        else if(action.equals("Click1"))
        {
            try {
                c = Calendar.getInstance();
                db2.myInsert("medicine_taken", "medicine_name, time", "\"" + medicine_name + "\", " + c.getTime());

                AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

                JSONArray result = new JSONArray(db1.mySelect("medicine_alarm", "*", "1 = 1"));
                JSONObject current = result.getJSONObject(0);
                String times = current.getString("time");
                String[] time = times.split(" ");

                if (current.get("auto") != "false" && !(c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)){
                    double interval = 0.0;
                    for (int idx = 1; idx < time.length; idx++) {
                        String[] hhmm1 = time[idx].split(":");
                        String[] hhmm2 = time[idx - 1].split(":");
                        interval += (Double.parseDouble(hhmm1[0]) - Double.parseDouble(hhmm2[0])) * 3600;
                        interval += (Double.parseDouble(hhmm1[1]) - Double.parseDouble(hhmm2[1])) * 60;
                    }
                    interval *= 1000;
                    interval /= time.length - 1;

                    for (int idx = 1; idx < time.length; idx++) {
                        long tmpTime = c.getTimeInMillis() + (long) interval * idx;
                        Calendar currentTime = Calendar.getInstance();
                        currentTime.setTimeInMillis(tmpTime);
                        String curHHMM = String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(currentTime.get(Calendar.MINUTE));
                        Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                        intent.setClass(context, MyBroadcastReceiver.class);
                        intent.putExtra("Title", current.getString("medicine_name"));
                        intent.putExtra("Type", "Alarm");
                        intent.putExtra("repeat_no", current.getInt("repeat_no"));
                        intent.putExtra("original_repeat_no", current.getInt("repeat_no"));
                        intent.putExtra("repeat_time", current.getInt("repeat_time"));
                        intent.putExtra("date", current.getString("date"));
                        intent.putExtra("time", curHHMM);
                        intent.putExtra("weekOfDate", current.getInt("weekOfDate"));
                        intent.putExtra("auto", "true");
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                        if (Build.VERSION.SDK_INT >= 23) {
                            alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
                        } else if (Build.VERSION.SDK_INT >= 19) {
                            alarm.setExact(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
                        } else {
                            alarm.set(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
                        }
                    }
                    Toast.makeText(context,"알람이 다음 복용 시간으로 설정되었습니다",Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        final int N = appWidgetIds.length;
        for(int i=0;i<N;i++)
        {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = buildViews(context);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    //Click1 이라는 Action을 onReceive로 보낸다.

    /**
     * @brief send click action to the onReceive
     * @param context
     * @return pendingintnet
     */
    private PendingIntent buildToastIntent(Context context)
    {
        // manifest에 지정해둔 Click1 액션으로 지정해 인텐트를 보낸 후 if문을 통해 다음 action을 결정한다
        Intent in = new Intent("Click1");

        PendingIntent pi = PendingIntent.getBroadcast(context,0,in, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    //위젯에 멀티 버튼 추가하기

    /**
     * @biref add button to the widget
     * @param context
     * @return views
     */
    private RemoteViews buildViews(Context context)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.button_app_widget);
        views.setOnClickPendingIntent(R.id.simple_btn2,buildToastIntent(context));

        return views;
    }

}

