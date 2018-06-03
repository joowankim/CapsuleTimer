package com.example.knight.a2018_mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by leejisung on 2018-05-31.
 *
 * Make Database to store student name and student number.
 */

public class DBforEmail extends SQLiteOpenHelper {

    public DBforEmail(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

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
