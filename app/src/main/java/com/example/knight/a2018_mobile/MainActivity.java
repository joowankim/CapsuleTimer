package com.example.knight.a2018_mobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.net.Socket;

/**
 * Created by Knight on 2018. 4. 30..
 *
 * Main activity
 *
 */

public class MainActivity extends AppCompatActivity {

    Button medicine_search_btn;
    Button to_memo;
    Button to_report;
    Button login;
    Button to_alarmList;
    EditText medicine_name_edt;
    String res;
    String Server_IP="106.10.40.50";
    private int Server_PORT=6000;

    /**
     * @description java class of main activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분

        medicine_name_edt = (EditText) findViewById(R.id.medicine_name);  // Find edit text widget in layout
        medicine_search_btn = (Button) findViewById(R.id.medicine_search_btn);  // Find button widget in layout
        to_memo = (Button) findViewById(R.id.to_memo);  // Find button widget in layout
        to_report = (Button) findViewById(R.id.to_report);
        login = (Button) findViewById(R.id.login);
        to_alarmList = findViewById(R.id.to_alarm_list);

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
            }
        });

        /**
         * @description add button event click listener
         */
        to_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), memo_list.class);  // Create intent and move to memo activity
                String result = "";  // String to result
                JSONObject request = new JSONObject();  // JSON Object to send request to server
                MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
                try {
                    request.put("Type", "Search_Memo");  // Put data to create JSON
                    request.put("User", "TEST");
                    Log.d("TEST", request.toString());
                    result = sock.request(request.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent.putExtra("json", result);
                startActivity(intent);
            }
        });

        /**
         * @brief GO TO REPORT ACTIVITY@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
         */

        to_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), showGraph.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        to_alarmList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Alarm_main.class);
                startActivity(intent);
            }
        });

    }

}
