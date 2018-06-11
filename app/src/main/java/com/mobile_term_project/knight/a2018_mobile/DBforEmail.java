package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @brief Database for email information
 * @author Knight
 * @date 2018.05.31
 * @version 1.0.0.1
 */

public class DBforEmail extends SQLiteOpenHelper {

    public DBforEmail(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * @param db
     * @brief create database to store email
     * @return N/A
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table Email (" +
                "nameOfHospital text, " +
                "nameOfDoctor text, " +
                "email_doctor text);";
        db.execSQL(sql);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

}
