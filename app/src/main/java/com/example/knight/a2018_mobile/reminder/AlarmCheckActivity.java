package com.example.knight.a2018_mobile.reminder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.knight.a2018_mobile.R;

/**
 * Created by leejisung on 2018-05-14.
 */

public class AlarmCheckActivity extends AppCompatActivity{

    private Button check_btn;
    private Button skip_btn;

    private int index = 0;
    private int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_check);

        check_btn = findViewById(R.id.check_btn);
        skip_btn = findViewById(R.id.skip_btn);

        check_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                flag = 1; // 약 먹음
            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(index == 3){
                    flag = 0; // 약 안먹음
                }
                else index++;
            }
        });

    }
}
