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
                for (int idx = 0; idx < alarmInfo.getString("time").split(" ").length; idx++) {
                    Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                    intent.setClass(context, MyBroadcastReceiver.class);
                    intent.putExtra("Id", alarmInfo.getInt("alarm_id"));
                    intent.putExtra("Title", alarmInfo.getString("medicine_name"));
                    intent.putExtra("Type", "Alarm");
                    intent.putExtra("repeat_no", alarmInfo.getInt("repeat_no"));
                    intent.putExtra("original_repeat_no", alarmInfo.getInt("repeat_no"));
                    intent.putExtra("repeat_time", Integer.parseInt(alarmInfo.getString("repeat_type").split(" ")[0]));
                    intent.putExtra("date", alarmInfo.getString("date"));
                    intent.putExtra("time", alarmInfo.getString("time").split(" ")[idx]);
                    intent.putExtra("weekOfDate", alarmInfo.getInt("weekOfDate"));
                    intent.putExtra("auto", alarmInfo.getString("auto"));

                    Calendar calendar = Calendar.getInstance();
                    Calendar today = Calendar.getInstance();
                    Log.d("TIME_STANDARD", calendar.getTimeInMillis() + "");
                    String date = alarmInfo.getString("date");
                    String time = alarmInfo.getString("time");
                    int weekOfDate = alarmInfo.getInt("weekOfDate");
                    int flag = 0;


                    String reqId = "";
                    if (time.split(":")[0].length() == 1)
                        reqId = alarmInfo.getInt("alarm_id") + "0" + time.split(" ")[idx].split(":")[0] + time.split(" ")[idx].split(":")[1]+"0";
                    else if (time.split(":")[0].length() == 2)
                        reqId = alarmInfo.getInt("alarm_id") + time.split(" ")[idx].split(":")[0] + time.split(" ")[idx].split(":")[1]+"0";

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqId), intent, PendingIntent.FLAG_ONE_SHOT);

                    if (date.compareTo("") != 0) {
                        calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[0]));
                        calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]) - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[2]));
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

                    Log.d("TIME", calendar.getTimeInMillis() + ", " + reqId);

                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(" ")[idx].split(":")[0]));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(" ")[idx].split(":")[1]));

                    if (Build.VERSION.SDK_INT >= 23) {
                        Log.d("VER", 0 + "");
                        alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= 19) {
                        Log.d("VER", 0 + "");
                        alarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    } else {
                        Log.d("VER", 0 + "");
                        alarm.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    }
                }
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
            for (int idx = 0; idx < alarmInfo.getString("time").split(" ").length; idx++) {
                Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                intent.setClass(context, MyBroadcastReceiver.class);
                intent.putExtra("Id", alarmInfo.getInt("alarm_id"));
                intent.putExtra("Title", alarmInfo.getString("medicine_name"));
                intent.putExtra("Type", "Alarm");
                intent.putExtra("repeat_no", alarmInfo.getInt("repeat_no"));
                intent.putExtra("original_repeat_no", alarmInfo.getInt("repeat_no"));
                intent.putExtra("repeat_time", Integer.parseInt(alarmInfo.getString("repeat_type").split(" ")[0]));
                intent.putExtra("date", alarmInfo.getString("date"));
                intent.putExtra("time", alarmInfo.getString("time").split(" ")[idx]);
                intent.putExtra("weekOfDate", alarmInfo.getInt("weekOfDate"));
                intent.putExtra("auto", alarmInfo.getString("auto"));

                Calendar calendar = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                Log.d("TIME_STANDARD", calendar.getTimeInMillis() + "");
                String date = alarmInfo.getString("date");
                String time = alarmInfo.getString("time");
                int weekOfDate = alarmInfo.getInt("weekOfDate");
                int flag = 0;


                String reqId = "";
                if (time.split(":")[0].length() == 1)
                    reqId = alarmInfo.getInt("alarm_id")+"0"+time.split(" ")[idx].split(":")[0]+time.split(" ")[idx].split(":")[1]+"0";
                else if (time.split(":")[0].length() == 2)
                    reqId = alarmInfo.getInt("alarm_id")+time.split(" ")[idx].split(":")[0]+time.split(" ")[idx].split(":")[1]+"0";

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqId), intent, PendingIntent.FLAG_ONE_SHOT);

                if (date.compareTo("") != 0) {
                    calendar.set(Calendar.YEAR, Integer.parseInt(date.split("/")[0]));
                    calendar.set(Calendar.MONTH, Integer.parseInt(date.split("/")[1]) - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("/")[2]));
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

                Log.d("TIME", calendar.getTimeInMillis() + ", "+reqId);

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(" ")[idx].split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(" ")[idx].split(":")[1]));

                if (Build.VERSION.SDK_INT >= 23) {
                    Log.d("VER", 0+"");
                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }else if (Build.VERSION.SDK_INT >= 19){
                    Log.d("VER", 0+"");
                    alarm.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
                else {
                    Log.d("VER", 0+"");
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