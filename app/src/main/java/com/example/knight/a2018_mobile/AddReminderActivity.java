package com.example.knight.a2018_mobile;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.knight.a2018_mobile.data.AlarmReminderContract;
import com.example.knight.a2018_mobile.reminder.AlarmScheduler;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;


public class AddReminderActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, LoaderManager.LoaderCallbacks<Cursor> {
    // CursorLoader는 AsyncTaskLoader의 서브 클래스로 DB 커서를 이용한다. 커서는 iterator와 거의 같고 데이터 집합을 자유롭게 순회 가능


    private static final int EXISTING_VEHICLE_LOADER = 0;


    private Toolbar mToolbar;
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText, mAutoTextView, mRemainText;
    private FloatingActionButton mFAB1;
    private FloatingActionButton mFAB2;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    public long mRepeatTime;
    private Switch mRepeatSwitch, mAutoSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mAuto;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private String mRemain;

    private boolean[] bolList = new boolean[8];

    private int sun_flag = 0;
    private int mon_flag = 0;
    private int tu_flag = 0;
    private int we_flag = 0;
    private int th_flag = 0;
    private int fri_flag = 0;
    private int sat_flag = 0;

    public Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    private ToggleButton mSunToggleButton;
    private ToggleButton mMonToggleButton;
    private ToggleButton mTueToggleButton;
    private ToggleButton mWedToggleButton;
    private ToggleButton mThrToggleButton;
    private ToggleButton mFriToggleButton;
    private ToggleButton mSatToggleButton;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "false";

    private static final String KEY_MONDAY = "false";
    private static final String KEY_TUESDAY = "false";
    private static final String KEY_WEDNESDAY = "false";
    private static final String KEY_THURSDAY = "false";
    private static final String KEY_FRIDAY = "false";
    private static final String KEY_SATURDAY = "false";
    private static final String KEY_SUNDAY = "false";

    private static final String KEY_REMAIN = "remaining";



    // Constant values in milliseconds
    private static final long milMinute = 60000L;
//    private static final long milHour = 3600000L;
//    private static final long milDay = 86400000L;
//    private static final long milWeek = 604800000L;
//    private static final long milMonth = 2592000000L;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);


        //알람 메인에서 추가버튼을 눌렀으면 Uri가 null
        //기존에 추가되었던 list item을 클릭했으면 해당 uri가 저장될것
        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();

        if (mCurrentReminderUri == null) { // floating action button 을 눌러 alarm을 처음 추가 하는 경우

            setTitle(getString(R.string.editor_activity_title_new_reminder));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a reminder that hasn't been created yet.)
            invalidateOptionsMenu();
            // 이 함수가 call 되고 나면 onPrepareOptionsMenu 가 호출됨
        } else {

            // 기존 알람을 선택한 경우
            setTitle(getString(R.string.editor_activity_title_edit_reminder));


            // initLoader을 호출하면 id를 가진 로더가 있는 지 찾아보고 있다면 해당 로더를 반환하고 없으면 새로 생성한다.
            // initLoader을 호출할 때 id에 해당하는 로더가 없다면 onCreateLoader가 호출되고 해당 메서드 안에서 로더 객체를 생성 후 반환한다
            getLoaderManager().initLoader(EXISTING_VEHICLE_LOADER, null, this);
        }


        // Initialize Views
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mAutoTextView = findViewById(R.id.set_auto_manual_btn);
        mAutoSwitch = findViewById(R.id.auto_manual_btn_switch);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        mRemainText = findViewById(R.id.set_remain_no);
        mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);

        mSunToggleButton = (ToggleButton) findViewById(R.id.tgbtn_sun_repeat);
        mMonToggleButton = (ToggleButton) findViewById(R.id.tgbtn_mon_repeat);
        mTueToggleButton = (ToggleButton) findViewById(R.id.tgbtn_tue_repeat);
        mWedToggleButton = (ToggleButton) findViewById(R.id.tgbtn_wed_repeat);
        mThrToggleButton = (ToggleButton) findViewById(R.id.tgbtn_thr_repeat);
        mFriToggleButton = (ToggleButton) findViewById(R.id.tgbtn_fri_repeat);
        mSatToggleButton = (ToggleButton) findViewById(R.id.tgbtn_sat_repeat);

        // auto/manual button 만들 것

        // toggle button event listener
        //setListener();

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "5 Minutes";
        mRemain = Integer.toString(0);

        //현재 시간 구함
        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

//        Log.i("calendar - date ", Integer.toString(mDay)); // 얘가 몇일인지 ( 21일) 이런거
//        Log.i("calendar - week day", Integer.toString(mCalendar.get(Calendar.DAY_OF_WEEK))); // 얘가 요일 (일요일이 1 월요일이 2 ~~)
//        Log.i("calendar - dayofmonth", Integer.toString(mCalendar.get(Calendar.DAY_OF_MONTH))); // 얘도 몇일인지네 (21)

        mDate = mDay + "/" + mMonth + "/" + mYear; // 00/05/2018 이런식
        mTime = mHour + ":" + mMinute;

        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            //입력하기 전에 호출
            //CharSequence s - 현재 edit text에 입력된 문자열
            //start - s에 저장된 문자열 내에 새로 추가될 문자열 위치 , ex) 12345에서 3과 4사이 선택 -> start = 3
            //count - s에 담긴 문자열 가운데 새로 사용자가 입력한 문자열에 의해 변경될 문자열 수, ex) 12345에서 45 선택 -> count = 2
            //after - 새로 추가될 문자열 수, ex) abc를 입력 -> after = 3
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            // 텍스트에 변화가 있을 때 호출
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            //입력이 끝났을때 호출
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mSunToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) sun_flag = 1; else sun_flag = 0; } });
        mMonToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) mon_flag = 2; else mon_flag = 0; } });
        mTueToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) tu_flag = 3; else tu_flag = 0; } });
        mWedToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) we_flag = 4; else we_flag = 0; } });
        mThrToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) th_flag = 5; else th_flag = 0; } });
        mFriToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) fri_flag = 6; else fri_flag = 0; } });
        mSatToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) sat_flag = 7; else sat_flag = 0; } });



        // Setup TextViews using alarm values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo); // skip눌렀을 때 반복할 횟수
        mRepeatTypeText.setText(mRepeatType);  // repeatType -> skip눌렀을때 얼마 후에 알람 울릴건지
        mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
        mRemainText.setText(mRemain);

        // To save state on device rotation
        // 이전 상태에 복구할 bundle이 있는지 여부를 확인하여 null이 아니면 복구 함
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            mTitleText.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
            mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            String savedRemainType = savedInstanceState.getString(KEY_REMAIN);
            mRemainText.setText(savedRemainType);
            mRemain = savedRemainType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);

            if(savedInstanceState.getString(KEY_SUNDAY).equals("true")) mSunToggleButton.setChecked(true);
            else mSunToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_MONDAY).equals("true")) mMonToggleButton.setChecked(true);
            else mMonToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_TUESDAY).equals("true")) mTueToggleButton.setChecked(true);
            else mTueToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_WEDNESDAY).equals("true")) mWedToggleButton.setChecked(true);
            else mWedToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_THURSDAY).equals("true")) mThrToggleButton.setChecked(true);
            else mThrToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_FRIDAY).equals("true")) mFriToggleButton.setChecked(true);
            else mFriToggleButton.setChecked(false);
            if(savedInstanceState.getString(KEY_SATURDAY).equals("true")) mSatToggleButton.setChecked(true);
            else mSatToggleButton.setChecked(false);

        }

        // Setup up active buttons
        if (mActive.equals("false")) {
            mFAB1.setVisibility(View.VISIBLE);
            mFAB2.setVisibility(View.GONE);

        } else if (mActive.equals("true")) {
            mFAB1.setVisibility(View.GONE);
            mFAB2.setVisibility(View.VISIBLE);
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.title_activity_add_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    // activity가 종료될 때 bundle에 데이터를 저장하고 activity를 재생성할 때 해당 bundle을 onCreate와 onSaveInstanceState에 모두 전달
    // 화면 가로,세로 변경 / 폰트크키, 폰트변경 / 언어 설정변경 / activity가 background에 있을 때 메모리 부족한 경우 등등
    // 미리 대처할 수 없는 경우를 대비하기 위한 method
    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        //                            (key, value)
        outState.putCharSequence(KEY_TITLE, mTitleText.getText());
        outState.putCharSequence(KEY_TIME, mTimeText.getText());
        outState.putCharSequence(KEY_DATE, mDateText.getText());
        outState.putCharSequence(KEY_REPEAT, mRepeatText.getText());
        outState.putCharSequence(KEY_REPEAT_NO, mRepeatNoText.getText());
        outState.putCharSequence(KEY_REPEAT_TYPE, mRepeatTypeText.getText());
        outState.putCharSequence(KEY_REMAIN, mRemainText.getText());
        outState.putCharSequence(KEY_ACTIVE, mActive);

        if(mSunToggleButton.isChecked()) outState.putCharSequence(KEY_SUNDAY, "true"); else outState.putCharSequence(KEY_SUNDAY, "false");
        if(mMonToggleButton.isChecked()) outState.putCharSequence(KEY_MONDAY, "true"); else outState.putCharSequence(KEY_MONDAY, "false");
        if(mTueToggleButton.isChecked()) outState.putCharSequence(KEY_TUESDAY, "true"); else outState.putCharSequence(KEY_TUESDAY, "false");
        if(mWedToggleButton.isChecked()) outState.putCharSequence(KEY_WEDNESDAY, "true"); else outState.putCharSequence(KEY_WEDNESDAY, "false");
        if(mThrToggleButton.isChecked()) outState.putCharSequence(KEY_THURSDAY, "true"); else outState.putCharSequence(KEY_THURSDAY, "false");
        if(mFriToggleButton.isChecked()) outState.putCharSequence(KEY_FRIDAY, "true"); else outState.putCharSequence(KEY_FRIDAY, "false");
        if(mSatToggleButton.isChecked()) outState.putCharSequence(KEY_SATURDAY, "true"); else outState.putCharSequence(KEY_SATURDAY, "false");

    }

    // On clicking Time picker
    // 현재 시간을 calendar에 얻어 timepicker에 setting
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    // 현재 날짜를 calendar에 얻어 datepicker에 setting
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    // timepicker에 setting한 time 얻어오기
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // Obtain date from date picker
    // datepicker에 setting한 날짜 얻어오기
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }

    // 알람 아이콘 모양 바뀌게 하기
    // On clicking the active button
    public void selectFab1(View v) {
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.GONE);
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.VISIBLE);
        mActive = "true";
    }

    // On clicking the inactive button
    public void selectFab2(View v) {
        mFAB2 = (FloatingActionButton) findViewById(R.id.starred2);
        mFAB2.setVisibility(View.GONE);
        mFAB1 = (FloatingActionButton) findViewById(R.id.starred1);
        mFAB1.setVisibility(View.VISIBLE);
        mActive = "false";
    }

    // On clicking the repeat switch
    // repeat 스위치 check 여부 확인
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    public void onSwitchAuto(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mAuto = "true";
            mAutoTextView.setText("Activate Auto Mode");
        } else {
            mAuto = "false";
            mAutoTextView.setText(R.string.repeat_off);
        }
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v){
        final String[] items = new String[5];

        items[0] = "1";
        items[1] = "2";
        items[2] = "3";
        items[3] = "4";
        items[4] = "5";

        // Create List Dialog
        // repeat list 선택하면 list dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("반복 횟수 ");
        // dialog에 있는 item setting
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatNo = items[item];
                mRepeatNoText.setText(mRepeatNo);
                mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("Enter Number");
//
//        // Create EditText box to input repeat number
//        final EditText input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        alert.setView(input);
//        alert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//
//                        if (input.getText().toString().length() == 0) {
//                            mRepeatNo = Integer.toString(1);
//                            mRepeatNoText.setText(mRepeatNo);
//                            mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
//                        }
//                        else {
//                            mRepeatNo = input.getText().toString().trim();
//                            mRepeatNoText.setText(mRepeatNo);
//                            mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
//                        }
//                    }
//                });
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                // do nothing
//            }
//        });
//        alert.show();
    }

    // On clicking repeat Interval button
    // 알람 간격으로 바꿈
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "5 Minutes";
        items[1] = "10 Minutes";
        items[2] = "15 Minutes";
        items[3] = "20 Minutes";
        items[4] = "30 Minutes";

        // Create List Dialog
        // repeat list 선택하면 list dialog 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select ");
        // dialog에 있는 item setting
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setRemainNo(View v){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");
        // Create EditText box to input remain number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRemain = Integer.toString(0);
                            Log.i("약 숫자: ", mRemain);
                            mRemainText.setText(mRemain);
                        }
                        else {
                            mRemain = input.getText().toString().trim();
                            Log.i("약 숫자: ", mRemain);
                            mRemainText.setText(mRemain);
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // ------------------------------------------------------
    // create menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_reminder, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new reminder, hide the "Delete" menu item.
        if (mCurrentReminderUri == null) {
            MenuItem menuItem = menu.findItem(R.id.discard_reminder);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            // alarm 저장 버튼이 눌린 경우
            case R.id.save_reminder:
                if (mTitleText.getText().toString().length() == 0){
                    mTitleText.setError("Reminder Title cannot be blank!");
                }
                else {
                    saveReminder();
                    finish();
                }
                return true;
            // Respond to a click on the "Delete" menu option
            // 알람 삭제 버튼이 눌린 경우
            case R.id.discard_reminder:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the reminder hasn't changed, continue with navigating up to parent activity
                // which is the {@link MainActivity}.
                if (!mVehicleHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(AddReminderActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // 저장하지 않고 alarm setting 화면 나갈 때
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // alarm을 삭제 할 때 확인하는 팝업 dialog
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the reminder.
                deleteReminder();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the reminder.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // alarm 삭제할 때
    private void deleteReminder() {
        // Only perform the delete if this is an existing reminder.
        if (mCurrentReminderUri != null) {
            // Call the ContentResolver to delete the reminder at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentreminderUri
            // content URI already identifies the reminder that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentReminderUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) { // alarm 삭제에 대한 error 처리
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // delete이 성공됬을 때
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Close the activity
        finish();
    }

    // On clicking the save button
    public void saveReminder(){

     /*   if (mCurrentReminderUri == null ) {
            // Since no fields were modified, we can return early without creating a new reminder.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return;
        }
     */

        // database에 각 key에 해당하는 value값들 put
        ContentValues values = new ContentValues();

        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);     // 몇번 반복할 건지
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);  // 몇분안에 다시 울리게 할건지
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE, mActive);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_SUNDAY, sun_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_MONDAY, mon_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_TUESDAY, tu_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_WEDNESDAY, we_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_THURSDAY, th_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_FRIDAY, fri_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_SATURDAY, sat_flag);
        values.put(AlarmReminderContract.AlarmReminderEntry.KEY_REMAIN, mRemain);


        // 여기서 toggle 버튼이 하나라도 눌리면 날짜로 alarm 시간 세팅해야 하고
        if(mSunToggleButton.isChecked()|mMonToggleButton.isChecked()|mTueToggleButton.isChecked()
                |mWedToggleButton.isChecked()|mThrToggleButton.isChecked()|mFriToggleButton.isChecked()|mSatToggleButton.isChecked())
        {
            bolList[0] = false;
            if(mSunToggleButton.isChecked()) bolList[1] = true; else bolList[1] = false;
            if(mMonToggleButton.isChecked()) bolList[2] = true; else bolList[2] = false;
            if(mTueToggleButton.isChecked()) bolList[3] = true; else bolList[3] = false;
            if(mWedToggleButton.isChecked()) bolList[4] = true; else bolList[4] = false;
            if(mThrToggleButton.isChecked()) bolList[5] = true; else bolList[5] = false;
            if(mFriToggleButton.isChecked()) bolList[6] = true; else bolList[6] = false;
            if(mSatToggleButton.isChecked()) bolList[7] = true; else bolList[7] = false;

            // day를 안넣으면 안되나;; 확인해야할듯
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);

        }
        else {// 하나라도 안눌려 있으면 user가 선택한 특정 날짜로 alarm 세팅 할 것
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);
        }
        //user가 설정한 시간을 milli초 단위로 바꿔서 저장
        long selectedTimestamp =  mCalendar.getTimeInMillis();
        //for_repeat_alarm = selectedTimestamp;

        Log.i("setting TIme", Long.toString(selectedTimestamp));

        // Check repeat type
        // 알람 인터벌로 고쳤음
        if (mRepeatType.equals("5 Minutes")) {
            mRepeatTime = 5 * milMinute;
        } else if (mRepeatType.equals("10 Minutes")) {
            mRepeatTime = 10 * milMinute;
        } else if (mRepeatType.equals("15 Minutes")) {
            mRepeatTime = 15  * milMinute;
        } else if (mRepeatType.equals("20 Minutes")) {
            mRepeatTime = 20 * milMinute;
        } else if (mRepeatType.equals("30 Minutes")) {
            mRepeatTime = 30 * milMinute;
        }

//        Log.i("repeat Number: ", mRepeatNo);
//        Log.i("test add", Integer.toString(Integer.parseInt(mRepeatNo)));
//        minute_repeat = Integer.parseInt(mRepeatNo);
//        for_reminder_uri = mCurrentReminderUri;

        // 알람 처음 setting하는 경우
        if (mCurrentReminderUri == null) {
            // This is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            // DB에 Uri 넣고 (세팅한 data 넣는거임) newUri로 반환 받음
            Uri newUri = getContentResolver().insert(AlarmReminderContract.AlarmReminderEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // newUri가 null이면 insert 실패한것
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                // null아 이나면 insert 성공 toast message 띄움
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            //alarm setting이 처음이 아닌경우 update를 해줌
            // update하고 난 후의 table의 몇번 째 행에 들어갔는지 int로 반환됨
            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // 업데이트가 정상적으로 되지 않았을 때
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                //업데이트가 정상적으로 되었을 때
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Create a new notification
        // selectedTimestamp -> 사용자가 설정한 시간
        if (mActive.equals("true")) { // 이건 알람 모양이 울리도록 선택되었을때 알람을 설정하게 만들어 놓은 것

            if(mSunToggleButton.isChecked()|mMonToggleButton.isChecked()|mTueToggleButton.isChecked()
                    |mWedToggleButton.isChecked()|mThrToggleButton.isChecked()|mFriToggleButton.isChecked()|mSatToggleButton.isChecked())
            {
                // mRepeatTime이 skip했을 때 얼마 후에 울릴지
                // bolList -> 각 toggle 버튼이 뭐가 눌렷는지 boolean arraylist임 (일 월 화 수 목 금 토 순서)
                // mCalendar -> day를 뺀/ 년,월,시,분,초가 들어가있음
                // selectedTimestamp -> 몇일인지를 뺀 나머지로 시간처리되어있음
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime, bolList, mCalendar);
            }
            else {// 하나라도 안눌려 있으면 특정 날짜로 alarm 세팅 할 것
                // 특정 날짜에 대한 시간이 설정되어있음
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            }
//            if (mRepeat.equals("true")) {
//                //반복 알람설정하는 경우
//                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
//
//            } else if (mRepeat.equals("false")) {
//                // 그냥 알람 설정하는 경우
//                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
//            }
        }
        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    // 새로운 로더 생성하는 단계
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

        // This loader will execute the ContentProvider's query method on a background thread
        // CursorLoader를 이용하여 loader 생성 -> 전달된 uri를 가리키는 loader가 반환 됨
        return new CursorLoader(this,   // Parent activity context
                mCurrentReminderUri,         // Query the content URI for the current reminder
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    //onLoadFinished 메서드는 백그라운드 작업을 완료되면 호출된다. 백그라운드 결과로 적재된 데이터에 접근할 수 있다.
    // 자료를 읽어 시스템 버퍼에 저장, 자료 읽기가 완료되면 호출이 됨
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        // DB 가리키는 커서를 처음으로 옮겨서 linear search 하고 string으로 얻어옴
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);

            int sundayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_SUNDAY);
            int mondayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_MONDAY);
            int tuesdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TUESDAY);
            int wednesdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_WEDNESDAY);
            int thursdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_THURSDAY);
            int fridayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_FRIDAY);
            int saturdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_SATURDAY);

            int remainColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REMAIN);



            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);
            String remain = cursor.getString(remainColumnIndex);

            // toggle 변수 추가해야 함

            int temp_sun = cursor.getInt(sundayColumnIndex);
            int temp_mon = cursor.getInt(mondayColumnIndex);
            int temp_tue = cursor.getInt(tuesdayColumnIndex);
            int temp_wen = cursor.getInt(wednesdayColumnIndex);
            int temp_thr = cursor.getInt(thursdayColumnIndex);
            int temp_fri = cursor.getInt(fridayColumnIndex);
            int temp_sat = cursor.getInt(saturdayColumnIndex);


            // 얻어온 string을 view에 초기화
            // Update the views on the screen with the values from the database
            mTitleText.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);
            mRepeatText.setText("Every " + " " + mRepeatType + ", " + mRepeatNo + "times");
            mRemainText.setText(remain);
            // Setup up active buttons
            // Setup repeat switch
            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

            // toggle 버튼 true / false 해야 함
            if(temp_sun != 0) mSunToggleButton.setChecked(true); else mSunToggleButton.setChecked(false);
            if(temp_mon != 0) mMonToggleButton.setChecked(true); else mMonToggleButton.setChecked(false);
            if(temp_tue != 0) mTueToggleButton.setChecked(true); else mTueToggleButton.setChecked(false);
            if(temp_wen != 0) mWedToggleButton.setChecked(true); else mWedToggleButton.setChecked(false);
            if(temp_thr != 0) mThrToggleButton.setChecked(true); else mThrToggleButton.setChecked(false);
            if(temp_fri != 0) mFriToggleButton.setChecked(true); else mFriToggleButton.setChecked(false);
            if(temp_sat != 0) mSatToggleButton.setChecked(true); else mSatToggleButton.setChecked(false);

        }
    }

    //onLoaderReset은 로더를 버릴 때 필요한 정리를 수행하는 곳이다
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}
