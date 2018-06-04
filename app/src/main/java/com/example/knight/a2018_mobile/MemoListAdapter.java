package com.example.knight.a2018_mobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by Knight on 2018. 5. 4..
 */

public class MemoListAdapter extends BaseAdapter {
    public JSONObject myMemo;
    public JSONArray memos;
    private String Server_IP="106.10.40.50";
    private int Server_PORT=6000;
    private LayoutInflater inflater;
    int fragment = 0;
    String request;
    MySocket sock;
    Map<String , Bitmap> bmp = new HashMap<>();

    Context myContext;

    public void refresh() {
        try {
            sock = new MySocket(Server_IP, Server_PORT);
            myMemo = new JSONObject(sock.request(request));
            memos = myMemo.getJSONArray("memo");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public MemoListAdapter(Context context, String jsonString) {
        request = jsonString;
        myContext = context;
        inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        refresh();
    }

    public MemoListAdapter(Context context, String jsonString, int fragment) {
        request = jsonString;
        myContext = context;
        this.fragment = fragment;
        inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        refresh();
    }

    public int getCount() {
        return memos.length();
    }

    public Object getItem(int position) {
        try {
            return memos.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        try{
            JSONObject memo = memos.getJSONObject(i);
            return memo.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getWriter(int i) {
        try{
            JSONObject memo = memos.getJSONObject(i);
            return memo.get("user").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void delete(int position) {
        Object trash = memos.remove(position);
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        Log.d("LEN", String.valueOf(bmp.size()));

        try {
            final JSONObject memo = memos.getJSONObject(position);

            if (bmp.containsKey(memo.get("image")) == false) {
                Thread getBitmap = new Thread() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL("http://106.10.40.50:5000" + memo.get("image") + ".jpeg");
                            HttpURLConnection image_http = (HttpURLConnection) url.openConnection();
                            image_http.setDoInput(true);
                            image_http.connect();

                            InputStream is = image_http.getInputStream();
                            bmp.put(memo.get("image").toString(), BitmapFactory.decodeStream(is));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        super.run();
                    }
                };

                Log.d("IMAGE", "Image");

                getBitmap.start();
                try {
                    getBitmap.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.memo_item, parent, false);
                viewHolder.text = convertView.findViewById(R.id.memo_text);
                viewHolder.date = convertView.findViewById(R.id.memo_time);
                viewHolder.img = convertView.findViewById(R.id.memo_image);
                viewHolder.close = convertView.findViewById(R.id.close);
                viewHolder.edit = convertView.findViewById(R.id.edit);


                viewHolder.img.setImageBitmap(bmp.get(memo.get("image").toString()));
                viewHolder.text.setText(memo.getString("text"));
                viewHolder.date.setText(memo.getString("time"));

                viewHolder.close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 0);
                    }
                });

                viewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ListView) parent).performItemClick(v, position, 1);
                    }
                });

                convertView.setId(memo.getInt("id"));
                convertView.setTag(viewHolder);
            }

            else {
                viewHolder = (ViewHolder) convertView.getTag();
                viewHolder.img.setImageBitmap(bmp.get(memo.get("image").toString()));
                viewHolder.text.setText(memo.getString("text"));
                viewHolder.date.setText(memo.getString("time"));
                convertView.setId(memo.getInt("id"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView text;
        public TextView date;
        public ImageView img;
        public ImageButton close;
        public ImageButton edit;
    };

}
