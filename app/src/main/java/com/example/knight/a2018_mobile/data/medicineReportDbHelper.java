package com.example.knight.a2018_mobile.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class medicineReportDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "report.db";

    private static final int DATABASE_VERSION = 2;

    // DB 생성
    public medicineReportDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_REPORT_TABLE =  "CREATE TABLE " + medicineReportContract.medicineReportEntry.TABLE_NAME + " ("
                + medicineReportContract.medicineReportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + medicineReportContract.medicineReportEntry.KEY_TITLE + " TEXT NOT NULL, "
                + medicineReportContract.medicineReportEntry.KEY_YEAR + " TEXT NOT NULL, "
                + medicineReportContract.medicineReportEntry.KEY_MONTH + " TEXT NOT NULL, "
                + medicineReportContract.medicineReportEntry.KEY_DATE + " TEXT NOT NULL, "
                + medicineReportContract.medicineReportEntry.KEY_TIME + " TEXT NOT NULL " + " );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_REPORT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
