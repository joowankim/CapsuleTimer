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

    public void setFromDB() {
        DB db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        try {
            JSONArray alarmSet = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
            for (int idx = 0; idx < alarmSet.length(); idx++) {
                JSONObject eachAlarm = alarmSet.getJSONObject(idx);
                Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                intent.setClass(context, MyBroadcastReceiver.class);
                intent.putExtra("Title", eachAlarm.getString("medicine_name"));
                intent.putExtra("Type", "Alarm");
                intent.putExtra("repeat_no", eachAlarm.getInt("repeat_no"));
                intent.putExtra("original_repeat_no", eachAlarm.getInt("repeat_no"));
                intent.putExtra("repeat_time", eachAlarm.getInt("repeat_time"));
                intent.putExtra("date", eachAlarm.getString("date"));
                intent.putExtra("time", eachAlarm.getString("time"));
                intent.putExtra("weekOfDate", eachAlarm.getInt("weekOfDate"));
                intent.putExtra("auto", eachAlarm.getString("auto"));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Calendar calendar = Calendar.getInstance();
                String date = eachAlarm.getString("date");
                String time = eachAlarm.getString("time");
                int weekOfDate = eachAlarm.getInt("weekOfDate");
                if (date.compareTo("") != 0) {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]));
                    calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[2]));
                }

                if (weekOfDate > 0) {
                    if ((weekOfDate & 0x00000001) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); }
                    else if ((weekOfDate & 0x00000010) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); }
                    else if ((weekOfDate & 0x00000100) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY); }
                    else if ((weekOfDate & 0x00001000) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY); }
                    else if ((weekOfDate & 0x00010000) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY); }
                    else if ((weekOfDate & 0x00100000) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY); }
                    else if ((weekOfDate & 0x01000000) > 0) { calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY); }
                }

                calendar.set(Calendar.HOUR, Integer.parseInt(time.split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));

                alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFromButton(JSONArray alarms) {
        try {
            Log.d("JSON", alarms.toString());
            for (int idx = 0; idx < alarms.length(); idx++) {
                JSONObject alarmInfo = alarms.getJSONObject(idx);
                Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                intent.setClass(context, MyBroadcastReceiver.class);
                intent.putExtra("Title", alarmInfo.getString("medicine_name"));
                intent.putExtra("Type", "Alarm");
                intent.putExtra("repeat_no", alarmInfo.getInt("repeat_no"));
                intent.putExtra("original_repeat_no", alarmInfo.getInt("repeat_no"));
                intent.putExtra("repeat_time", alarmInfo.getInt("repeat_time"));
                intent.putExtra("date", alarmInfo.getString("date"));
                intent.putExtra("time", alarmInfo.getString("time"));
                intent.putExtra("weekOfDate", alarmInfo.getInt("weekOfDate"));
                intent.putExtra("auto", alarmInfo.getString("auto"));
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                Calendar calendar = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                Log.d("TIME_STANDARD", calendar.getTimeInMillis() + "");
                String date = alarmInfo.getString("date");
                String time = alarmInfo.getString("time");
                int weekOfDate = alarmInfo.getInt("weekOfDate");
                int flag = 0;

                if (date.compareTo("") != 0) {
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]) - 1);
                    calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[2]));
                }

                if (weekOfDate > 0) {

//                Log.d("VALUE", today.get(Calendar.HOUR_OF_DAY) + ", " + Integer.parseInt(time.split(":")[0]) + ", " + today.get(Calendar.MINUTE) + ", " + Integer.parseInt(time.split(":")[1]));
//                Log.d("STATEMENT", !(today.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(time.split(":")[0]) && today.get(Calendar.MINUTE) >= Integer.parseInt(time.split(":")[1])) + "");

                    for (int i = 0; i < 7; i++) {
                        if ((weekOfDate & (0x00000001 << (4 * i))) > 0 && today.get(Calendar.DAY_OF_WEEK) <= (i + 1)) {
                            if (today.get(Calendar.DAY_OF_WEEK) == (i + 1) && !(today.get(Calendar.HOUR_OF_DAY) > Integer.parseInt(time.split(":")[0]) && today.get(Calendar.MINUTE) > Integer.parseInt(time.split(":")[1]))) {
                                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                                flag = 1;
                                break;
                            }
                            if (today.get(Calendar.DAY_OF_WEEK) < (i + 1)) {
                                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                                flag = 1;
                                break;
                            }
                        }
                    }

                    if (flag == 0) {
                        if ((weekOfDate & 0x00000001) > 0 && alarmInfo.get("auto").toString().compareTo("false") == 0) {
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        } else if ((weekOfDate & 0x00000010) > 0) {
                            if ((weekOfDate & 0x00000010) > 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            } else if ((weekOfDate & 0x00000100) > 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            } else if ((weekOfDate & 0x00001000) > 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            } else if ((weekOfDate & 0x00010000) > 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            } else if ((weekOfDate & 0x00100000) > 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            }
                        } else if ((weekOfDate & 0x01000000) > 0 && alarmInfo.get("auto").toString().compareTo("false") == 0) {
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        }

                        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                            calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        }
                    }
                }

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));

                Log.d("TIME", calendar.getTimeInMillis() + "");

                if (Build.VERSION.SDK_INT >= 23) {
                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }else if (Build.VERSION.SDK_INT >= 19){
                    alarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                else {
                    alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }
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