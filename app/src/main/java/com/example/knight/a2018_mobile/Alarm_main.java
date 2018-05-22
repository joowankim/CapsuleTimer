package com.example.knight.a2018_mobile;

import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.knight.a2018_mobile.data.AlarmReminderContract;
import com.example.knight.a2018_mobile.data.AlarmReminderDbHelper;

public class Alarm_main extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddReminderButton;
    private Toolbar mToolbar;
    AlarmCursorAdapter mCursorAdapter;
    AlarmReminderDbHelper alarmReminderDbHelper = new AlarmReminderDbHelper(this);
    ListView reminderListView;
    ProgressDialog prgDialog;

    private static final int VEHICLE_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        reminderListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        // setEmptyview --> list가 비어있으면 eptyView를 보여주고 item이 있으면 item 보여줌
        reminderListView.setEmptyView(emptyView);

        // AlarmCursorAdapter에서 설정하는 alarm_item.xml을 list형태로 보여줌
        mCursorAdapter = new AlarmCursorAdapter(this, null);
        reminderListView.setAdapter(mCursorAdapter);

        //list에 있는 alarm 선택할 때
        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(Alarm_main.this, AddReminderActivity.class);

                // ContentUris.withAppendedId --> ID의 raw를 Uri로 만듦 / 인자로 전달된 id의 해당하는 raw를 uri로 가져오게 됨
                Uri currentVehicleUri = ContentUris.withAppendedId(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri); // 전달받은 uri raw를 intent에 setting한 후 AddRemiderActivity에 넘겨줌

                startActivity(intent);

            }
        });


        // 새로 alarm을 추가하는 button
        mAddReminderButton = (FloatingActionButton) findViewById(R.id.fab);
        mAddReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddReminderActivity.class);
                startActivity(intent);
            }
        });

        //loader 초기화
        getLoaderManager().initLoader(VEHICLE_LOADER, null, this);


    }

    // loader가 없는 경우 loader 생성
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                AlarmReminderContract.AlarmReminderEntry._ID,
                AlarmReminderContract.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderContract.AlarmReminderEntry.KEY_DATE,
                AlarmReminderContract.AlarmReminderEntry.KEY_TIME,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE,
                AlarmReminderContract.AlarmReminderEntry.KEY_SUNDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_MONDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_TUESDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_WEDNESDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_THURSDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_FRIDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_SATURDAY,
                AlarmReminderContract.AlarmReminderEntry.KEY_REMAIN
        };

        return new CursorLoader(this,   // Parent activity context
                AlarmReminderContract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }
}
