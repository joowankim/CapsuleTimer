package com.example.knight.a2018_mobile;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.knight.a2018_mobile.data.AlarmReminderContract;


public class AlarmCursorAdapter extends CursorAdapter {

    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage , mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    // constructor
    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    // 화면에 뷰를 출력해줌  -> alarm_items.xml과 연결
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_items, parent, false);
    }

    // bindView --> 출력할 뷰와 content provider의 column을 연결하는 역할
    // 인자로 전달받은 cursor로 DB를 참조해서 해당하는 row의 data를 가져와 string에 저장하고
    // 저장된 value를 각 set 메소드 호출을 통해 view를 초기화 시킴
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDateAndTimeText = (TextView) view.findViewById(R.id.recycle_date_time);
        mRepeatInfoText = (TextView) view.findViewById(R.id.recycle_repeat_info);
        mActiveImage = (ImageView) view.findViewById(R.id.active_image);
        mThumbnailImage = (ImageView) view.findViewById(R.id.thumbnail_image);

        int titleColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_DATE);
        int timeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TIME);
        int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT);
        int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_NO);
        int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_REPEAT_TYPE);
        int activeColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_ACTIVE);
        int mondayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_MONDAY);
        int tuesdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_TUESDAY);
        int wednesdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_WEDNESDAY);
        int thursdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_THURSDAY);
        int fridayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_FRIDAY);
        int saturdayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_SATURDAY);
        int sundayColumnIndex = cursor.getColumnIndex(AlarmReminderContract.AlarmReminderEntry.KEY_SUNDAY);


        String title = cursor.getString(titleColumnIndex);
        String date = cursor.getString(dateColumnIndex);
        String time = cursor.getString(timeColumnIndex);
        String repeat = cursor.getString(repeatColumnIndex);
        String repeatNo = cursor.getString(repeatNoColumnIndex);
        String repeatType = cursor.getString(repeatTypeColumnIndex);
        String active = cursor.getString(activeColumnIndex);

        String dateTime = date + " " + time;


        setReminderTitle(title);
        setReminderDateTime(dateTime);
        setReminderRepeatInfo(repeat, repeatNo, repeatType);
        setActiveImage(active);
    }

    // Set reminder title view
    public void setReminderTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }

    // Set date and time views
    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    // Set repeat views
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            mRepeatInfoText.setText("Every " + repeatNo + " " + repeatType + "(s)");
        }else if (repeat.equals("false")) {
            mRepeatInfoText.setText("Repeat Off");
        }
    }

    // Set active image as on or off
    public void setActiveImage(String active){
        if(active.equals("true")){
            mActiveImage.setImageResource(R.drawable.ic_notifications_black_24dp);
        }else if (active.equals("false")) {
            mActiveImage.setImageResource(R.drawable.ic_notifications_off_black);
        }
    }
}
