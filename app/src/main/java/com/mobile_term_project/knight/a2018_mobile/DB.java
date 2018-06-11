package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @brief make database to store alarm setting information
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class DB extends SQLiteOpenHelper {

    String name;
    SQLiteDatabase db;

    public DB(Context context, String name, SQLiteDatabase.CursorFactory cursor, int version) {
        super(context, name, cursor, version);
        this.name = name;
    }

    /**
     * @brief make database for alarm and taking medicine
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        if (name.compareTo("Alarm.db") == 0) {
            String sql1 = "create table medicine(medicine_id integer primary key autoincrement, medicine_name text)";
            String sql2 = "create table medicine_alarm(alarm_id integer primary key autoincrement, medicine_id integer, medicine_name text, date text, time text, repeat text, repeat_no integer, repeat_type text, active text, weekOfDate integer, auto text, remain integer)";
            db.execSQL(sql1);
            db.execSQL(sql2);
        } else if (name.compareTo("Taken.db") == 0) {
            String sql = "create table medicine_taken(taken_medicine_id integer primary key autoincrement, medicine_name text, time real)";
            db.execSQL(sql);
        }
    }

    /**
     * @brief if database's information is changed, then update database with new version
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (name.compareTo("Taken.db") == 0) {
            String sql1 = "drop table medicine";
            String sql2 = "drop table medicine_alarm";
            db.execSQL(sql1);
            db.execSQL(sql2);
        } else if (name.compareTo("taken") == 0) {
            String sql = "drop table medicine_taken";
            db.execSQL(sql);
        }

        onCreate(db);
    }

    /**
     * @brief make cursor for write to database
     * @return SQLiteDatabase cursor
     */
    @Override
    public SQLiteDatabase getWritableDatabase() {
        db = super.getWritableDatabase();
        return super.getWritableDatabase();
    }

    /**
     * @brief delete data from database
     * @param table
     * @param query
     */
    public void myDelete(String table, String query) {
        db.execSQL("delete from "+table+" where "+query);
    }

    /**
     * @brief update database with new data
     * @param table
     * @param column
     * @param query
     */
    public void myUpdate(String table, String column, String query) {
        db.execSQL("update "+table+" set "+column+" where "+query);
    }

    /**
     * @brief search database and put data into the JSONObject
     * @param table
     * @param column
     * @param query
     * @return string value
     */
    public String mySelect(String table, String column, String query) {
        Cursor cursor = db.rawQuery("select "+column+" from " + table + " where "+query, null);
        JSONArray res = new JSONArray();
        while (cursor.moveToNext()) {
            JSONObject tmp = new JSONObject();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                int type = cursor.getType(i);
                try {
                    if (type == Cursor.FIELD_TYPE_STRING) {
                        tmp.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    else if (type == Cursor.FIELD_TYPE_INTEGER) {
                        tmp.put(cursor.getColumnName(i), cursor.getInt(i));
                    }
                    else if (type == Cursor.FIELD_TYPE_FLOAT) {
                        tmp.put(cursor.getColumnName(i), cursor.getFloat(i));
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            res.put(tmp);
        }

        return res.toString();
    }

    /**
     * @brief insert data into the proper database
     * @param table
     * @param column
     * @param value
     */
    public void myInsert(String table, String column, String value) {
        Log.d("SQL", "insert into "+table+" ("+column+") values ("+value+")");
        db.execSQL("insert into "+table+" ("+column+") values ("+value+")");

    }
}
