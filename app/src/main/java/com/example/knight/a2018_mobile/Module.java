package com.example.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONObject;

import java.util.Calendar;

public class Module {

    private static final String PREF_MONTH = "month";
    private static final String PREF_YEAR = "year";

    public final static void notiVersion(AlarmManager alarm, Calendar calendar, PendingIntent pendingIntent) {
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

    public final static void calendarMonthChaning(SharedPreferences sp, Calendar cal, int delta) {
        int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
        int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, thisMonth);
        cal.set(Calendar.YEAR, thisYear);
        cal.add(Calendar.MONTH, delta);
        sp.edit().putInt(PREF_MONTH, cal.get(Calendar.MONTH)).putInt(PREF_YEAR, cal.get(Calendar.YEAR)).apply();
    }

    public final static String genPendingIntentId(int id, String time) {
        String reqId = "";
        String[] splitedTime = time.split(":");

        if (splitedTime[0].length() == 1)
            reqId = id+"0"+splitedTime[0]+splitedTime[1];
        else if (splitedTime[0].length() == 2)
            reqId = id+splitedTime[0]+splitedTime[1];
        return reqId;
    }

    public final static void lineSetting(LineDataSet lineData, int color) {
        lineData.setLineWidth(2);
        lineData.setCircleRadius(6);
        lineData.setCircleColor(color);
        lineData.setCircleColorHole(Color.BLUE);
        lineData.setColor(color);
        lineData.setDrawCircleHole(true);
        lineData.setDrawCircles(true);
    }

    public final static void settingAlarm(Context context, JSONObject json) {
        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        try {
            for (int idx = 0; idx < json.getString("time").split(" ").length; idx++) {
                Intent intent = new Intent("com.example.knight.alarm.AlarmRinging");
                intent.setClass(context, MyBroadcastReceiver.class);
                intent.putExtra("Id", json.getInt("alarm_id"));
                intent.putExtra("Title", json.getString("medicine_name"));
                intent.putExtra("Type", "Alarm");
                intent.putExtra("repeat_no", json.getInt("repeat_no"));
                intent.putExtra("original_repeat_no", json.getInt("repeat_no"));
                intent.putExtra("repeat_time", Integer.parseInt(json.getString("repeat_type").split(" ")[0]));
                intent.putExtra("date", json.getString("date"));
                intent.putExtra("time", json.getString("time").split(" ")[idx]);
                intent.putExtra("weekOfDate", json.getInt("weekOfDate"));
                intent.putExtra("auto", json.getString("auto"));

                Calendar calendar = Calendar.getInstance();
                Calendar today = Calendar.getInstance();
                Log.d("TIME_STANDARD", calendar.getTimeInMillis() + "");
                String date = json.getString("date");
                String time = json.getString("time");
                int weekOfDate = json.getInt("weekOfDate");
                int flag = 0;

                String reqId = genPendingIntentId(json.getInt("alarm_id"), time.split(" ")[idx]);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqId), intent, PendingIntent.FLAG_ONE_SHOT);

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(" ")[idx].split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(" ")[idx].split(":")[1]));

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
                            if (((i == 0 || i == 6) && json.get("auto").toString().compareTo("false") == 0) || (1 <= i && i <= 5)) {
                                Log.d("VALUE", i + ", " + json.get("auto").toString());
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
                    }

                    if (flag == 0) {
                        if ((weekOfDate & 0x00000001) > 0 && json.get("auto").toString().compareTo("false") == 0) {
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        } else if ((weekOfDate & 0x01000000) > 0 && json.get("auto").toString().compareTo("false") == 0) {
                            Log.d("TEST", calendar.getTime() + "");
                            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                        } else if ((weekOfDate & 0x00111110) > 0) {
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
                        }
                    }

                    if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    }
                }

                Log.d("TIME", calendar.getTimeInMillis() + ", " + reqId);

                notiVersion(alarm, calendar, pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
