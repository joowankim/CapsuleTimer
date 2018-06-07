package com.example.knight.a2018_mobile;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import android.support.v4.app.NotificationCompat;


/**
 * @brief MainActivity that show first app screen with alarm list
 * @author Knight
 * @date 2018.04.30
 * @version 1.0.0.1
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private ListView list;
    private AlarmAdapter alarmAdapter;
    private ImageButton medicine_search_btn;
    private EditText medicine_name_edt;
    private ImageView img;
    private Toolbar toolbar;

//    String res;
    String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    String user_id;
    String user_pw;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int flag = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    /**
     * @description java class of main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분

        alarmAdapter = new AlarmAdapter(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        medicine_name_edt = findViewById(R.id.medicine_name);  // Find edit text widget in layout
        medicine_search_btn = findViewById(R.id.medicine_search_btn);  // Find button widget in layout
        list = findViewById(R.id.list);
        img = findViewById(R.id.img);
        fab = findViewById(R.id.fab);
        //to_memo = findViewById(R.id.to_memo);  // Find button widget in layout
//        to_report = findViewById(R.id.to_report);
////        login = (Button) findViewById(R.id.login);

//        logout = findViewById(R.id.logout);
//        to_alarmList = findViewById(R.id.to_alarm_list);
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        list.setAdapter(alarmAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alarmAdapter.notifyDataSetChanged();
                JSONObject tmp = (JSONObject) alarmAdapter.getItem(position);

                switch ((int) id) {
                    case 0:
                        try {
                            Intent intent = new Intent(getApplicationContext(), showGraph.class);
                            intent.putExtra("Medicine_Name", tmp.getString("medicine_name"));
                            intent.putExtra("Medicine", tmp.toString());
                            intent.putExtra("Id", user_id);
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 1:
                        try {
                            Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                            intent.putExtra("Exist", tmp.toString());
                            getApplicationContext().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        try {
                            DB db;
                            db = new DB(getApplicationContext(), "Alarm.db", null, 1);
                            db.getWritableDatabase();
                            db.myDelete("medicine_alarm", "medicine_name = \"" + tmp.getString("medicine_name") + "\"");
                            db.close();
                            alarmAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case 3:
                        AlarmManager alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
                        Calendar c = Calendar.getInstance();

                        try {
                            String times = tmp.getString("time");
                            String[] time = times.split(" ");

                            if (tmp.get("auto") == "false")
                                return;
                            if (c.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY && c.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
                                return;

                            Log.d("AUTO_ALARM", tmp.getString("auto") + ", " + c.get(Calendar.DAY_OF_WEEK));
                            Toast.makeText(getApplicationContext(), tmp.getString("medicine_name") + " 약을 먹었습니다. 점심, 저녁 약시간에 알려드리도록 하겠습니다.", Toast.LENGTH_SHORT).show();

                            String current = String.valueOf(Calendar.getInstance().getTimeInMillis()/1000.0);

                            DB db = new DB(getApplicationContext(), "Taken.db", null, 1);
                            db.getWritableDatabase();
                            db.myInsert("medicine_taken", "medicine_name, time", "\"" + tmp.getString("medicine_name") + "\", \"" + current +"\"");
                            try {
                                db = new DB(getApplicationContext(), "Alarm.db", null, 1);
                                db.getWritableDatabase();
                                JSONArray res = new JSONArray(db.mySelect("medicine_alarm", "remain", "alarm_id = "+tmp.getInt("alarm_id")));
                                JSONObject cur = res.getJSONObject(0);
                                db.myUpdate("medicine_alarm", "remain = "+(cur.getInt("remain")-1), "alarm_id = "+tmp.getInt("alarm_id"));
                                db.close();

                                if (cur.getInt("remain")-1 < 10) {
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    NotificationCompat.Builder builder = null;

                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        int importance = NotificationManager.IMPORTANCE_HIGH;
                                        NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
                                        notificationManager.createNotificationChannel(notificationChannel);
                                        builder = new NotificationCompat.Builder(getApplicationContext(), notificationChannel.getId());
                                    } else {
                                        builder = new NotificationCompat.Builder(getApplicationContext());
                                    }

                                    builder = builder
                                            .setSmallIcon(R.drawable.ic_launcher_background)
                                            .setColor(Color.BLUE)
                                            .setContentTitle(tmp.getString("medicine_name"))
                                            .setTicker("Capsule Timer")
                                            .setContentText(tmp.getString("Title") + "약이 " + (cur.getInt("remain")-1) + "개만 남았습니다.")
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setAutoCancel(true);
                                    notificationManager.notify(123, builder.build());

                                }

                                JSONObject info = new JSONObject();
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Login_Session", MODE_PRIVATE);
                                String user_id = sharedPreferences.getString("Id", "None");
                                MySocket socket = new MySocket(Server_IP, Server_PORT);
                                info.put("Id", user_id);
                                info.put("Medicine_Name", tmp.getString("medicine_name"));
                                info.put("Date", current);
                                info.put("Type", "Medicine_Taken");
                                socket.request(info.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

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
                                intent.setClass(getApplicationContext(), MyBroadcastReceiver.class);
                                intent.putExtra("Id", tmp.getInt("alarm_id"));
                                intent.putExtra("Title", tmp.getString("medicine_name"));
                                intent.putExtra("Type", "Alarm");
                                intent.putExtra("repeat_no", tmp.getInt("repeat_no"));
                                intent.putExtra("original_repeat_no", tmp.getInt("repeat_no"));
                                intent.putExtra("repeat_time", tmp.getInt("repeat_type"));
                                intent.putExtra("date", tmp.getString("date"));
                                intent.putExtra("time", curHHMM);
                                intent.putExtra("weekOfDate", tmp.getInt("weekOfDate"));
                                intent.putExtra("auto", "true");

                                String reqId = Module.genPendingIntentId(tmp.getInt("alarm_id"), curHHMM);
                                Log.d("Alarm", curHHMM.split(":")[1]);
//                                if (curHHMM.split(":")[0].length() == 1)
//                                    reqId = tmp.getInt("alarm_id")+"0"+curHHMM.split(":")[0]+curHHMM.split(":")[1]+"0";
//                                else if (curHHMM.split(":")[0].length() == 2)
//                                    reqId = tmp.getInt("alarm_id")+curHHMM.split(":")[0]+curHHMM.split(":")[1]+"0";


                                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(reqId), intent, PendingIntent.FLAG_ONE_SHOT);

                                Log.d("DATA", currentTime.getTime()+", ");
//                                if (Build.VERSION.SDK_INT >= 23) {
//                                    alarm.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }else if (Build.VERSION.SDK_INT >= 19){
//                                    alarm.setExact(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }
//                                else {
//                                    alarm.set(AlarmManager.RTC_WAKEUP, tmpTime, pendingIntent);
//                                }
                                Module.notiVersion(alarm, currentTime, pendingIntent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }

                list.setAdapter(new AlarmAdapter(getApplicationContext()));
            }
        });

        /**
         * @description add button event click listener
         */
        medicine_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicine_name = medicine_name_edt.getText().toString();  // Get medicine name from edit text widget
                String result = "";  // String to result
                JSONObject request = new JSONObject();  // JSON Object to send request to server

                if(medicine_name.compareTo("") == 0){
                    return;
                }else {
                    MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
                    try {
                        request.put("Type", "Search_Medicine");  // Put data to create JSON
                        request.put("Name", medicine_name);
                        result = sock.request(request.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(getApplicationContext(), Show_medicine_list.class);
                    intent.putExtra("json", result);
                    startActivity(intent);
                }

                try {
                    URL url = new URL("http://106.10.40.50:5000/image/test2018-05-16-21-59-08");
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                    Bitmap bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    img.setImageBitmap(bm);
                } catch (Exception e) {
                }

            }
        });

        // 약 검색 시 키보드에서 완료 버튼 누르면 약 검색 이벤트가 완료된것
        medicine_name_edt.setImeOptions(EditorInfo.IME_ACTION_DONE);
        medicine_name_edt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionid, KeyEvent keyEvent) {
                if(actionid == EditorInfo.IME_ACTION_DONE){
                    String medicine_name = medicine_name_edt.getText().toString();  // Get medicine name from edit text widget
                    String result = "";  // String to result
                    JSONObject request = new JSONObject();  // JSON Object to send request to server

                    if(medicine_name.compareTo("") == 0){
                        return false;
                    }else {
                        MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
                        try {
                            request.put("Type", "Search_Medicine");  // Put data to create JSON
                            request.put("Name", medicine_name);
                            result = sock.request(request.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(getApplicationContext(), Show_medicine_list.class);
                        intent.putExtra("json", result);
                        startActivity(intent);
                    }

                    try {
                        URL url = new URL("http://106.10.40.50:5000/image/test2018-05-16-21-59-08");
                        URLConnection conn = url.openConnection();
                        conn.connect();
                        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                        Bitmap bm = BitmapFactory.decodeStream(bis);
                        bis.close();
                        img.setImageBitmap(bm);
                    } catch (Exception e) {
                    }
                    return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingAlarm.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * @brief set alarm list to the list view and get id&password data from sharedPreferences
     */
    @Override
    protected void onResume() {
        alarmAdapter = new AlarmAdapter(getApplicationContext());
        list.setAdapter(alarmAdapter);

        user_id = sharedPreferences.getString("Id", "None");
        user_pw = sharedPreferences.getString("Password", "None");

        super.onResume();
    }

    /**
     * @brief process back button action
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /**
     * @brief when user select menu in navigation, then execute proper action
     * @param item
     * @return boolean value
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_memo) {
            // Handle the camera action
            Intent intent = new Intent(getApplicationContext(), memo_list.class);  // Create intent and move to memo activity
            String result = "";  // String to result
            JSONObject request = new JSONObject();  // JSON Object to send request to server
            try {
                request.put("Type", "Search_Memo");  // Put data to create JSON
                request.put("User", user_id);
                request.put("Medicine_Name", "*");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            intent.putExtra("json", request.toString());
            startActivity(intent);
        } else if (id == R.id.nav_homepage) {
            Intent intent = new Intent(getApplicationContext(), AppHomepage.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            editor.remove("Id");
            editor.remove("Password");
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_email) {
            Intent intent = new Intent(getApplicationContext(), EnrollEmail.class);
            startActivity(intent);
        } else if (id == R.id.nav_appinfo) {
            Intent intent = new Intent(getApplicationContext(), AppInfo.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}