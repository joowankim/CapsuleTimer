package com.example.knight.a2018_mobile;


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
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.design.widget.FloatingActionButton;

/**
 * Created by Kim on 2018-05-06.
 */

public class FragmentMemoList extends Fragment {

    TextView textView;
    ListView listView;
    MemoListAdapter memoListAdapter;
    SharedPreferences sharedPreferences;
    FloatingActionButton floatingActionButton;
    String id;
    JSONObject request;

    public FragmentMemoList () { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_memo_list, container, false);

//        textView = (TextView) view.findViewById(R.id.textView);
//        textView.setVisibility(View.INVISIBLE);
//        textView.setHeight(0);
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

        // 리스트 생성
        listView = view.findViewById(R.id.listView);
        memoListAdapter = new MemoListAdapter(getContext(), request.toString(), 1);
        floatingActionButton = view.findViewById(R.id.add);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), writing_memo.class);
                intent.putExtra("Medicine_Name", medicine_name);
                startActivity(intent);
            }
        });

        listView.setAdapter(memoListAdapter);
        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getActivity().getResources().getDisplayMetrics());
    }

    @Override
    public void onResume() {
        memoListAdapter = new MemoListAdapter(getContext(), request.toString(), 1);
        listView.setAdapter(memoListAdapter);
        super.onResume();
    }
}
