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
 * <p>
 * Main activity
 */

public class MainActivity extends AppCompatActivity {

    Button medicine_search_btn;
    Button to_memo;
    Button to_report;
    EditText medicine_name_edt;
    String res;
    String Server_IP = "118.36.9.247";
    private int Server_PORT = 6000;

    /**
     * @param savedInstanceState
     * @description java class of main activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분

        medicine_name_edt = (EditText) findViewById(R.id.medicine_name);  // Find edit text widget in layout
        medicine_search_btn = (Button) findViewById(R.id.medicine_search_btn);  // Find button widget in layout
        to_report = (Button) findViewById(R.id.to_report);
        to_memo = (Button) findViewById(R.id.to_memo);  // Find button widget in layout

        /**
         * @description add button event click listener
         */
        medicine_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicine_name = medicine_name_edt.getText().toString();  // Get medicine name from edit text widget
                String result = "";  // String to result
                JSONObject request = new JSONObject();  // JSON Object to send request to server
                MedicineListAdapter medicineList;

                if (medicine_name.compareTo("") == 0) {
                    return;
                } else {
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

        to_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent = new Intent(getApplicationContext(), showGraph.class);
                startActivity(intent);
            }

        });

        /**
         * @description add button event click listener
         */
        to_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), writing_memo.class);  // Create intent and move to memo activity
                startActivity(intent);
            }
        });

    }

}
