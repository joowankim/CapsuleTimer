package com.example.knight.a2018_mobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import java.util.ArrayList;

public class memo_list extends AppCompatActivity {

    TextView textView;
    SwipeMenuListView listView;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        textView = (TextView) findViewById(R.id.textView);
        textView.setText("무슨 약 메모");

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(context);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Edit");  //메모 수정 버튼 추후에 없애도 상관 없을듯
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(context);
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
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        final ArrayList<String> list = new ArrayList<>();
        list.add("asvd");
        list.add("aasd");
        list.add("asvd11");
        list.add("aAA11d");
        list.add("as323vd");
        list.add("asvd");
        list.add("aasd");
        list.add("asvd11");
        list.add("aAA11d");
        list.add("as323vd");
        list.add("asvd");
        list.add("aasd");
        list.add("asvd11");
        list.add("aAA11d");
        list.add("as323vd");
        list.add("asvd");
        list.add("aasd");
        list.add("asvd11");
        list.add("aAA11d");
        list.add("as323vd");

        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);
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
        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Log.d("open", position + " item selected ");
                        break;
                    case 1:
                        // delete
                        Toast.makeText(getApplicationContext(), list.get(position) + " 항목이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        listView.clearChoices();
                        adapter.notifyDataSetChanged();
                        Log.d("delete", position + " item deleted ");
                        break;
                }
                // true : close the menu; false : not close the menu
                return true;
            }
        });

        // in Activity Context
        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_memo));

        // set action button
        FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();

        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);

        // menus in action button
        ImageView text = new ImageView(this);
        text.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_text));
        SubActionButton addText = itemBuilder.setContentView(text).build();

        ImageView photo = new ImageView(this);
        photo.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_add_photo));
        SubActionButton addPhoto = itemBuilder.setContentView(photo).build();

        // show action menu
        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(addText)
                .addSubActionView(addPhoto)
                .attachTo(actionButton)
                .build();

        text.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), edit_memo.class);
                startActivity(intent);
            }
        });

        photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), edit_memo.class);
                startActivity(intent);
            }
        });

    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getApplicationContext().getResources().getDisplayMetrics());
    }

}