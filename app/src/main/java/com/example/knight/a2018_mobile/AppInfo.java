package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * Created by leejisung on 2018-06-03.
 */

public class AppInfo extends AppCompatActivity {

    private TextView textVersion;
    private TextView textGit;
    private String version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);
        textVersion = findViewById(R.id.textView2);
        textGit = findViewById(R.id.textView3);

        version = "버젼 1.0.0.0";
        textVersion.setText(version);

        textGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AppInfoGit.class);
                startActivity(intent);
            }
        });

    }

}
