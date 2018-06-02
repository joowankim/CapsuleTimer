package com.example.knight.a2018_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SettingAlarm extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private DB db;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String []time = {"", "", "", "", "", ""};
    private String date = "";
    private String active = "true";
    private String repeat = "false";
    private String repeatType = "0";
    private String repeatNo = "0";
    private String auto = "true";
    private int weekOfDate = 0;
    private int timeIdx = 1;
    private int dataIdx = 1;


    public EditText alarm_name;
    public Button submit;
    public TextView dateText;
    public TextView []timeText = new TextView[6];
    public FloatingActionButton fab1;
    public FloatingActionButton fab2;
    public TextView repeatText;
    public TextView autoText;
    public TextView repeatNoText;
    public TextView repeatTypeText;
    public ToggleButton sunday;
    public ToggleButton monday;
    public ToggleButton tuesday;
    public ToggleButton wednesday;
    public ToggleButton thursday;
    public ToggleButton friday;
    public ToggleButton saturday;
    public RelativeLayout []timeLayout = new RelativeLayout[6];
    public ImageButton addTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        db.getWritableDatabase();

        alarm_name = findViewById(R.id.reminder_title);
        dateText = findViewById(R.id.set_date);
        timeText[1] = findViewById(R.id.set_time1);
        timeText[2] = findViewById(R.id.set_time2);
        timeText[3] = findViewById(R.id.set_time3);
        timeText[4] = findViewById(R.id.set_time4);
        timeText[5] = findViewById(R.id.set_time5);
        submit = findViewById(R.id.test);
        fab1 = findViewById(R.id.starred1);
        fab2 = findViewById(R.id.starred2);
        repeatText = findViewById(R.id.set_repeat);
        repeatNoText = findViewById(R.id.set_repeat_no);
        autoText = findViewById(R.id.set_auto_manual_btn);
        repeatTypeText = findViewById(R.id.set_repeat_type);
        sunday = findViewById(R.id.tgbtn_sun_repeat);
        monday = findViewById(R.id.tgbtn_mon_repeat);
        tuesday = findViewById(R.id.tgbtn_tue_repeat);
        wednesday = findViewById(R.id.tgbtn_wed_repeat);
        thursday = findViewById(R.id.tgbtn_thr_repeat);
        friday = findViewById(R.id.tgbtn_fri_repeat);
        saturday = findViewById(R.id.tgbtn_sat_repeat);
        timeLayout[2] = findViewById(R.id.time2);
        timeLayout[3] = findViewById(R.id.time3);
        timeLayout[4] = findViewById(R.id.time4);
        timeLayout[5] = findViewById(R.id.time5);
        addTime = findViewById(R.id.addTime);

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeIdx < 5) {
                    timeIdx += 1;
                    timeLayout[timeIdx].setVisibility(View.VISIBLE);
                }

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray res = null;
                JSONObject resJson = null;

                if (sunday.isChecked()) { weekOfDate = weekOfDate | 0x00000001; }
                if (monday.isChecked()) { weekOfDate = weekOfDate | 0x00000010; }
                if (tuesday.isChecked()) { weekOfDate = weekOfDate | 0x00000100; }
                if (wednesday.isChecked()) { weekOfDate = weekOfDate | 0x00001000; }
                if (thursday.isChecked()) { weekOfDate = weekOfDate | 0x00010000; }
                if (friday.isChecked()) { weekOfDate = weekOfDate | 0x00100000; }
                if (saturday.isChecked()) { weekOfDate = weekOfDate | 0x01000000; }

                try {
                    res = new JSONArray(db.mySelect("medicine", "*", "medicine_name = \""+alarm_name.getText().toString()+"\""));
                    resJson = res.getJSONObject(0);
                    resJson.get("medicine_id");
                } catch (JSONException e) {

                    db.myInsert("medicine", "medicine_name", "\""+alarm_name.getText().toString()+"\"");
                    try {
                        res = new JSONArray(db.mySelect("medicine", "*", "medicine_name = \"" + alarm_name.getText().toString() + "\""));
                        resJson = res.getJSONObject(0);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    e.printStackTrace();
                }

                try {
                    String tmpTime = "";
                    res = new JSONArray();
                    for (int i = 1; i <= timeIdx; i++) {
                        Log.d("TIME", i+", "+timeIdx);
                        if (time[i].compareTo("") != 0) {
                            tmpTime += time[i] + " ";

                            JSONObject tmpRes = new JSONObject();
                            tmpRes.put("medicine_name", alarm_name.getText().toString());
                            tmpRes.put("date", date);
                            tmpRes.put("time", time[i]);
                            tmpRes.put("repeat_no", Integer.parseInt(repeatNo));
                            tmpRes.put("repeat_time", Integer.parseInt(repeatType.split(" ")[0]));
                            tmpRes.put("weekOfDate", weekOfDate);
                            tmpRes.put("auto", auto);
                            res.put(tmpRes);

                        }
                    }
                    tmpTime.trim();
                    db.myInsert("medicine_alarm", "medicine_id, medicine_name, date, time, repeat, repeat_no, repeat_type, active, weekOfDate, auto", resJson.getInt("medicine_id") + ", \"" + alarm_name.getText().toString() + "\", "+"\""+date+"\", \""+tmpTime+"\", \""+repeat+"\", \""+repeatNo+"\", \""+repeatType+"\", \""+active+"\", "+weekOfDate+", \""+auto+"\"");
//
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlarmService alarmService = new AlarmService(getApplicationContext());
                alarmService.setFromButton(res);


                finish();
            }
        });
    }

    // 알람 아이콘 모양 바뀌게 하기
    // On clicking the active button
    public void selectFab1(View v) {
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.VISIBLE);
        this.active = "true";
    }

    // On clicking the inactive button
    public void selectFab2(View v) {
        fab2.setVisibility(View.GONE);
        fab1.setVisibility(View.VISIBLE);
        this.active = "false";
    }

    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        day = dayOfMonth;
        month = monthOfYear;
        this.year = year;
        date = dayOfMonth + "/" + monthOfYear + "/" + year;
        dateText.setText(date);
    }

    public void callSetTime1(View v) {
        dataIdx = 1;
        setTime(v);
    }

    public void callSetTime2(View v) {
        dataIdx = 2;
        setTime(v);
    }

    public void callSetTime3(View v) {
        dataIdx = 3;
        setTime(v);
    }

    public void callSetTime4(View v) {
        dataIdx = 4;
        setTime(v);
    }

    public void callSetTime5(View v) {
        dataIdx = 5;
        setTime(v);
    }

    // On clicking Time picker
    // 현재 시간을 calendar에 얻어 timepicker에 setting
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }


    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        hour = hourOfDay;
        this.minute = minute;
        if (this.minute < 10) {
            time[dataIdx] = hourOfDay + ":" + "0" + minute;
        } else {
            time[dataIdx] = hourOfDay + ":" + minute;
        }
        timeText[dataIdx].setText(time[dataIdx]);
    }

    public void onSwitchAuto(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            auto = "true";
            autoText.setText("Activate Auto Mode");
        } else {
            auto = "false";
            autoText.setText("Off");
        }
    }

    // On clicking the repeat switch
    // repeat 스위치 check 여부 확인
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            repeat = "true";
            repeatText.setText("Every " + " " + repeatType + ", " + repeatNo + "times");
        } else {
            repeat = "false";
            repeatText.setText("Off");
        }
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v){
        final String[] items = new String[5];

        items[0] = "1";
        items[1] = "2";
        items[2] = "3";
        items[3] = "4";
        items[4] = "5";

        // Create List Dialog
        // repeat list 선택하면 list dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("반복 횟수 ");
        // dialog에 있는 item setting
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                repeatNo = items[item];
                repeatNoText.setText(repeatNo);
                repeatText.setText("Every " + " " + repeatType + ", " + repeatNo + "times");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat Interval button
    // 알람 간격으로 바꿈
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "1 Minutes";
        items[1] = "10 Minutes";
        items[2] = "15 Minutes";
        items[3] = "20 Minutes";
        items[4] = "30 Minutes";

        // Create List Dialog
        // repeat list 선택하면 list dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select ");
        // dialog에 있는 item setting
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                repeatType = items[item];
                repeatTypeText.setText(repeatType);
                repeatText.setText("Every " + " " + repeatType + ", " + repeatNo + "times");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}

