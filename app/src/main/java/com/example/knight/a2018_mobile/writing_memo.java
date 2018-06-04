package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by Knight on 2018. 5. 4..
 *
 * Writing memo activity
 *
 */


public class writing_memo extends AppCompatActivity {

    ImageView memo_edit_image;
    EditText memo_edit_content;
    Button memo_edit_submit;
    Spinner spinner;

    int position;
    String user_id;
    SharedPreferences sharedPreferences;
    public static final int PICK_IMAGE = 1;
    private String Server_IP = "106.10.40.50";
    private int Server_PORT = 6000;
    private Uri selectedimg;
    DB db;
    Intent getIntents;

    /**
     * @param savedInstanceState
     * @description java class of writing memo activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_memo);

        memo_edit_image = (ImageView) findViewById(R.id.memo_image);
        memo_edit_content = (EditText) findViewById(R.id.memo_content);
        memo_edit_submit = (Button) findViewById(R.id.memo_upload);
        spinner = findViewById(R.id.medicine_name);

        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "None");

        getIntents = getIntent();
        select(0);

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
                finish();
                try {
                    JSONObject request = new JSONObject();  // Create JSON Object to send request
                    MySocket sock = new MySocket(Server_IP, Server_PORT);  // Create socket for server IP and PORT
                    String memo_text = memo_edit_content.getText().toString();  // Get memo text from edit text widget
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimg);  // Get Image from gallery
                    String jpeg = "";
                    int idx = 0;

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte array[] = baos.toByteArray();
                    request.put("Type", "Write_Memo");  // Add data to create request
                    request.put("Id", user_id);
                    request.put("Text", memo_text);
                    request.put("Medicine_Name", spinner.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), request.toString(), Toast.LENGTH_LONG).show();
                    jpeg = Base64.encodeToString(array, Base64.DEFAULT);
                    Log.d("LENGTH", String.valueOf(jpeg.length()));
                    sock.request(request.toString(), jpeg, 1);  // Send request
                    finish();


                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    Log.d("Memo", e.toString());
                    Log.d("Memo", "Send to server error");
                }
            }
        });
    }

    /**
     * @param requestCode unique ID to identify request
     * @param resultCode  unique ID to identify result is ok or not
     * @param data        data send by other activity
     * @description get result when picture is selected from gallery
     */
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    @SuppressLint("NewApi")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            try {
                selectedimg = data.getData();  // Get image from received intent
                memo_edit_image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));  // Change image view which is selected by gallery
                Log.d("IMAGE", selectedimg.toString());
//                Log.d("Base64", Base64.encodeToString("Hello".getBytes(), Base64.DEFAULT));

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                e.printStackTrace();
                Log.d("Memo", "Get image error");
            }
        }
    }

    //    Check existing sutdent data in DB and show it in list view
    public void select(int flag) {

//        Get readable database
        db = new DB(getApplicationContext(), "Alarm.db", null, 1);
        db.getWritableDatabase();
        String[] medicine = null;
        int init = 0;
        try {
            JSONArray result = new JSONArray(db.mySelect("medicine_alarm", "*", "1 = 1"));
            medicine = new String[result.length()];
            for (int idx = 0; idx < result.length(); idx++) {
                JSONObject tmp = result.getJSONObject(idx);
                Log.d("DB TEST", "");
                medicine[idx] = tmp.getString("medicine_name");
                if (medicine[idx].compareTo(getIntents.getStringExtra("Medicine_Name")) == 0)
                    init = idx;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Create new ArrayAdapter object with changed data and set it to list view
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicine);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        if (flag == 0) {
            flag = 1;
            Log.d("TEST", getIntents.getStringExtra("Medicine_Name"));
            spinner.setSelection(adapter.getPosition(getIntents.getStringExtra("Medicine_Name")));
        }
    }
}