package com.example.knight.a2018_mobile;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kim on 2018-05-06.
 */

public class FragmentMemoList extends Fragment {

    TextView textView;
    SwipeMenuListView listView;
    MemoListAdapter memoListAdapter;
    SharedPreferences sharedPreferences;
    String id;
    JSONObject request;

    public FragmentMemoList () { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_memo_list, container, false);

        textView = (TextView) view.findViewById(R.id.textView);
        textView.setVisibility(View.INVISIBLE);
        textView.setHeight(0);
        sharedPreferences = getContext().getSharedPreferences("Login_Session", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("Id", "None");
        request = new JSONObject();

        try {
            request.put("Type", "Search_Memo");
            request.put("User", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");      // 메모 수정버튼
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        // 리스트 생성
        listView = (SwipeMenuListView) view.findViewById(R.id.listView);
//        final ArrayList<String> list = new ArrayList<>();
//        list.add("asvd");
//        list.add("aasd");
//        list.add("asvd11");
//        list.add("aAA11d");
//        list.add("as323vd");
//        list.add("asvd");
//        list.add("aasd");
//        list.add("asvd11");
//        list.add("aAA11d");
//        list.add("as323vd");
//        list.add("asvd");
//        list.add("aasd");
//        list.add("asvd11");
//        list.add("aAA11d");
//        list.add("as323vd");
//        list.add("asvd");
//        list.add("aasd");
//        list.add("asvd11");
//        list.add("aAA11d");
//        list.add("as323vd");

//        final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
        memoListAdapter = new MemoListAdapter(getContext(), request.toString());

        listView.setAdapter(memoListAdapter);
        // set creator
        listView.setMenuCreator(creator);

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                listView.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                listView.smoothOpenMenu(position);
            }
        });

        // 서버랑도 연동해서 바꿔야할 부분
        // 메모 편집, 삭제
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
//                        // open
//                        Log.d("open", position + " item selected ");
//                        break;
//                    case 1:
//                        // delete
//                        Toast.makeText(getActivity(), list.get(position) + " 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
//                        list.remove(position);
//                        listView.clearChoices();
//                        adapter.notifyDataSetChanged();
//                        Log.d("delete", position + " item deleted ");
//                        break;
//                }
//                // true : close the menu; false : not close the menu
//                return true;
//            }
//        });


        return view;
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getActivity().getResources().getDisplayMetrics());
    }
}
