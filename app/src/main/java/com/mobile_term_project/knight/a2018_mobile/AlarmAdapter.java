package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.graphics.Color;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class AlarmAdapter extends BaseAdapter {

    public Context context;
    public DB db;
    public JSONArray result;
    public LayoutInflater inflater;
    private int idx = 0;

    public int Modul;

    Random random = new Random();
    JSONObject tmp = null;

    public int[] color = {
            Color.argb(150,239,222,239),
            Color.argb(150,222,239,255),
            Color.argb(150,184,243,184),
            Color.argb(150,255,185,0),
            Color.argb(150,255,221,166),
            Color.argb(150,255,204,204),
            Color.argb(150,187,209,232),
            Color.argb(150,140,189,237),
            Color.argb(150,255,173,197),
            Color.argb(150,204,209,255),
            Color.argb(150,168,200,249),
            Color.argb(150,184,215,255),
            Color.argb(150,220,173,103),
            Color.argb(150,236,175,181),
            Color.argb(150,255,230,90),
            Color.argb(150,255,198,165),
            Color.argb(150,236,175,181),
            Color.argb(150,240,180,105),
            Color.argb(150,109,214,109),
            Color.argb(150,203,255,117)
    };

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
        db.close();
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
        Log.d("POS", position+"");
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
                viewHolder.delete = convertView.findViewById(R.id.delete);
                viewHolder.card = convertView.findViewById(R.id.card);
                viewHolder.cardView = convertView.findViewById(R.id.cardView);

                if(tmp.getInt("weekOfDate") > 0){
                    int date = tmp.getInt("weekOfDate");
                    int everyday = 0x01111111;
                    String week_date = "";
                    if(date == everyday){
                        week_date += "매일 ";
                    }else{
                        if ((date & 0x00000010) > 0) week_date += "월 ";
                        if ((date & 0x00000100) > 0) week_date += "화 ";
                        if ((date & 0x00001000) > 0) week_date += "수 ";
                        if ((date & 0x00010000) > 0) week_date += "목 ";
                        if ((date & 0x00100000) > 0) week_date += "금 ";
                        if ((date & 0x01000000) > 0) week_date += "토 ";
                        if ((date & 0x00000001) > 0) week_date += "일 ";
                    }
                    week_date += tmp.getString("time");
                    viewHolder.date.setText(week_date);
                }else{
                    viewHolder.date.setText(tmp.getString("date") + " " + tmp.getString("time"));
                }

                if(tmp.getString("repeat").equalsIgnoreCase("true"))
                    viewHolder.repeat.setText("매 " + tmp.getString("repeat_type") + ", " + tmp.getString("repeat_no") + "회");
                else
                    viewHolder.repeat.setText("반복 없음");

                viewHolder.title.setText(tmp.getString("medicine_name"));
                viewHolder.idx = position;

                //taken button event
                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 3);
                    }
                });

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
                Modul = 0;
                String[] splitTime = tmp.getString("time").split(" ");
                for(int i=0; i<splitTime.length; i++){
                    String[] s = splitTime[i].split(":");
                    Log.i("split time", s[0] + ", " + s[1]);
                    Modul += Integer.parseInt(s[0]) + Integer.parseInt(s[1]);
                }
                Modul = Modul % 100;
                if(Modul>20)
                    Modul = Modul% 5;
                viewHolder.cardView.setCardBackgroundColor(color[Modul]);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder)convertView.getTag();
                viewHolder.title.setText(tmp.getString("medicine_name"));
                if(tmp.getInt("weekOfDate") > 0){
                    int date = tmp.getInt("weekOfDate");
                    int everyday = 0x01111111;
                    String week_date = "";
                    if(date == everyday){
                        week_date += "매일 ";
                    }else{
                        if ((date & 0x00000010) > 0) week_date += "월 ";
                        if ((date & 0x00000100) > 0) week_date += "화 ";
                        if ((date & 0x00001000) > 0) week_date += "수 ";
                        if ((date & 0x00010000) > 0) week_date += "목 ";
                        if ((date & 0x00100000) > 0) week_date += "금 ";
                        if ((date & 0x01000000) > 0) week_date += "토 ";
                        if ((date & 0x00000001) > 0) week_date += "일 ";
                    }
                    week_date += tmp.getString("time");
                    viewHolder.date.setText(week_date);
                }else{
                    viewHolder.date.setText(tmp.getString("date") + " " + tmp.getString("time"));
                }

                if(tmp.getString("repeat").equalsIgnoreCase("true"))
                    viewHolder.repeat.setText("매 " + tmp.getString("repeat_type") + ", " + tmp.getString("repeat_no") + "회");
                else
                    viewHolder.repeat.setText("반복 없음");
                viewHolder.idx = position;

                viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 3);
                    }
                });

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

                Modul = 0;
                String[] splitTime = tmp.getString("time").split(" ");
                for(int i=0; i<splitTime.length; i++){
                    String[] s = splitTime[i].split(":");
                    Modul += Integer.parseInt(s[0]) + Integer.parseInt(s[1]);
                }
                Modul = Modul % 100;
                if(Modul>20)
                    Modul = Modul% 5;
                viewHolder.cardView.setCardBackgroundColor(color[Modul]);
                convertView.setTag(viewHolder);
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
