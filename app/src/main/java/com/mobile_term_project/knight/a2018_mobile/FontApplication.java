package com.mobile_term_project.knight.a2018_mobile;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

/**
 * @brief changing fonts in this application
 * @author Joo wan Kim
 * @date 2018.06.01
 * @version 1.0.0.1
 */

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance().addNormal(Typekit.createFromAsset(this, "SDMiSaeng.ttf")).addBold(Typekit.createFromAsset(this, "SDMiSaeng.ttf"));
    }

}
