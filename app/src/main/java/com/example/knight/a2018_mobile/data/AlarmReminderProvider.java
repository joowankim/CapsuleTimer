package com.example.knight.a2018_mobile.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;



public class AlarmReminderProvider extends ContentProvider {

    public static final String LOG_TAG = AlarmReminderProvider.class.getSimpleName();

    private static final int REMINDER = 100;

    private static final int REMINDER_ID = 101;

    //UriMatcher -> 리졸버 앱에서 전달한 Uri를 분석하여 어떤 요청을 하는지 쉽게 분석해주는 클래스
    //두개의 Uri값을 비교하여 해당하는 값(약속된 값을)을 출력해주는 기능을 하는 class
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // 약속된 값을 등록할 때 사용하는 addURI()
        //addURI(String authority, String path, int code)
//        addURI 를 사용할 때 인자는  3개가 있다.
//        authority : 컨텐트 프로바이더 중에서 어느 authority는 해당하는지 구분을 할 때 사용 지정된 값이 있다.
//        path : authority 중에서 다음 path에 해당하며 path/# 으로 하는 경우 path 다음에 모든 숫자를 지칭하며 path/* 의 경우 숫자와 문자를 포함한다.
        //code  : match() 메소드 사용시 일치한다면 반환하는 정수를 의미한다

        // content://com.delaroystudios.alarmreminde/reminder-path
        sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE, REMINDER);

        // content://com.delaroystudios.alarmreminde/reminder-path/101
        sUriMatcher.addURI(AlarmReminderContract.CONTENT_AUTHORITY, AlarmReminderContract.PATH_VEHICLE + "/#", REMINDER_ID);

    }

    private AlarmReminderDbHelper mDbHelper;

    //ContentProvider 객체가 생성되면 호출됨
    @Override
    public boolean onCreate() {
        // DB class 객체 생성
        mDbHelper = new AlarmReminderDbHelper(getContext());
        return true;
    }

    // 각 함수의 첫번째 인자 uri --> provider의 uri =
    // data 소스 읽기
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
    String sortOrder) {

        // database 읽기 위한 객체 생성
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        //UriMatcher의 match 함수 인자에 Uri를 전달하면 그 결과로 Uri가 의미하는 요청 코드를 반환받는다
        //리졸버 앱에서 전달한 Uri가 어떤 요청인지 상수로 반환
        // 새로운 URI를 가져와서 등록된 URI와 비교하여 해당하는 정수값을 반환해주는 match()
        int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                cursor = database.query(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // selection에서 _ID+ "=?" --> 특정 data 하나만 읽는 cursor를 얻는 의미
                cursor = database.query(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return AlarmReminderContract.AlarmReminderEntry.CONTENT_LIST_TYPE;
            case REMINDER_ID:
                return AlarmReminderContract.AlarmReminderEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    // data 추가
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return insertReminder(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertReminder(Uri uri, ContentValues values) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //TABLE_NAME에 해당하는 database에 value넣으면 새로운 행에 대한 정수를 return함
        long id = database.insert(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //update 함수에서 데이터를 갱신하고 notifyChange 함수를 호출하면, 프로바이더는 등록된 모든 콘텐트 관찰자에게 이 사실을 알린다.
        //즉 변경 사실을 다른 모든 content provider들에게 알림
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    //data 삭제
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                rowsDeleted = database.delete(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    // data update
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case REMINDER:
                return updateReminder(uri, contentValues, selection, selectionArgs);
            case REMINDER_ID:
                selection = AlarmReminderContract.AlarmReminderEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateReminder(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateReminder(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(AlarmReminderContract.AlarmReminderEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
