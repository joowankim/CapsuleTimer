package com.example.knight.a2018_mobile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @brief
 * @author Knight
 * @date 2018.06.03
 * @version 1.0.0.1
 */

public class AppInfoGit extends AppCompatActivity {

    WebView browser;

    /**
     * @brief make web view to visit git page
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gitpage);

        browser = findViewById(R.id.webkit);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setWebViewClient(new WebViewClient());
        browser.loadUrl("https://github.com/kjw217/CapsuleTimer");

    }
}
