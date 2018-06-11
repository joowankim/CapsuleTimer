package com.mobile_term_project.knight.a2018_mobile;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.design.widget.FloatingActionButton;
import android.widget.Toast;

/**
 * @brief get the memo_list layout and inflate that
 * @author Joo wan Kim
 * @date 2018.05.20
 * @version 1.0.0.1
 */

public class FragmentMemoList extends Fragment {

    TextView textView;
    ListView listView;
    MemoListAdapter memoListAdapter;
    SharedPreferences sharedPreferences;
    FloatingActionButton floatingActionButton;
    String id;
    JSONObject request;

    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;

    public FragmentMemoList () { }

    /**
     * @brief inflate memo list
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return memo list view group
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_memo_list, container, false);

        // get memo list from web server
        final String medicine_name = ((showGraph)getActivity()).medicine_name;
        sharedPreferences = getContext().getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("Id", "None");
        request = new JSONObject();

        try {
            request.put("Type", "Search_Memo");
            request.put("User", id);
            Log.d("FRAG", medicine_name);
            request.put("Medicine_Name", medicine_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // generate list
        listView = view.findViewById(R.id.listView);
        memoListAdapter = new MemoListAdapter(getContext(), request.toString(), 1);

        floatingActionButton = view.findViewById(R.id.add);

        /**
         * @brief when button is clicked, send intent edit memo activity
         */
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), writing_memo.class);
                intent.putExtra("Medicine_Name", medicine_name);
                startActivity(intent);
            }
        });

        listView.setAdapter(memoListAdapter);

        /**
         * @brief when item is clicked, it would act what it commanded
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch ((int)id) {
                    case 1:
                        // open item in the list
                        Log.d("open", String.valueOf(memoListAdapter.getItemId(position)) + " " + String.valueOf(position) + " item selecteds ");
                        JSONObject tmp = (JSONObject)memoListAdapter.getItem(position);
                        Intent tmp_intent = new Intent(getActivity(), edit_memo.class);
                        tmp_intent.putExtra("memo", tmp.toString());
                        startActivityForResult(tmp_intent, 666);
                        break;
                    case 0:
                        // delete item in web server
                        MySocket sock = new MySocket(Server_IP, Server_PORT);
                        try {
                            JSONObject memo = (JSONObject)memoListAdapter.getItem(position);
                            JSONObject delete_request = new JSONObject();
                            delete_request.put("Type", "Delete_Memo");
                            delete_request.put("Position", memo.getInt("id"));
                            delete_request.put("Id", memo.getString("user"));
                            JSONObject result = new JSONObject(sock.request(delete_request.toString()));
                            if (result.get("result").toString().compareTo("No") == 0) {
                                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                                break;
                            } else
                                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_LONG).show();
                            listView.setAdapter(new MemoListAdapter(getContext(), request.toString(), 1));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("delete", position + " item deleted ");
                        break;
                }
            }
        });

        return view;
    }

    /**
     * @brief convert dp size to pixel size
     * @param dp
     * @return
     */
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getActivity().getResources().getDisplayMetrics());
    }

    /**
     * @brief when this fragment is resumed, this would generate memo list
     */
    @Override
    public void onResume() {
        memoListAdapter = new MemoListAdapter(getContext(), request.toString(), 1);
        listView.setAdapter(memoListAdapter);
        super.onResume();
    }
}
