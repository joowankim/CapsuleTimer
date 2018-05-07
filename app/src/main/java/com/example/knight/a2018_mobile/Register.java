package com.example.knight.a2018_mobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {

    EditText id;
    EditText pw;
    EditText re_pw;
    Button register;
    String Server_IP="106.10.40.50";
    private int Server_PORT=6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = (EditText) findViewById(R.id.register_id);
        pw = (EditText) findViewById(R.id.register_pw);
        re_pw = (EditText) findViewById(R.id.register_re_pw);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySocket sock = new MySocket(Server_IP, Server_PORT);
                JSONObject result;
                try{
                    JSONObject request = new JSONObject();
                    request.put("Type", "Validation");
                    request.put("Id", id.getText().toString());
                    result = new JSONObject(sock.request(request.toString()));
                    if (result.get("result").toString().compareTo("No") == 0) {
                        Toast.makeText(getApplicationContext(), "ID is duplicated", Toast.LENGTH_LONG).show();
                    } else if (pw.getText().toString().length() < 4) {
                        Toast.makeText(getApplicationContext(), "Password should me longer", Toast.LENGTH_LONG).show();
                    } else if (pw.getText().toString().compareTo(re_pw.getText().toString()) != 0) {
                        Toast.makeText(getApplicationContext(), "Two password is different", Toast.LENGTH_LONG).show();
                    } else {
                        request = new JSONObject();
                        request.put("Type", "Register");
                        request.put("Id", id.getText().toString());
                        request.put("Password", pw.getText().toString());
                        result = new JSONObject(sock.request(request.toString()));
                        if (result.get("result").toString().compareTo("No") == 0) {
                            Toast.makeText(getApplicationContext(), "Register failed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Register successed", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
