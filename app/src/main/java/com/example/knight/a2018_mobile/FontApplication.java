package com.example.knight.a2018_mobile;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "SDMiSaeng.ttf")).addBold(Typekit.createFromAsset(this, "SDMiSaeng.ttf"));
    }

}
