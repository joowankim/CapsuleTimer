package com.example.knight.a2018_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Knight on 2018. 4. 30..
 *
 * Main activity
 *
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    private ListView list;
    private AlarmAdapter alarmAdapter;
    private Button medicine_search_btn;
    private EditText medicine_name_edt;
    private ImageView img;
    private Toolbar toolbar;

//    ButtonRectangle to_memo;
//    ButtonRectangle to_report;
//    ButtonRectangle login;
//    ButtonRectangle logout;
//    ButtonRectangle to_alarmList;

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
                Toast.makeText(getApplicationContext(), position+", "+id, Toast.LENGTH_SHORT).show();
                JSONObject tmp = (JSONObject) alarmAdapter.getItem(position);
                switch ((int) id) {
                    case 0:
                        try {
                            Intent intent = new Intent(getApplicationContext(), showGraph.class);
                            intent.putExtra("Medicine_Name", tmp.getString("medicine_name"));
                            getApplicationContext().startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                }
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
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

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

        /**
         * @description add button event click listener
         */
//        to_memo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), memo_list.class);  // Create intent and move to memo activity
//                String result = "";  // String to result
//                JSONObject request = new JSONObject();  // JSON Object to send request to server
//                try {
//                    request.put("Type", "Search_Memo");  // Put data to create JSON
//                    request.put("User", user_id);
//                    request.put("Medicine_Name", "*");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                intent.putExtra("json", request.toString());
//                if (flag == 0) {
//                    intent.setClass(getApplicationContext(), Login.class);
//                }
//                startActivity(intent);
//            }
//        });
//
//        /**
//         * @brief GO TO REPORT ACTIVITY@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
//         */
//
//        to_report.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), showGraph.class);
//                startActivity(intent);
//            }
//        });

//        login.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                startActivity(intent);
//            }
//        });

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (flag == 1) {
//                    Log.d("123", "1");
//                    editor.remove("Id");
//                    editor.remove("Password");
//                    editor.commit();
//                    flag = 0;
//                    logout.setText("Login");
//                } else if (flag == 0) {
//                    Log.d("123", "0");
//                    //logout.setText("Login");
//                    Intent intent = new Intent(getApplicationContext(), Login.class);
//                    startActivity(intent);
//                }
//            }
//        });

//        to_alarmList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), AlarmList.class);
//                if (flag == 0) {
//                    intent.setClass(getApplicationContext(), Login.class);
//                    Log.d("Test", "TEST");
//                }
//                startActivity(intent);
//            }
//        });

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

    @Override
    protected void onResume() {
        super.onResume();
        list.setAdapter(new AlarmAdapter(getApplicationContext()));

        user_id = sharedPreferences.getString("Id", "None");
        user_pw = sharedPreferences.getString("Password", "None");

        if (user_id.compareTo("None") == 0 || user_pw.compareTo("None") == 0) {
        }
        else {
            flag = 1;
//            logout.setText("Logout");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
            if (flag == 0) {
                intent.setClass(getApplicationContext(), Login.class);
            }
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "memo", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_setting) {
            Toast.makeText(getApplicationContext(), "setting", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            if (flag == 1) {
                Log.d("123", "1");
                editor.remove("Id");
                editor.remove("Password");
                editor.commit();
                flag = 0;
            } else if (flag == 0) {
                Log.d("123", "0");
                //logout.setText("Login");
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                //  finish();  << 로그인 화면 고쳐야 함
            }
            Toast.makeText(getApplicationContext(), "logout", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_email) {
            Toast.makeText(getApplicationContext(), "email", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_homepage) {
            Toast.makeText(getApplicationContext(), "homepage", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}