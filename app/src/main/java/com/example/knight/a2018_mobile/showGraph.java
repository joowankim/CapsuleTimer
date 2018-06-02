/**
 * @brief draw graph using MPAndroidChart library
 */

package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

    Toolbar toolbar;

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


    String from = "19700101", to = "21001231";    //20180508 form
    String medicine_name;
    int times = 4;
    List list = new ArrayList();    // report information
    ArrayList<Integer[]> day  = new ArrayList<Integer[]>();
    ArrayList<Integer[]> time = new ArrayList<Integer[]>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);

        //서버 닫혀있으면 걍 꺼짐미다
        JSONObject request = new JSONObject();  // JSON Object to send request to server
        MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
        String result = "";

        intent = getIntent();
        medicine_name = intent.getStringExtra("medicine_name");

        try {
            request.put("Type", "Medicine_Record");  // Put data to create JSON
            request.put("Id", "TEST");
            request.put("Medicine_Name", medicine_name);       // 시작 날짜
            request.put("From", from);         // 종료 날짜
            request.put("To", to);

            /**
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

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

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

        // 약 먹는 횟수에 따라 그래프의 선의 개수가 달라진다
        switch(times) {
            case 1:
                selectedLine = singleLine;
                break;
            case 2:
                selectedLine = doubleLine;
                break;
            case 3:
                selectedLine = tripleLine;
                break;
            case 4:
                selectedLine = quadLine;
                break;
            default:
                selectedLine = singleLine;
        }

        // calendars
        singleCalendar = new FragmentCalendar();

        // memo list
        memoList = new FragmentMemoList();


        ViewPager pager = (ViewPager) findViewById(R.id.container);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(pager);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedLine).commit();

//        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
//        tabs.addTab(tabs.newTab().setText("그래프"));
//        tabs.addTab(tabs.newTab().setText("달력"));
//        tabs.addTab(tabs.newTab().setText("메모"));

        // 지금은 많이 사용하는게 아니라 줄 그어 진다는데 사실 잘 모르겠지만 돌아가긴 합니다


    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Graph", "Calendar", "Memo"};

        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

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

