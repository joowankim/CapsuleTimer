package com.example.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Random;

public class AlarmAdapter extends BaseAdapter {

    public Context context;
    public DB db;
    public JSONArray result;
    public LayoutInflater inflater;

    Random random = new Random();
    JSONObject tmp = null;

    public int[] color = {
            Color.rgb(239,222,239),
            Color.rgb(222,239,255),
            Color.rgb(184,243,184),
            Color.rgb(255,221,166),
            Color.rgb(255,204,204),
            Color.rgb(187,209,232),
            Color.rgb(255,173,197),
            Color.rgb(204,209,255),
            Color.rgb(168,200,249),
            Color.rgb(236,175,181)
    };

    public int colorsetting;

    public AlarmAdapter(Context context) {
        this.context = context;
        db = new DB(context, "Alarm.db", null, 1);
        db.getWritableDatabase();
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            result = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return result.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return result.getJSONObject(position);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        try {
            tmp = result.getJSONObject(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.alarm_item, parent, false);
                viewHolder.date = convertView.findViewById(R.id.dateTime);
                viewHolder.title = convertView.findViewById(R.id.title);
                viewHolder.repeat = convertView.findViewById(R.id.repeat);
                viewHolder.img = convertView.findViewById(R.id.img);
                viewHolder.btn = convertView.findViewById(R.id.Taken);
                viewHolder.edit = convertView.findViewById(R.id.edit);
//                viewHolder.report = convertView.findViewById(R.id.report);
                viewHolder.delete = convertView.findViewById(R.id.delete);
                viewHolder.card = convertView.findViewById(R.id.card);
                viewHolder.cardView = convertView.findViewById(R.id.cardView);

                viewHolder.date.setText(tmp.getString("date") + " " + tmp.getString("time"));
                viewHolder.title.setText(tmp.getString("medicine_name"));
                viewHolder.repeat.setText("Every " + tmp.getString("repeat_no") + " " + tmp.getString("repeat_type"));
                viewHolder.idx = position;

                viewHolder.cardView.setCardBackgroundColor(color[random.nextInt(10)]);



                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
                        Calendar c = Calendar.getInstance();

                        try {
                            JSONObject current = result.getJSONObject(position);
                            String times = current.getString("time");
                            String[] time = times.split(" ");

                            if (current.get("auto") == "false")
                                return;
                            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY || c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                                return;

                            double interval = 0.0;
                            for (int idx = 1; idx < time.length; idx ++) {
                                String []hhmm1 = time[idx].split(":");
                                String []hhmm2 = time[idx-1].split(":");
                                interval += (Double.parseDouble(hhmm1[0]) - Double.parseDouble(hhmm2[0]))*3600;
                                interval += (Double.parseDouble(hhmm1[1]) - Double.parseDouble(hhmm2[1]))*60;
                            }
                            interval *= 1000;
                            interval /= time.length - 1;

                            for (int idx = 1; idx < time.length; idx ++) {
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
                                }else if (Build.VERSION.SDK_INT >= 19){
                                    alarm.setExact(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
                                }
                                else {
                                    alarm.set(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

//                viewHolder.report.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            Intent intent = new Intent(context, showGraph.class);
//                            intent.putExtra("Medicine_Name", tmp.getString("medicine_name"));
//                            context.startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            Intent intent = new Intent(context, SettingAlarm.class);
//                            intent.putExtra("Exist", tmp.toString());
//                            context.startActivity(intent);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            db = new DB(context, "Alarm.db", null, 1);
//                            db.getWritableDatabase();
//                            db.myDelete("medicine_alarm", "medicine_name = \"" + tmp.getString("medicine_name") + "\"");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                viewHolder.report.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        ((ListView) parent).performItemClick(v, position, 0);
//                    }
//                });

                viewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 0);
                    }
                });

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 1);
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 2);
                    }
                });

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder)convertView.getTag();
                viewHolder.date.setText(tmp.getString("date") + " " + tmp.getString("time"));
                viewHolder.title.setText(tmp.getString("medicine_name"));
                viewHolder.repeat.setText("Every " + tmp.getString("repeat_no") + " " + tmp.getString("repeat_type"));
                viewHolder.idx = position;

                viewHolder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 0);
                    }
                });

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 1);
                    }
                });

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 2);
                    }
                });

                viewHolder.cardView.setCardBackgroundColor(color[random.nextInt(10)]);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    public class ViewHolder {
        public TextView title;
        public TextView date;
        public TextView repeat;
        public ImageButton report;
        public ImageButton edit;
        public ImageButton delete;
        public ImageView img;
        public Button btn;
        public int idx;
        public RelativeLayout card;
        public CardView cardView;
    };
}
