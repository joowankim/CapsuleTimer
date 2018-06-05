package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class edit_memo extends AppCompatActivity {

    ImageView memo_edit_image;
    EditText memo_edit_content;
    Button memo_edit_submit;
    TextView medicine_label;
    Spinner spinner;

    int position;
    int flag = 0;
    String writer;
    String user_id;
    SharedPreferences sharedPreferences;
    public static final int PICK_IMAGE = 1;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private Uri selectedimg;
    private int index;
    Bitmap bmp;
    DB db;
    MySocket sock = new MySocket(Server_IP, Server_PORT);



    /**
     * @description java class of writing memo activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_memo);
        Intent intent = getIntent();

        memo_edit_image = (ImageView)findViewById(R.id.memo_image);
        memo_edit_content = (EditText)findViewById(R.id.memo_content);
        memo_edit_submit = (Button)findViewById(R.id.memo_upload);
        spinner = findViewById(R.id.medicine_name);
        medicine_label = findViewById(R.id.medicine_label);

        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "None");

        select();

        try {
            JSONObject memo = new JSONObject(intent.getStringExtra("memo"));
            JSONObject request = new JSONObject();
            Log.d("JSON", memo.toString());
            request.put("Type", "Edit_Memo");
            request.put("Id", memo.getString("user"));
            request.put("Position", memo.getString("id"));
            request.put("Medicine_Name", memo.getString("medicine_name"));

            medicine_label.setVisibility(View.VISIBLE);
            medicine_label.setText(memo.getString("medicine_name"));
            spinner.setVisibility(View.GONE);

            final JSONObject result = new JSONObject(sock.request(request.toString()));
            Thread getBitmap = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://106.10.40.50:5000" + result.get("image") + ".jpeg");
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

            Log.d("IMAGE", "Image");

            getBitmap.start();
            try {
                getBitmap.join();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            memo_edit_image.setImageBitmap(bmp);
            memo_edit_content.setText(result.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /**
         * @description add button event click listener
         */
        memo_edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();  // Create intent
                intent.setType("image/*");  // Set type of URI
                intent.setAction(Intent.ACTION_GET_CONTENT); // Set action of event
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);  // Start activity

            }
        });

        /**
         * @description add button event click listener
         */
        memo_edit_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject request = new JSONObject();  // Create JSON Object to send request
                    MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket for server IP and PORT
                    String memo_text = memo_edit_content.getText().toString();  // Get memo text from edit text widget
                    Bitmap curBmp;
                    if (flag == 1)
                        curBmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimg);  // Get Image from gallery
                    else
                        curBmp = bmp;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    curBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte array [] = baos.toByteArray();
                    request.put("Type", "Edit_Content");  // Add data to create request
                    request.put("Id", user_id);
                    request.put("Position", position);
                    request.put("Text", memo_text);
                    request.put("Image", array);
                    request.put("Medicine_Name", spinner.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), request.toString(), Toast.LENGTH_LONG).show();
                    sock.request(request.toString());  // Send request
                    sock.join();
                    Intent result = new Intent();
                    result.putExtra("position", index);
                    result.putExtra("text", memo_text);
                    result.putExtra("image", array);
                    setResult(231, result);
                    finish();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @description get result when picture is selected from gallery
     * @param requestCode unique ID to identify request
     * @param resultCode unique ID to identify result is ok or not
     * @param data data send by other activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == PICK_IMAGE) {
            flag = 1;
            try {
                selectedimg = data.getData();  // Get image from received intent
                memo_edit_image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));  // Change image view which is selected by gallery
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Log.d("Memo", "Get image error");
            }
        }
    }

    //    Check existing sutdent data in DB and show it in list view
    public void select() {

//        Get readable database
        db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        db.getWritableDatabase();
        String []medicine = null;
        try {
            JSONArray result = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
            medicine = new String[result.length()];
            for (int idx = 0; idx < result.length(); idx++) {
                JSONObject tmp = result.getJSONObject(idx);
                Log.d("DB TEST", "");
                medicine[idx] = tmp.getString("medicine_name");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Create new ArrayAdapter object with changed data and set it to list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicine);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
    }
}
