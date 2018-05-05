package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Show_medicine_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_list);

        Intent data = getIntent();
        ListView listView = (ListView) findViewById(R.id.medicine_list);
        MedicineListAdapter adapter = new MedicineListAdapter(this, data.getStringExtra("json"));
        listView.setAdapter(adapter);
    }
}
