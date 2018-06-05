package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class Show_medicine_info extends AppCompatActivity {

    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    TextView medicine_result_name;
    ImageView medicine_result_image;
    ExpandableListView expandableListView;  // 확장형 리스트 선언
    JSONObject result;
    Bitmap bmp;

    // arrayGroup : 효능/효과, 사용법, 주의사항 같은 큰 범주 목록
    // HashMap을 사용해서 arrayChild를 통해 큰 범주에 들어가는 세부사항 리스트화
    // 효능이나 사용법은 세부사항 하나로 가능하나
    // 주의사항은 너무 많아서 나눌 필요가 있어보임
    private ArrayList<String> arrayGroup = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_info);

        Intent data = getIntent();
        String url = data.getStringExtra("link");
        MySocket sock = new MySocket(Server_IP, Server_PORT);
        JSONObject request = new JSONObject();

        medicine_result_name = (TextView) findViewById(R.id.medicine_result_name);
        medicine_result_image = (ImageView) findViewById(R.id.medicine_result_image);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);    // 확장형 리스트 초기화

        try {
            request.put("Type", "Search_Medicine");  // Put data to create JSON
            request.put("Name", url);
            result = new JSONObject(sock.request(request.toString()));
            Log.d("TEST", Uri.parse("http://drug.mfds.go.kr" + result.getString("image")).toString());
            Thread getBitmap = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://drug.mfds.go.kr" + result.getString("image"));
                        HttpURLConnection image_http = (HttpURLConnection) url.openConnection();
                        image_http.setDoInput(true);
                        image_http.connect();

                        InputStream is = image_http.getInputStream();
                        bmp = BitmapFactory.decodeStream(is);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    super.run();
                }
            };

            getBitmap.start();
            try {
                getBitmap.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 약 이름 세팅
            medicine_result_name.setText(result.getString("name"));
            // 이미지 세팅
            medicine_result_image.setImageBitmap(bmp);


            // 각각의 범주를 ArrayList의 add method를 사용하여 범주 목록 추가
            arrayGroup.add("효능/효과");
            arrayGroup.add("사용법");
            arrayGroup.add("주의사항");

            // 각각의 범주들에 세부사항들로 추가할 리스트들 생성
            ArrayList<String> result_effect = new ArrayList<String>();
            ArrayList<String> result_usage = new ArrayList<String>();
            ArrayList<String> result_notice = new ArrayList<String>();

            // 세부사항들을 각각의 리스트에 추가
            result_effect.add(result.getString("effect"));
            result_usage.add(result.getString("usage"));
            result_notice.add(result.getString("notice"));

            // HashMap을 통해 arrayChild로 arrayGroup과 위에서 생성한 세부사항 리스트들을 연결시킴
            arrayChild.put(arrayGroup.get(0), result_effect);
            arrayChild.put(arrayGroup.get(1), result_usage);
            arrayChild.put(arrayGroup.get(2), result_notice);

            // adapter 설정
            expandableListView.setAdapter(new expandableAdapter(this, arrayGroup, arrayChild));



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
