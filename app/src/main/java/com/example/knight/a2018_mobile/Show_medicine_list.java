package com.example.knight.a2018_mobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class Show_medicine_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_medicine_list);

        Intent data = getIntent();
        ListView listView = (ListView) findViewById(R.id.medicine_list);
        final MedicineListAdapter adapter = new MedicineListAdapter(this, data.getStringExtra("json"));
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject medicine = (JSONObject)adapter.getItem(position);
                try {
                    Intent intent = new Intent(getApplicationContext(), Show_medicine_info.class);
                    intent.putExtra("link", medicine.getString("link"));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
