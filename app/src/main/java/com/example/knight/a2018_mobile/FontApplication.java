package com.example.knight.a2018_mobile;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "SDMiSaeng.ttf")).addBold(Typekit.createFromAsset(this, "SDMiSaeng.ttf"));
    }

}
