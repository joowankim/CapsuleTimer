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
 * @brief
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
    SharedPreferences sharedPreferences;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private int Edit_Request = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        Intent intent = getIntent();
        request = intent.getStringExtra("json");
        memoListAdapter = new MemoListAdapter(this,  request);
//        textView = (TextView) findViewById(R.id.textView);
//        textView.setText("무슨 약 메모");
        add = findViewById(R.id.add);
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);

        // 리스트 생성
        listView = findViewById(R.id.listView);

        listView.setAdapter(memoListAdapter);
        // set creator

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                switch ((int)id) {
                                                    case 1:
                                                        // open
                                                        Log.d("open", String.valueOf(memoListAdapter.getItemId(position)) + " " + String.valueOf(position) + " item selecteds ");
                                                        Intent intent = new Intent(getApplicationContext(), edit_memo.class);
                                                        intent.putExtra("position", Integer.parseInt(String.valueOf(memoListAdapter.getItemId(position))));
                                                        intent.putExtra("writer", memoListAdapter.getWriter(position));
                                                        intent.putExtra("index", position);
                                                        startActivityForResult(intent, Edit_Request);
                                                        listView.clearChoices();
                                                        memoListAdapter.notifyDataSetChanged();
                                                        break;
                                                    case 0:
                                                        // delete
                                                        MySocket sock = new MySocket(Server_IP, Server_PORT);
                                                        try {
                                                            JSONObject request = new JSONObject();
                                                            request.put("Type", "Delete_Memo");
                                                            request.put("Position", Integer.parseInt(String.valueOf(memoListAdapter.getItemId(position))));
                                                            request.put("Id", memoListAdapter.getWriter(position));
                                                            JSONObject result = new JSONObject(sock.request(request.toString()));
                                                            if (result.get("result").toString().compareTo("No") == 0) {
                                                                Toast.makeText(memo_list.this, "", Toast.LENGTH_SHORT).show();
                                                                break;
                                                            } else
                                                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        memoListAdapter.delete(position);
                                                        listView.clearChoices();
                                                        memoListAdapter.notifyDataSetChanged();
                                                        Log.d("delete", position + " item deleted ");
                                                        break;
                                                }
                                            }
                                        });

        // 서버랑도 연동해서 바꿔야할 부분
        // 메모 편집, 삭제
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        // open
//                        Log.d("open", String.valueOf(memoListAdapter.getItemId(position)) + " " + String.valueOf(position) + " item selecteds ");
//                        Intent intent = new Intent(getApplicationContext(), edit_memo.class);
//                        intent.putExtra("position", Integer.parseInt(String.valueOf(memoListAdapter.getItemId(position))));
//                        intent.putExtra("writer", memoListAdapter.getWriter(position));
//                        intent.putExtra("index", position);
//                        startActivityForResult(intent, Edit_Request);
//                        listView.clearChoices();
//                        memoListAdapter.notifyDataSetChanged();
//                        break;
//                    case 1:
//                        // delete
//                        MySocket sock = new MySocket(Server_IP, Server_PORT);
//                        try {
//                            JSONObject request = new JSONObject();
//                            request.put("Type", "Delete_Memo");
//                            request.put("Position", Integer.parseInt(String.valueOf(memoListAdapter.getItemId(position))));
//                            request.put("Id", memoListAdapter.getWriter(position));
//                            JSONObject result = new JSONObject(sock.request(request.toString()));
//                            if (result.get("result").toString().compareTo("No") == 0) {
//                                Toast.makeText(getApplicationContext(), "Deleting failed", Toast.LENGTH_LONG).show();
//                                break;
//                            }
//                            else
//                                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_LONG).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        memoListAdapter.delete(position);
//                        listView.clearChoices();
//                        memoListAdapter.notifyDataSetChanged();
//                        Log.d("delete", position + " item deleted ");
//                        break;
//                }
//                // true : close the menu; false : not close the menu
//                return true;
//            }
//        });

//        // in Activity Context
//        ImageView icon = new ImageView(this); // Create an icon
//        icon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_memo));
//
//        // set action button
//        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
//                .setContentView(icon)
//                .build();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), writing_memo.class);
                startActivity(intent);
            }
        });

//        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
//
//        // menus in action button
//        ImageView text = new ImageView(this);
//        text.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_text));
//        SubActionButton addText = itemBuilder.setContentView(text).build();
//
//        ImageView photo = new ImageView(this);
//        photo.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_photo));
//        SubActionButton addPhoto = itemBuilder.setContentView(photo).build();

        // show action menu
//        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
//                .addSubActionView(addText)
//                .addSubActionView(addPhoto)
//                .attachTo(actionButton)
//                .build();
//
//        text.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), writing_memo.class);
//                startActivity(intent);
//            }
//        });
//
//        photo.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), writing_memo.class);
//                startActivity(intent);
//            }
//        });

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("TEST", String.valueOf(resultCode));
//        if (requestCode == Edit_Request && resultCode == 231) {
//            Log.d("TESTSSS", String.valueOf(data.getIntExtra("position", 0)));
//            int position = data.getIntExtra("position", 0);
//            String text = data.getStringExtra("text");
////            String image = data.getStringExtra("image");
////            memoListAdapter.change();
////            listView.clearChoices();
////            memoListAdapter.notifyDataSetChanged();
//
//        }
//    }


    @Override
    protected void onResume() {
        super.onResume();
        listView.clearChoices();
        listView = findViewById(R.id.listView);
        listView.setAdapter(new MemoListAdapter(getApplicationContext(), request));
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
    }

}
