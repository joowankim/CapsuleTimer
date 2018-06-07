package com.example.knight.a2018_mobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @brief show memo that user write to the list view
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class memo_list extends AppCompatActivity {

    TextView textView;
    ListView listView;
    MemoListAdapter memoListAdapter;
    Context context = this;
    FloatingActionButton add;
    String request;
    String user_id;
    Intent intent = null;
    SharedPreferences sharedPreferences;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private int Edit_Request = 666;

    /**
     * @brief process open memo and upload memo that user write
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        intent = getIntent();
        request = intent.getStringExtra("json");
        memoListAdapter = new MemoListAdapter(this, request);
        add = findViewById(R.id.add);
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);

        // 리스트 생성
        listView = findViewById(R.id.listView);

        listView.setAdapter(memoListAdapter);
        // set creator

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 1:
                        // open
                        Log.d("open", String.valueOf(memoListAdapter.getItemId(position)) + " " + String.valueOf(position) + " item selecteds ");
                        JSONObject tmp = (JSONObject) memoListAdapter.getItem(position);
                        Intent tmp_intent = new Intent(getApplicationContext(), edit_memo.class);
                        tmp_intent.putExtra("memo", tmp.toString());
                        startActivityForResult(tmp_intent, Edit_Request);
                        break;
                    case 0:
                        // delete
                        MySocket sock = new MySocket(Server_IP, Server_PORT);
                        try {
                            JSONObject memo = (JSONObject) memoListAdapter.getItem(position);
                            JSONObject request = new JSONObject();
                            request.put("Type", "Delete_Memo");
                            request.put("Position", memo.getInt("id"));
                            request.put("Id", memo.getString("user"));
                            JSONObject result = new JSONObject(sock.request(request.toString()));
                            if (result.get("result").toString().compareTo("No") == 0) {
                                Toast.makeText(memo_list.this, "", Toast.LENGTH_SHORT).show();
                                break;
                            } else
                                Toast.makeText(getApplicationContext(), "삭제되었습니다.", Toast.LENGTH_LONG).show();
                            Log.d("JSON", intent.getStringExtra("json"));
                            listView.setAdapter(new MemoListAdapter(getApplicationContext(), intent.getStringExtra("json")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("delete", position + " item deleted ");
                        break;
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), writing_memo.class);
                intent.putExtra("Medicine_Name", "");
                startActivity(intent);
            }
        });
    }


    /**
     * @brief show memo to the list view
     */
    @Override
    protected void onResume() {
        listView.setAdapter(new MemoListAdapter(getApplicationContext(), request));
        super.onResume();
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
    }

}
