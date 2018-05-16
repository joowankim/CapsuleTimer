package com.example.knight.a2018_mobile.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;


public class AlarmReminderContract {

    private AlarmReminderContract() {}

    // content provider에 접근하기위해 외부에 공개하는 접근명
    //manifest에 authorities와 동일하게 지음
    public static final String CONTENT_AUTHORITY = "com.example.knight.a2018_mobile";

    // Content Provider에 접근하기 위한 규약 --> contnent://authority/path/id
    // authority에 접근명  ==> AndroidManifest.xml 파일에 선언한 authorities 와 동일하게 작성
    // path에 PATH_VEHICLE 넣기
    // id부분엔 raw의 id가 들어가게 되겠지 (id로 raw를 식별하는 거지) -- main에서 withAppededId 사용
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_VEHICLE = "reminder-path";

    public static final class AlarmReminderEntry implements BaseColumns {

        // 명시한 data(string)의 raw를 URI로 만드는 작업 ==> PATH_VEHICLE가 있는 raw를 uri로 가져옴
        // Provider의 데이터 중 접근하려는 데이터는 Uri로 나타낸다. Uri는 관계형 DB에서 Table이나 View와 비슷한 역할
        // CONTENT_URI = content://com.delaroystudios.alarmreminder/reminder-path
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_VEHICLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_VEHICLE;


        //---------------------------------------------------------------------------------------------------
        // DB attributes 생성
        public final static String TABLE_NAME = "vehicles";

        public final static String _ID = BaseColumns._ID;

        public static final String KEY_TITLE = "title";
        public static final String KEY_DATE = "date";
        public static final String KEY_TIME = "time";
        public static final String KEY_REPEAT = "repeat";
        public static final String KEY_REPEAT_NO = "repeat_no";
        public static final String KEY_REPEAT_TYPE = "repeat_type";
        public static final String KEY_ACTIVE = "active";

    }

    // columnName를 이용해서 cursor로 DB를 참조한후 해당 column value를 return
    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString( cursor.getColumnIndex(columnName) );
    }
}
