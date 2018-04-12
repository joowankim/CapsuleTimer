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
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btn;
    CheckBox chk1;
    CheckBox chk2;
    TextView status;
    RadioGroup radio;
    RadioButton radio1;
    RadioButton radio2;
    RadioButton radio3;

    String n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 이 부분이 XML을 자바 객체로 변경해주는 부분
        btn = (Button)findViewById(R.id.btn);
        chk1 = (CheckBox)findViewById(R.id.chk1);
        chk2 = (CheckBox)findViewById(R.id.chk2);
        radio = (RadioGroup)findViewById(R.id.radio);
        radio1 = (RadioButton)findViewById(R.id.radio1);
        radio2 = (RadioButton)findViewById(R.id.radio2);
        radio3 = (RadioButton)findViewById(R.id.radio3);
//        status = (TextView)findViewById(R.id.status);
        btn.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view){
                n = "Coffee";

                int id = radio.getCheckedRadioButtonId();
                if(radio1.getId() == id){
                    n = "Decaf " + n;
                }
                else if(radio2.getId() == id){
                    n = "Espresso " + n;
                }
                else if(radio3.isSelected()){
                    n = "Columbian " + n;
                }

                if(chk1.isChecked())
                {
                    n += " & "+"Cream";
                }
                if(chk2.isChecked()){
                    n += " & "+"Sugar";
                }

                Toast.makeText(getApplicationContext(), n, Toast.LENGTH_LONG).show();
            }
        });

    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//        Log.d(tag, "In the onStart() event");
//
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        Log.d(tag, "In the onStop() event");
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Log.d(tag, "In the onDestroy() event");
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Log.d(tag, "In the onPause() event");
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(tag, "In the onResume() event");
//    }
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Log.d(tag, "In the onRestart() event");
//    }
}


// 네?