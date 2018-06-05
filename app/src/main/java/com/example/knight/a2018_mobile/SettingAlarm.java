package com.example.knight.a2018_mobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
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

//씀

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
    private String mRemain = "0";
    private String exist = "";
    private int alarm_id = 0;
    private int medicine_id = 0;
    private int weekOfDate = 0;
    private int timeIdx = 1;
    private int dataIdx = 1;
    private Intent intent = null;
    private String user_id;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    SharedPreferences sharedPreferences;


    public EditText alarm_name;
    public Button submit;
    public TextView dateText;
    public TextView []timeText = new TextView[6];
    public FloatingActionButton fab2;
    public TextView repeatText;
    public TextView autoText;
    public TextView repeatNoText;
    public TextView repeatTypeText;
    public TextView remainText;
    public ToggleButton sunday;
    public ToggleButton monday;
    public ToggleButton tuesday;
    public ToggleButton wednesday;
    public ToggleButton thursday;
    public ToggleButton friday;
    public ToggleButton saturday;
    public RelativeLayout []timeLayout = new RelativeLayout[6];
    public RelativeLayout repeat_No, repeat_interval;
    public ImageButton addTime;
    public Switch aSwitch1, aSwitch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        db.getWritableDatabase();
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "None");

        intent = getIntent();
        try {
            Log.d("INTENT", intent.getStringExtra("Exist"));
            exist = intent.getStringExtra("Exist");
        } catch (Exception e) {
            exist = "";
            e.printStackTrace();
        }

        alarm_name = findViewById(R.id.reminder_title);
        dateText = findViewById(R.id.set_date);
        timeText[1] = findViewById(R.id.set_time1);
        timeText[2] = findViewById(R.id.set_time2);
        timeText[3] = findViewById(R.id.set_time3);
        timeText[4] = findViewById(R.id.set_time4);
        timeText[5] = findViewById(R.id.set_time5);
        submit = findViewById(R.id.test);
        fab2 = findViewById(R.id.starred2);
        repeatText = findViewById(R.id.set_repeat);
        repeatNoText = findViewById(R.id.set_repeat_no);
        autoText = findViewById(R.id.set_auto_manual_btn);
        repeatTypeText = findViewById(R.id.set_repeat_type);
        remainText = findViewById(R.id.set_remain_no);
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
        repeat_No = findViewById(R.id.RepeatNo);
        repeat_interval = findViewById(R.id.RepeatInterval);
        addTime = findViewById(R.id.addTime);
        aSwitch1 = findViewById(R.id.repeat_switch);
        aSwitch2 = findViewById(R.id.auto_manual_btn_switch);

        repeat_No.setClickable(false);
        repeat_interval.setClickable(false);

        if (exist.compareTo("") != 0) {
            try {
                JSONObject tmp = new JSONObject(exist);
                alarm_name.setText(tmp.getString("medicine_name"));
                alarm_id = tmp.getInt("alarm_id");
                medicine_id = tmp.getInt("medicine_id");
                if (tmp.getString("date") != "")
                    dateText.setText(tmp.getString("date"));
                int nVisible = tmp.getString("time").split(" ").length;
                for (int i = 2; i < nVisible; i++)
                    timeLayout[i].setVisibility(View.VISIBLE);
                for (int i = 1; i < nVisible; i++)
                    timeText[i].setText(tmp.getString("time").split(" ")[i]);
                if (tmp.getString("repeat").compareTo("true") == 0) {
                    repeatText.setText("켜짐");
                    repeat = "true";
                    aSwitch1.setChecked(true);
                }
                if (tmp.getInt("weekOfDate") > 0) {
                    int date = tmp.getInt("weekOfDate");
                    if ((date & 0x00000001) > 0) {
                        sunday.setChecked(true);
                        sunday.setTextColor(Color.RED); sunday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x00000010) > 0) {
                        monday.setChecked(true);
                        monday.setTextColor(Color.RED); monday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x00000100) > 0) {
                        tuesday.setChecked(true);
                        tuesday.setTextColor(Color.RED); tuesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x00001000) > 0) {
                        wednesday.setChecked(true);
                        wednesday.setTextColor(Color.RED); wednesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x00010000) > 0) {
                        thursday.setChecked(true);
                        thursday.setTextColor(Color.RED); thursday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x00100000) > 0) {
                        friday.setChecked(true);
                        friday.setTextColor(Color.RED); friday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                    if ((date & 0x01000000) > 0) {
                        saturday.setChecked(true);
                        saturday.setTextColor(Color.RED); saturday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));
                    }
                }
                if (tmp.getString("auto").compareTo("false") == 0) {
                    autoText.setText("꺼짐");
                    auto = "false";
                    aSwitch2.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        sunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {sunday.setTextColor(Color.RED); sunday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {sunday.setTextColor(Color.rgb(21,21,21)); sunday.setBackgroundColor(Color.rgb(255,255,255));} } });
        monday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {monday.setTextColor(Color.RED); monday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {monday.setTextColor(Color.rgb(21,21,21)); monday.setBackgroundColor(Color.rgb(255,255,255));} } });
        tuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {tuesday.setTextColor(Color.RED); tuesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {tuesday.setTextColor(Color.rgb(21,21,21)); tuesday.setBackgroundColor(Color.rgb(255,255,255));} } });
        wednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {wednesday.setTextColor(Color.RED); wednesday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {wednesday.setTextColor(Color.rgb(21,21,21)); wednesday.setBackgroundColor(Color.rgb(255,255,255));} } });
        thursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {thursday.setTextColor(Color.RED); thursday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {thursday.setTextColor(Color.rgb(21,21,21)); thursday.setBackgroundColor(Color.rgb(255,255,255));} } });
        friday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {friday.setTextColor(Color.RED); friday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {friday.setTextColor(Color.rgb(21,21,21)); friday.setBackgroundColor(Color.rgb(255,255,255));} } });
        saturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {saturday.setTextColor(Color.RED); saturday.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_for_toggle));}
                else {saturday.setTextColor(Color.rgb(21,21,21)); saturday.setBackgroundColor(Color.rgb(255,255,255));} } });


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

                if (timeText[1].getText().toString().compareTo("") == 0 || remainText.getText().toString().compareTo("") == 0 || alarm_name.getText().toString().compareTo("") == 0) {
                    Toast.makeText(getApplicationContext(), "알람 이름, 알람 시간, 남은 약 개수는 반드시 입력해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

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
                        MySocket socket = new MySocket(Server_IP, Server_PORT);
                        JSONObject req = new JSONObject();
                        req.put("Id", user_id);
                        req.put("Medicine_Name", alarm_name.getText().toString());
                        req.put("Type", "Add_Medicine");
                        socket.request(req.toString());
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
                    Log.d("CHECK_INSERT", exist);
                    if (exist.compareTo("") == 0)
                        db.myInsert("medicine_alarm", "medicine_id, medicine_name, date, time, repeat, repeat_no, repeat_type, active, weekOfDate, auto, remain", resJson.getInt("medicine_id") + ", \"" + alarm_name.getText().toString() + "\", "+"\""+date+"\", \""+tmpTime+"\", \""+repeat+"\", \""+repeatNo+"\", \""+repeatType+"\", \""+active+"\", "+weekOfDate+", \""+auto+"\", "+remainText.getText());
                    else
                        db.myUpdate("medicine_alarm", "medicine_id = " + medicine_id + ", medicine_name = \"" + alarm_name.getText().toString() + "\", date = "+"\""+date+"\", time = \""+tmpTime+"\", repeat = \""+repeat+"\", repeat_no = \""+repeatNo+"\", repeat_type = \""+repeatType+"\", active = \""+active+"\", weekOfDate = "+weekOfDate+", auto = \""+auto+"\", remain = "+remainText.getText(), "alarm_id="+alarm_id);
//
                    res = new JSONArray(db.mySelect("medicine_alarm", "*", "medicine_name = \""+alarm_name.getText().toString()+"\""));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlarmService alarmService = new AlarmService(getApplicationContext());
                alarmService.setFromButton(res);
                finish();
            }
        });
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
        date = year + "/" + monthOfYear + "/" + dayOfMonth;
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
            autoText.setText("켜짐");
        } else {
            auto = "false";
            autoText.setText("꺼짐");
        }
    }

    // On clicking the repeat switch
    // repeat 스위치 check 여부 확인
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            repeat_No.setClickable(true);
            repeat_interval.setClickable(true);
            repeat = "true";
            repeatText.setText("매 " + repeatType + ", " + repeatNo + "회");
        } else {
            repeat_No.setClickable(false);
            repeat_interval.setClickable(false);
            repeat = "false";
            repeatText.setText("꺼짐");
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
                repeatText.setText("매 " + " " + repeatType + ", " + repeatNo + "회");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat Interval button
    // 알람 간격으로 바꿈
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "1 분";
        items[1] = "10 분";
        items[2] = "15 분";
        items[3] = "20 분";
        items[4] = "30 분";

        // Create List Dialog
        // repeat list 선택하면 list dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select ");
        // dialog에 있는 item setting
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                repeatType = items[item];
                repeatTypeText.setText(repeatType);
                repeatText.setText("매 " + repeatType + ", " + repeatNo + "회");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setRemainNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("숫자를 입력하세요");
        // Create EditText box to input remain number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRemain = Integer.toString(0);
                            Log.i("약 숫자: ", mRemain);
                            remainText.setText(mRemain);
                        }
                        else {
                            mRemain = input.getText().toString().trim();
                            Log.i("약 숫자: ", mRemain);
                            remainText.setText(mRemain);
                        }
                    }
                });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

}

