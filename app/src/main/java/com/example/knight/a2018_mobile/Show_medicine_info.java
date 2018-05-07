package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Show_medicine_info extends AppCompatActivity {

    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    TextView medicine_result_name;
    TextView medicine_result_effect;
    TextView medicine_result_usage;
    TextView medicine_result_notice;
    ImageView medicine_result_image;
    JSONObject result;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_info);

        Intent data = getIntent();
        String url = data.getStringExtra("link");
        MySocket sock = new MySocket(Server_IP, Server_PORT);
        JSONObject request = new JSONObject();

        medicine_result_name = (TextView) findViewById(R.id.medicine_result_name);
        medicine_result_effect = (TextView) findViewById(R.id.medicine_result_effect);
        medicine_result_usage = (TextView) findViewById(R.id.medicine_result_usage);
        medicine_result_notice = (TextView) findViewById(R.id.medicine_result_notice);
        medicine_result_image = (ImageView) findViewById(R.id.medicine_result_image);

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
            medicine_result_name.setText(result.getString("name"));
            medicine_result_effect.setText(result.getString("effect"));
            medicine_result_usage.setText(result.getString("usage"));
            medicine_result_notice.setText(result.getString("notice"));
            medicine_result_image.setImageBitmap(bmp);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
