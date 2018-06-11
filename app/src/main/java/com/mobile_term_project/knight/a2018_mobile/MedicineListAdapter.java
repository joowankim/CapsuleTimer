package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @brief
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class MedicineListAdapter extends BaseAdapter {
    public JSONObject myMedicine;
    public JSONArray medicines;
    public LayoutInflater inflater;

    Context myContext;

    public MedicineListAdapter(Context context, String jsonString) {
        try {
            Log.d("TEST", jsonString);
            myMedicine = new JSONObject(jsonString);
            medicines = myMedicine.getJSONArray("medicine");
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public View getView(final int position, View convertView, final ViewGroup parent) {
        LinearLayout view = null;
        ViewHolder viewHolder = new ViewHolder();
        try {
            final JSONObject medicine = medicines.getJSONObject(position);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.medicine_item, parent, false);
                convertView.setClickable(true);
                viewHolder.name = convertView.findViewById(R.id.medicine_name);
                viewHolder.company = convertView.findViewById(R.id.company);
                viewHolder.ingredient = convertView.findViewById(R.id.ingredient);

                viewHolder.name.setText(medicine.getString("name"));
                viewHolder.company.setText(medicine.getString("company"));
                viewHolder.ingredient.setText(medicine.getString("ingredient"));

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 0);
                    }
                });

                convertView.setTag(viewHolder);
            }

            else {
                viewHolder = (MedicineListAdapter.ViewHolder) convertView.getTag();
                viewHolder.name.setText(medicine.getString("name"));
                viewHolder.company.setText(medicine.getString("company"));
                viewHolder.ingredient.setText(medicine.getString("ingredient"));
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 0);
                    }
                });
                convertView.setTag(viewHolder);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView name;
        public TextView company;
        public TextView ingredient;
    }
}
