/**
 * @brief draw graph using MPAndroidChart library
 */

package com.example.knight.a2018_mobile;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class showGraph extends AppCompatActivity {

    private final String Server_IP = "106.10.40.50";
    private final int Server_PORT = 6000;

    Toolbar toolbar;

    // graph fragments
    FragmentSingle  singleLine;
    FragmentDouble  doubleLine;
    FragmentTriple  TripleLine;
    FragmentQuad    quadLine;

    // calendar fragments
    FragmentCalendar singleCalendar;

    // memo list fragment
    FragmentMemoList memoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_graph);
/*
//서버 닫혀있으면 걍 꺼짐미다
        JSONObject request = new JSONObject();  // JSON Object to send request to server
        MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket with server IP and PORT
        String result = "";

        try {
            request.put("Type", "graph_data");  // Put data to create JSON
            request.put("start", "0401");       // 시작 날짜
            request.put("end", "0501");         // 종료 날짜
            result = sock.request(request.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        // 약 먹는 횟수에 따라 그래프의 선의 개수가 달라진다
        singleLine  = new FragmentSingle();
        doubleLine  = new FragmentDouble();
        TripleLine  = new FragmentTriple();
        quadLine    = new FragmentQuad();

        // calendars
        singleCalendar = new FragmentCalendar();

        // memo list
        memoList = new FragmentMemoList();


        getSupportFragmentManager().beginTransaction().replace(R.id.container, singleLine).commit();

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("그래프"));
        tabs.addTab(tabs.newTab().setText("달력"));
        tabs.addTab(tabs.newTab().setText("메모"));

        // 지금은 많이 사용하는게 아니라 줄 그어 진다는데 사실 잘 모르겠지만 돌아가긴 합니다
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("그래프", "선택된 탭 : " + position);

                // 임시로 전체다 single line
                Fragment selected = null;
                if(position == 0) {
                    selected = singleLine;
                } else if (position == 1) {
                    selected = singleCalendar;
                } else if (position == 2) {
                    selected = memoList;
                }
                // fragment 바꿔줌
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }



            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}

