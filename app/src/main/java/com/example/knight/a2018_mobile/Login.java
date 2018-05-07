package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText id;
    EditText pw;
    Button login;
    Button register;
    String Server_IP="106.10.40.50";
    private int Server_PORT=6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = (EditText) findViewById(R.id.login_id);
        pw = (EditText) findViewById(R.id.login_pw);
        login = (Button) findViewById(R.id.login_submit);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySocket sock = new MySocket(Server_IP, Server_PORT);
                JSONObject request = new JSONObject();
                JSONObject result = new JSONObject();
                try {
                    request.put("Type", "Login");
                    request.put("Id", id.getText().toString());
                    request.put("Password", pw.getText().toString());
                    result = new JSONObject(sock.request(request.toString()));
                    if (result.get("result").toString().compareTo("No") == 0) {
                        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Login successed", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
