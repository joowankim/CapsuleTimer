package com.mobile_term_project.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private int MODE_PRIVATE = 0;

    public MyBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NOTI", "NOTIFICATION ");
        Log.d("NOTI", intent.getStringExtra("Type") + intent.getIntExtra("Id", 0)+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1]);

        if (intent.getStringExtra("Type").compareTo("Alarm") == 0) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                notificationManager.createNotificationChannel(notificationChannel);
                builder = new NotificationCompat.Builder(context, notificationChannel.getId());
            } else {
                builder = new NotificationCompat.Builder(context);
            }

            String reqId = "";
            if (intent.getStringExtra("time").split(":")[0].length() == 1)
                reqId = intent.getIntExtra("Id", 0)+"0"+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];
            else if (intent.getStringExtra("time").split(":")[0].length() == 2)
                reqId = intent.getIntExtra("Id", 0)+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];


            Intent taken = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");
            taken.putExtra("Id", intent.getIntExtra("Id", 0));
            taken.putExtra("Type", "Taken");
            taken.putExtra("Title", intent.getStringExtra("Title"));
            taken.putExtra("repeat_no", intent.getIntExtra("repeat_no",0));
            taken.putExtra("original_repeat_no", intent.getIntExtra("repeat_no",0));
            taken.putExtra("repeat_time", intent.getIntExtra("repeat_time", 0));
            taken.putExtra("date", intent.getStringExtra("date"));
            taken.putExtra("time", intent.getStringExtra("time"));
            taken.putExtra("weekOfDate", intent.getIntExtra("weekOfDate", 0));
            taken.putExtra("auto", intent.getStringExtra("auto"));
            taken.setClass(context, MyBroadcastReceiver.class);

            Intent skip = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");
            skip.putExtra("Id", intent.getIntExtra("Id", 0));
            skip.putExtra("Type", "Skip");
            skip.putExtra("Title", intent.getStringExtra("Title"));
            skip.putExtra("repeat_no", intent.getIntExtra("repeat_no",0));
            skip.putExtra("original_repeat_no", intent.getIntExtra("repeat_no",0));
            skip.putExtra("repeat_time", intent.getIntExtra("repeat_time", 0));
            skip.putExtra("date", intent.getStringExtra("date"));
            skip.putExtra("time", intent.getStringExtra("time"));
            skip.putExtra("weekOfDate", intent.getIntExtra("weekOfDate", 0));
            skip.putExtra("auto", intent.getStringExtra("auto"));
            skip.setClass(context, MyBroadcastReceiver.class);

            builder = builder
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setColor(Color.BLUE)
                    .setContentTitle(intent.getStringExtra("Title"))
                    .setTicker("TEST")
                    .setContentText(intent.getStringExtra("Title") + " " + "먹어야할 시간입니다.")
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_launcher_background, "Take", PendingIntent.getBroadcast(context, Integer.parseInt(reqId+1), taken, PendingIntent.FLAG_ONE_SHOT))
                    .addAction(R.drawable.ic_access_time_black, "Skip", PendingIntent.getBroadcast(context, Integer.parseInt(reqId+2), skip, PendingIntent.FLAG_ONE_SHOT));
            notificationManager.notify(333, builder.build());

        } else if (intent.getStringExtra("Type").compareTo("Taken") == 0) {

            String current = String.valueOf(Calendar.getInstance().getTimeInMillis()/1000.0);

            DB db = new DB(context, "Taken.db", null, 1);
            db.getWritableDatabase();
            db.myInsert("medicine_taken", "medicine_name, time", "\"" + intent.getStringExtra("Title") + "\", \"" + current +"\"");
            /* TODO
            In this place we have to implement socket communication
            */
            try {
                db = new DB(context, "Alarm.db", null, 1);
                db.getWritableDatabase();
                JSONArray res = new JSONArray(db.mySelect("medicine_alarm", "remain", "alarm_id = "+intent.getIntExtra("Id", 0)));
                JSONObject cur = res.getJSONObject(0);
                db.myUpdate("medicine_alarm", "remain = "+(cur.getInt("remain")-1), "alarm_id = "+intent.getIntExtra("Id", 0));

                if (cur.getInt("remain")-1 < 10) {
                    NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = null;

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                        notificationManager.createNotificationChannel(notificationChannel);
                        builder = new NotificationCompat.Builder(context, notificationChannel.getId());
                    } else {
                        builder = new NotificationCompat.Builder(context);
                    }

                    builder = builder
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setColor(Color.BLUE)
                            .setContentTitle(intent.getStringExtra("Title"))
                            .setTicker("TEST")
                            .setContentText(intent.getStringExtra("Title") + "약이 " + (cur.getInt("remain")-1) + "개만 남았습니다.")
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setAutoCancel(true);
                    notificationManager.notify(123, builder.build());

                }

                JSONObject info = new JSONObject();
                SharedPreferences sharedPreferences = context.getSharedPreferences("Login_Session", MODE_PRIVATE);
                String user_id = sharedPreferences.getString("Id", "None");
                MySocket socket = new MySocket(Server_IP, Server_PORT);
                info.put("Id", user_id);
                info.put("Medicine_Name", intent.getStringExtra("Title"));
                info.put("Date", current);
                info.put("Type", "Medicine_Taken");
                socket.request(info.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }


            AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent newAlarm = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");

            String reqId = "";
            if (intent.getStringExtra("time").split(":")[0].length() == 1)
                reqId = intent.getIntExtra("Id", 0)+"0"+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];
            else if (intent.getStringExtra("time").split(":")[0].length() == 2)
                reqId = intent.getIntExtra("Id", 0)+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];

            newAlarm.putExtra("Id", intent.getIntExtra("Id", 0));
            newAlarm.putExtra("Title", intent.getStringExtra("Title"));
            newAlarm.putExtra("Type", "Alarm");
            newAlarm.putExtra("repeat_no", intent.getIntExtra("repeat_no", 0)-1);
            newAlarm.putExtra("original_repeat_no", intent.getIntExtra("repeat_no",0));
            newAlarm.putExtra("repeat_time", intent.getIntExtra("repeat_time", 0));
            newAlarm.putExtra("date", intent.getStringExtra("date"));
            newAlarm.putExtra("time", intent.getStringExtra("time"));
            newAlarm.putExtra("weekOfDate", intent.getIntExtra("weekOfDate", 0));
            newAlarm.putExtra("auto", intent.getStringExtra("auto"));
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqId+0), newAlarm, PendingIntent.FLAG_ONE_SHOT);

            Calendar calendar = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            String date = newAlarm.getStringExtra("date");
            String time = newAlarm.getStringExtra("time");
            int weekOfDate = newAlarm.getIntExtra("weekOfDate", 0);
            int flag = 0;

            if (weekOfDate > 0) {

//                Log.d("VALUE", today.get(Calendar.HOUR_OF_DAY) + ", " + Integer.parseInt(time.split(":")[0]) + ", " + today.get(Calendar.MINUTE) + ", " + Integer.parseInt(time.split(":")[1]));
//                Log.d("STATEMENT", !(today.get(Calendar.HOUR_OF_DAY) >= Integer.parseInt(time.split(":")[0]) && today.get(Calendar.MINUTE) >= Integer.parseInt(time.split(":")[1])) + "");

                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.split(":")[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(time.split(":")[1]));

                for (int i = 0; i < 7; i++) {
                    if ((weekOfDate & (0x00000001 << (4 * i))) > 0 && today.get(Calendar.DAY_OF_WEEK) <= (i + 1)) {
                        if (((i == 0 || i == 6) && intent.getStringExtra("auto").toString().compareTo("false") == 0) || (1 <= i && i <= 5)) {
                            Log.d("VALUE", i + ", " + intent.getStringExtra("auto").toString());
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
                    if ((weekOfDate & 0x00000001) > 0 && intent.getStringExtra("auto").toString().compareTo("false") == 0) {
                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    } else if ((weekOfDate & 0x01000000) > 0 && intent.getStringExtra("auto").toString().compareTo("false") == 0) {
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

                Log.d("TIME", calendar.getTimeInMillis() + "");
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

        } else if (intent.getStringExtra("Type").compareTo("Skip") == 0) {
            Log.d("REPEAT", intent.getIntExtra("repeat_no", 0) + "");
            if (intent.getIntExtra("repeat_no", 0) > 0) {

                Intent newAlarm = new Intent("com.mobile_term_project.knight.alarm.AlarmRinging");

                String reqId = "";
                if (intent.getStringExtra("time").split(":")[0].length() == 1)
                    reqId = intent.getIntExtra("Id", 0)+"0"+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];
                else if (intent.getStringExtra("time").split(":")[0].length() == 2)
                    reqId = intent.getIntExtra("Id", 0)+intent.getStringExtra("time").split(":")[0]+intent.getStringExtra("time").split(":")[1];

                newAlarm.setClass(context, MyBroadcastReceiver.class);
                newAlarm.putExtra("Id", intent.getIntExtra("Id", 0));
                newAlarm.putExtra("Title", intent.getStringExtra("Title"));
                newAlarm.putExtra("Type", "Alarm");
                newAlarm.putExtra("original_repeat_no", intent.getIntExtra("repeat_no",0));
                newAlarm.putExtra("repeat_no", intent.getIntExtra("repeat_no", 0)-1);
                newAlarm.putExtra("repeat_time", intent.getIntExtra("repeat_time", 0));
                newAlarm.putExtra("date", intent.getStringExtra("date"));
                newAlarm.putExtra("time", intent.getStringExtra("time"));
                newAlarm.putExtra("weekOfDate", intent.getIntExtra("weekOfDate", 0));
                newAlarm.putExtra("auto", intent.getStringExtra("auto"));

                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(reqId+0), newAlarm, PendingIntent.FLAG_ONE_SHOT);

                Calendar calendar = Calendar.getInstance();
                AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                if (Build.VERSION.SDK_INT >= 23) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + intent.getIntExtra("repeat_time", 0) * 60000, pendingIntent);
                }else if (Build.VERSION.SDK_INT >= 19){
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + intent.getIntExtra("repeat_time", 0) * 60000, pendingIntent);
                }
                else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + intent.getIntExtra("repeat_time", 0) * 60000, pendingIntent);
                }}

        }
    }


}
