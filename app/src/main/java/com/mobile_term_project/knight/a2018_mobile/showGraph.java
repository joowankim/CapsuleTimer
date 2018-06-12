/**
 * @brief generate report using MPAndroidChart library and Material Calendar View library
 * @author Joo wan Kim
 * @date 2018.05.12
 * @version 1.0.0.1
 */

package com.mobile_term_project.knight.a2018_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class showGraph extends AppCompatActivity {

    private final String Server_IP = "106.10.40.50";
    private final int Server_PORT = 6000;

    TextView dosage;
    TextView remainder;
    TextView repeatAlarm;
    TextView repeatInterval;
    TextView alarmType;
    TextView alarmDate;
    TextView weekDay;
    TextView medi_name;

    TextView dosageLabel;
    TextView remainderLabel;
    TextView repeatAlarmLabel;
    TextView repeatIntervalLabel;
    TextView alarmTypeLabel;
    TextView alarmDateLabel;
    TextView weekDayLabel;

    Toolbar toolbar;

    // sending email image
    Button sendReport;

    // graph fragments
    FragmentSingle  singleLine;
    FragmentDouble  doubleLine;
    FragmentTriple  tripleLine;
    FragmentQuad    quadLine;

    Fragment selectedLine;

    Intent intent;

    // calendar fragments
    FragmentCalendar singleCalendar;

    // memo list fragment
    FragmentMemoList memoList;

    // for sending email
    SQLiteDatabase db;
    DBforEmail helper;
    String[] emailInfo;

    // for bottom sheet
    LinearLayout bottomSheet;
    BottomSheetBehavior bottomSheetBehavior;

    String user_id;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String from = "19700101", to = "21001231";    //20180508 form
    String medicine_name;
    int times = 1;
    List list = new ArrayList();    // report information
    ArrayList<Integer[]> day  = new ArrayList<Integer[]>();
    ArrayList<Integer[]> time = new ArrayList<Integer[]>();

    /**
     * @brief draw report layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);

        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "None");

        dosage = findViewById(R.id.dosage);
        remainder = findViewById(R.id.remainder);
        repeatAlarm = findViewById(R.id.repeatAlarm);
        repeatInterval = findViewById(R.id.repeatInterval);
        alarmType = findViewById(R.id.alarmType);
        alarmDate = findViewById(R.id.date);
        weekDay = findViewById(R.id.weekDay);
        medi_name = findViewById(R.id.medi_name);

        dosageLabel = findViewById(R.id.dosage_label);
        remainderLabel = findViewById(R.id.remainder_label);
        repeatAlarmLabel = findViewById(R.id.repeatAlarm_label);
        repeatIntervalLabel = findViewById(R.id.repeatInterval_label);
        alarmTypeLabel = findViewById(R.id.alarmType_label);
        alarmDateLabel = findViewById(R.id.date_label);
        weekDayLabel = findViewById(R.id.weekDay_label);

        // sending email button
        sendReport = findViewById(R.id.sendReport);

        JSONObject request = new JSONObject();  // JSON Object to send request to server
        MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
        String result = "";

        intent = getIntent();
        medicine_name = intent.getStringExtra("Medicine_Name");
        medi_name.setText(medicine_name);

        try {

            JSONObject medicine = new JSONObject(intent.getStringExtra("Medicine"));
            times = medicine.getString("time").split(" ").length;

            dosage.setText("1일 "+medicine.getString("time").split(" ").length+"회");
            //Todo
            // DB에 remainder 추가해서 ...
            // remainder.setText
            if (medicine.getString("repeat").compareTo("true") == 0) {
                repeatAlarm.setText(medicine.getString("repeat_no")+" 회");
                repeatInterval.setText(medicine.getString("repeat_type"));
            } else {
                repeatAlarm.setVisibility(View.GONE);
                repeatInterval.setVisibility(View.GONE);
                repeatAlarmLabel.setVisibility(View.GONE);
                repeatIntervalLabel.setVisibility(View.GONE);
            }
            if (medicine.getString("auto").compareTo("false") == 0)
                alarmType.setText("수동");
            else
                alarmType.setText("자동");
            if (medicine.getString("date").compareTo("") == 0) {
                alarmDate.setVisibility(View.GONE);
                alarmDateLabel.setVisibility(View.GONE);
            }
            else {
                String []tmp = medicine.getString("date").split("/");
                alarmDate.setText(tmp[0]+"년 "+tmp[1]+"월 "+tmp[2]+"일");
            }
            if (medicine.getInt("weekOfDate") > 0) {
                String res = "";
                int tmp = medicine.getInt("weekOfDate");
                if ((tmp & 0x00000010) > 0) { res += " 월"; }
                if ((tmp & 0x00000100) > 0) { res += " 화"; }
                if ((tmp & 0x00001000) > 0) { res += " 수"; }
                if ((tmp & 0x00010000) > 0) { res += " 목"; }
                if ((tmp & 0x00100000) > 0) { res += " 금"; }
                if ((tmp & 0x01000000) > 0) { res += " 토"; }
                if ((tmp & 0x00000001) > 0) { res += " 일"; }
                weekDay.setText(res);
            } else {
                weekDay.setVisibility(View.GONE);
                weekDayLabel.setVisibility(View.GONE);
            }
            remainder.setText(medicine.getInt("remain")+"개 남았습니다.");

            request.put("Type", "Medicine_Record");  // Put data to create JSON
            request.put("Id", intent.getStringExtra("Id"));
            request.put("Medicine_Name", medicine.getString("medicine_name"));       // 시작 날짜
            request.put("From", from);         // 종료 날짜
            request.put("To", to);

            /**
             * FORMAT
             * getString("Date") : "2018-05-09 01:18:24"
             *            User   : "TEST"
             *            Medicine_Name : "Tylenol"
             */
            result = sock.request(request.toString());

            if(result != null) {
                JSONObject res = new JSONObject(result);
                JSONArray res_arr = res.getJSONArray("record");
                JSONObject report_info;

                for (int i = 0; i < res_arr.length(); i++) {
                    report_info = res_arr.getJSONObject(i);
                    list.add(report_info.getString("Date"));
                }

            } else {
                Toast.makeText(getApplicationContext(), "정보를 받아오지 못했습니다", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "서버와 연결할 수 없습니다", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        singleLine  = new FragmentSingle();
        doubleLine  = new FragmentDouble();
        tripleLine  = new FragmentTriple();
        quadLine    = new FragmentQuad();

        for (Object object : list) {
            String element = (String) object;
            String[] t = element.split(" ");

            String[] temp_day = t[0].split("-");
            String[] temp_time = t[1].split(":");

            day.add(new Integer[]{Integer.parseInt(temp_day[0]),Integer.parseInt(temp_day[1]),Integer.parseInt(temp_day[2])});
            time.add(new Integer[]{Integer.parseInt(temp_time[0]),Integer.parseInt(temp_time[1]),Integer.parseInt(temp_time[2])});
        }

        Log.d("DAY", day.toString());
        Log.d("TIME", time.toString());

        // 약 먹는 횟수에 따라 그래프의 선의 개수가 달라진다
        switch(times) {
            case 1:
                Log.d("taken", "1");
                selectedLine = singleLine;
                break;
            case 2:
                Log.d("taken", "2");
                selectedLine = doubleLine;
                break;
            case 3:
                Log.d("taken", "3");
                selectedLine = tripleLine;
                break;
            case 4:
                Log.d("taken", "4");
                selectedLine = quadLine;
                break;
            default:
                selectedLine = singleLine;
        }

        // calendars
        singleCalendar = new FragmentCalendar();

        // memo list
        memoList = new FragmentMemoList();

        bottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        ViewPager pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);

        /**
         * sending report to doctor
         */
        helper = new DBforEmail(this, "Email.db", null, 2);
        registerForContextMenu(sendReport);

    }

    /**
     * @brief showing doctors' E-mail list
     * @param menu
     * @param v
     * @param menuInfo
     * @return N/A
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);

        // generate the E-mail list object from database
        db = helper.getReadableDatabase();
        Cursor c = db.query("Email", null, null, null, null, null, null);

        // get the E-mail list into emailInfo
        emailInfo = new String[c.getCount()];
        int count = 0;
        while (c.moveToNext()) {
            emailInfo[count] = c.getString(c.getColumnIndex("email_doctor"));
            count++;
        }
        c.close();

        // when the E-mail exists
        if(emailInfo.length > 0) {
            menu.setHeaderTitle("Emails");
            for (int i = 0; i < emailInfo.length; i++) {
                menu.add(0, i, i, emailInfo[i]);
            }
        } else {
            Toast.makeText(getApplicationContext(), "수신자의 메일주소를 등록해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @brief when menu's item selected, then change the button's color with selected menu.
     * @param item selected item in E-mail list
     * @return
     */
    public boolean onContextItemSelected(MenuItem item){
        // get the ID of selected item
        int id = item.getItemId();

        MySocket sock = new MySocket(Server_IP, Server_PORT);
        try {
            JSONObject req = new JSONObject();
            req.put("Type", "Create_Instant_Report");
            req.put("Id", user_id);
            req.put("Hash", Module.MD5(user_id+medicine_name));
            req.put("Medicine_Name", medicine_name);
            sock.request(req.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // inflate sending E-mail activity set from and to
        Intent it = new Intent(Intent.ACTION_SEND);
        String[] address = {emailInfo[id]};
        it.putExtra(Intent.EXTRA_EMAIL, address);
        it.putExtra(Intent.EXTRA_TEXT, "레포트를 확인하려면 아래 링크를 클릭하세요\r\n\r\n" +
                "http://106.10.40.50:5000/report/instant/"+Module.MD5(user_id+medicine_name));
        it.putExtra(Intent.EXTRA_SUBJECT, "[Report] " + intent.getStringExtra("Id") + "의 복용기록");
        it.setType("message/rfc822");
        startActivity(Intent.createChooser(it, "Choose Email Client"));

        return super.onContextItemSelected(item);
    }

    /**
     * @brief enable swipe UX and generate tab UI
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Graph", "Calendar", "Memo"};

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * @brief get each page title
         * @param position
         * @return page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        /**
         * @brief count titles
         * @return title size
         */
        @Override
        public int getCount() {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            return TITLES.length;
        }

        /**
         * @brief assign each item
         * @param position index of item
         * @return fragment
         */
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return selectedLine;
            } else if (position == 1) {
                return singleCalendar;
            } else {
                return memoList;
            }
        }
    }
}
