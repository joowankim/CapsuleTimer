package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Show_medicine_info extends AppCompatActivity {

    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    TextView medicine_result_name;
    TextView medicine_result_effect;
    TextView medicine_result_usage;
    TextView medicine_result_notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_info);

        Intent data = getIntent();
        String url = data.getStringExtra("link");
        MySocket sock = new MySocket(Server_IP, Server_PORT);
        JSONObject request = new JSONObject();
        JSONObject result;

        medicine_result_name = (TextView) findViewById(R.id.medicine_result_name);
        medicine_result_effect = (TextView) findViewById(R.id.medicine_result_effect);
        medicine_result_usage = (TextView) findViewById(R.id.medicine_result_usage);
        medicine_result_notice = (TextView) findViewById(R.id.medicine_result_notice);

        try {
            request.put("Type", "Search_Medicine");  // Put data to create JSON
            request.put("Name", url);
            result = new JSONObject(sock.request(request.toString()));
            Log.d("TEST", result.toString());
            medicine_result_name.setText(result.getString("name"));
            medicine_result_effect.setText(result.getString("effect"));
            medicine_result_usage.setText(result.getString("usage"));
            medicine_result_notice.setText(result.getString("notice"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
