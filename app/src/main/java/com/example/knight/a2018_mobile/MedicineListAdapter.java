package com.example.knight.a2018_mobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Knight on 2018. 5. 4..
 */

public class MedicineListAdapter extends BaseAdapter {
    public JSONObject myMedicine;
    public JSONArray medicines;

    Context myContext;

    public MedicineListAdapter(Context context, String jsonString) {
        try {
            Log.d("TEST", jsonString);
            myMedicine = new JSONObject(jsonString);
            medicines = myMedicine.getJSONArray("medicine");
//            for (int i = 0; i < medicines.length(); i++) {
//
//                JSONObject medicine = medicines.getJSONObject(i);
//
//                Log.i("Seq", "" + medicine.getString("Sequence"));
//                Log.i("name","" + medicine.getString("name"));
//                Log.i("link", "" + medicine.getString("link"));
//                Log.i("ingredient", "" + medicine.getString("ingredient"));
//                Log.i("ningredient","" + medicine.getString("ningredient"));
//                Log.i("company", "" + medicine.getString("company"));
//                Log.i("type", "" + medicine.getString("type"));
//
//
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myContext = context;
    }

    public int getCount() {
        return medicines.length();
    }

    public Object getItem(int position) {
        try {
            return medicines.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout view = null;
        try {
            final JSONObject medicine = medicines.getJSONObject(position);
            view = new LinearLayout(myContext);
            TextView name = new TextView(myContext);
            TextView company = new TextView(myContext);
            TextView ingredient = new TextView(myContext);
            TextView type = new TextView(myContext);
//            ImageView img = new ImageView(myContext);

            name.setText(medicine.getString("name"));
            company.setText(medicine.getString("company"));
            ingredient.setText(medicine.getString("ingredient"));
            type.setText(medicine.getString("type"));

            view.setOrientation(LinearLayout.VERTICAL);
            view.addView(name);
            view.addView(company);
            view.addView(ingredient);
            view.addView(type);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(myContext, Show_medicine_info.class);
                        intent.putExtra("link", medicine.getString("link"));
                        myContext.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
//            img.setimage

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
