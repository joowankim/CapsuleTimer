package com.example.knight.a2018_mobile;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.net.Socket;


public class MainActivity extends AppCompatActivity {

    Button medicine_search_btn;
    EditText medicine_name_edt;
    String res;
//    String Server_IP="192.168.0.122";
//    private int Server_PORT=9999;
    String Server_IP="118.36.9.247";
    private int Server_PORT=6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분

        medicine_name_edt = (EditText) findViewById(R.id.medicine_name);
        medicine_search_btn = (Button) findViewById(R.id.medicine_search_btn);

        medicine_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String medicine_name = medicine_name_edt.getText().toString();
                String result = "";

                if(medicine_name.compareTo("") == 0){
                    return;
                }else {
                    MySocket sock = new MySocket(Server_IP, Server_PORT);
                    try {
                        result = sock.request(medicine_name);
                        sock.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
