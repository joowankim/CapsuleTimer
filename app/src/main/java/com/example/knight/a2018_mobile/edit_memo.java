package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class edit_memo extends AppCompatActivity {

    ImageView memo_edit_image;
    EditText memo_edit_content;
    Button memo_edit_submit;
    int position;
    String writer;
    String user_id;
    SharedPreferences sharedPreferences;
    public static final int PICK_IMAGE = 1;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private Uri selectedimg;
    private int index;
    Bitmap bmp;
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
        position = intent.getIntExtra("position", 0);
        writer = intent.getStringExtra("writer");
        index = intent.getIntExtra("index", 0);
        sharedPreferences = getSharedPreferences("Login_Session", MODE_PRIVATE);
        user_id = sharedPreferences.getString("Id", "None");

        try {
            JSONObject request = new JSONObject();
            request.put("Type", "Edit_Memo");
            request.put("Id", writer);
            request.put("Position", position);
            final JSONObject result = new JSONObject(sock.request(request.toString()));
            Thread getBitmap = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://106.10.40.50:5000/" + result.get("image") + ".jpeg");
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
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedimg);  // Get Image from gallery

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte array [] = baos.toByteArray();
                    request.put("Type", "Edit_Content");  // Add data to create request
                    request.put("Id", user_id);
                    request.put("Position", position);
                    request.put("Text", memo_text);
                    request.put("Image", array);
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
                    Log.d("Memo", e.toString());
                    Log.d("Memo", "Send to server error");
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
            try {
                selectedimg = data.getData();  // Get image from received intent
                memo_edit_image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedimg));  // Change image view which is selected by gallery
            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                Log.d("Memo", "Get image error");
            }
        }
    }
}
