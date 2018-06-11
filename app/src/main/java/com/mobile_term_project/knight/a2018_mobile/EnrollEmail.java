package com.mobile_term_project.knight.a2018_mobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @brief take email information from enroll email activity
 * @author Knight
 * @date 2018.06.03
 * @version 1.0.0.1
 */

public class EnrollEmail extends AppCompatActivity{

    ListView listView;
    EditText email;
    EditText name_doctor;
    EditText name_hospital;
    ImageButton updateBtn;
    ImageButton deleteBtn;
    String nameOfHospital, nameOfDoctor, email_doctor;
    String[] studentInfo;
    TextView textView;

    SQLiteDatabase db;
    DBforEmail helper;

    /**
     * @param savedInstanceState
     * @brief make database to store email and process update and delete event
     * @return N/A
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        name_hospital = findViewById(R.id.name_hospital);
        name_doctor = findViewById(R.id.name_doctor);
        email = findViewById(R.id.email);

        updateBtn = findViewById(R.id.update);
        deleteBtn = findViewById(R.id.delete);
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.textView2);

        helper = new DBforEmail(this, "Email.db", null, 2);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_hospital.getText().toString().length() == 0 ||
                        name_doctor.getText().toString().length() == 0 || email.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    nameOfHospital = name_hospital.getText().toString();
                    nameOfDoctor = name_doctor.getText().toString();
                    email_doctor = email.getText().toString();
                    insert(nameOfHospital, nameOfDoctor, email_doctor);
                    invalidate();

                    name_hospital.setText(null);
                    name_doctor.setText(null);
                    email.setText(null);
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name_doctor.getText().toString().length() == 0) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    nameOfDoctor= name_doctor.getText().toString();
                    delete(nameOfDoctor);
                    invalidate();

                    name_doctor.setText(null);
                }


            }
        });
        invalidate();
    }

    /**
     * @param nameOfHospital
     * @param nameOfDoctor
     * @param email_doctor
     * @brief get email user's information and insert into the database
     * @return N/A
     */
    public void insert(String nameOfHospital, String nameOfDoctor, String email_doctor) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nameOfHospital", nameOfHospital);
        values.put("nameOfDoctor", nameOfDoctor);
        values.put("email_doctor", email_doctor);
        db.insert("Email", null, values);
        Log.i("insert", "성공");
    }

    /**
     * @param nameOfDoctor
     * @brief get name of doctor and search information which have name and delete it from database
     * @return N/A
     */
    public void delete(String nameOfDoctor) {
        db = helper.getWritableDatabase();
        db.delete("Email", "nameOfDoctor=?", new String[]{nameOfDoctor});
        Log.i("delete", "성공");
    }

    /**
     * @brief doing linear search in database and each of data store to the string array
     * @return N/A
     */
    public void select(){
        db = helper.getReadableDatabase();
        Cursor c = db.query("Email", null, null,null,null,null,null);

        if(c == null) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.INVISIBLE);

        studentInfo = new String[c.getCount()];
        int count = 0;
        while(c.moveToNext()){
            studentInfo[count] = c.getString(c.getColumnIndex("nameOfHospital")) + " / " + c.getString(c.getColumnIndex("nameOfDoctor")) + " / " +
                    c.getString(c.getColumnIndex("email_doctor"));
            count++;
        }

        if(count == 0) textView.setVisibility(View.VISIBLE);
        else textView.setVisibility(View.INVISIBLE);

        c.close();

    }


    /**
     * @brief update list view with email that user input
     * @return N/A
     */
    public void invalidate(){
        select();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentInfo);
        listView.setAdapter(adapter);

    }
}