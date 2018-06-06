package com.example.knight.a2018_mobile;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @brief Implementation of App Widget functionality.
 * @author Knight
 * @date 2018.05.04
 * @version 1.0.0.1
 */

public class MonthCalendarWidget extends AppWidgetProvider {
    private static final String ACTION_PREVIOUS_MONTH = "com.example.android.monthcalendarwidget.action.PREVIOUS_MONTH";
    private static final String ACTION_NEXT_MONTH = "com.example.android.monthcalendarwidget.action.NEXT_MONTH";
    private static final String ACTION_RESET_MONTH = "com.example.android.monthcalendarwidget.action.RESET_MONTH";

    private static final String PREF_MONTH = "month";
    private static final String PREF_YEAR = "year";

    private String []records;
    private String medicine_name = "";
    DB db1, db2;
    ArrayList<Integer[]> Day  = new ArrayList<Integer[]>();

    /**
     * app widget의 속성(meta data)에서 지정해준 updatePeriodMillis 값에 따라 주기적으로 호출된다 (month_calendar_widget_info.xml에 있다)
     * 처음 widget이 화면에 붙을 때, init() 작업을 해주기 위해서도 call 된다
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds 업데이트할 widget의 ID들
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        db1 = new DB(context, "Alarm.db", null, 1);
        db2 = new DB(context, "Taken.db", null, 1);
        db1.getWritableDatabase();
        db2.getWritableDatabase();

        try {
            JSONArray result = new JSONArray(db1.mySelect("medicine_alarm", "*", "1 = 1"));
            JSONObject tmp = result.getJSONObject(0);
            medicine_name = tmp.getString("medicine_name");

            JSONArray taken = new JSONArray(db2.mySelect("medicine_taken", "*", "medicine_name = \"" + medicine_name + "\""));
            records = new String[taken.length()];
            for (int idx = 0; idx < taken.length(); idx++) {
                JSONObject tmpTaken = result.getJSONObject(idx);
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(Long.parseLong(tmpTaken.getString("time")));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                records[idx] = simpleDateFormat.format(c.getTime());

                for (Object object : records) {
                    String element = (String) object;
                    String[] t = element.split(" ");

                    String[] temp_day = t[0].split("-");

                    Day.add(new Integer[]{Integer.parseInt(temp_day[0]),Integer.parseInt(temp_day[1]),Integer.parseInt(temp_day[2])});
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Day.add(new Integer[]{0, 0, 0});
        }

        //모든 widget을 업데이트 하기에 모든 widget ID를 호출하여 업데이트
        for (int appWidgetId : appWidgetIds) {
            drawWidget(context, appWidgetId);
        }
    }

    private void redrawWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, MonthCalendarWidget.class));
        for (int appWidgetId : appWidgetIds) { drawWidget(context, appWidgetId); }
    }

    /**
     * 일반적인 broadcast receiver
     * onUpdate()와 같은 callback method들보다 먼저 불리게 된다
     * 이 method는 implement할 필요가 없다. 이미 구현되어 있다.
     * 원래 각 callback 함수들을 부르는 기능이 구현되어 있지만 추가적인 기능이 필요할 때 더 구현한다
     * @param context
     * @param intent 클릭과 같은 action들
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // broadcast된 action을 받음
        String action = intent.getAction();

        if (ACTION_PREVIOUS_MONTH.equals(action)) { // 저번달로 가는 화살표를 클릭했을 때
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, -1);
            sp.edit().putInt(PREF_MONTH, cal.get(Calendar.MONTH)).putInt(PREF_YEAR, cal.get(Calendar.YEAR)).apply();
            redrawWidgets(context);

        } else if (ACTION_NEXT_MONTH.equals(action)) {  // 다음달로 가는 화살표를 클릭했을 때
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            Calendar cal = Calendar.getInstance();
            int thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
            cal.add(Calendar.MONTH, 1);
            sp.edit().putInt(PREF_MONTH, cal.get(Calendar.MONTH)).putInt(PREF_YEAR, cal.get(Calendar.YEAR)).apply();
            redrawWidgets(context);

        } else if (ACTION_RESET_MONTH.equals(action)) { // 달이 써있는 부분을 클릭했을 때
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().remove(PREF_MONTH).remove(PREF_YEAR).apply();
            redrawWidgets(context);
        }
    }

    // 내가 지정한 API레벨보다 높은 메소드를 호출하는 부분이 있을 때 @TargetApi(API레벨)을 붙여 사용하면 된다
    /**
     * 지정한 위젯의 크기를 바꿀 때 사용된다
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param newOptions
     */
    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        // 바뀐대로 그린다
        drawWidget(context, appWidgetId);
    }

    /**
     * app widget을 그린다
     * @param context
     * @param appWidgetId
     */
    private void drawWidget(Context context, int appWidgetId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);  // 액티비티에서 설정한  여러 앱위젯의 상태를 appWidgetManager로 넘겨준다
        Resources res = context.getResources();                                     // 현재 어플리케이션 패키지에 리소스 객체를 반환한다
        // OPTION_APPWIDGET_MIN_WIDTH  : 위젯의 현재 너비에 대한 작은 쪽 경계의 크기
        // OPTION_APPWIDGET_MIN_HEIGHT : 위젯의 현재 높이에 대한 작은 쪽 경계의 크기
        // OPTION_APPWIDGET_MAX_WIDTH  : 위젯의 현재 너비에 대한 큰 쪽 경계의 크기
        // OPTION_APPWIDGET_MAX_HEIGHT : 위젯의 현재 높이에 대한 큰 쪽 경계의 크기
        Bundle widgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);   // 위의 값들을 얻을 수 있다

        boolean shortMonthName = false;
        boolean mini = false;
        int numWeeks = 6;

        int[] count = { 0, };
        int dateIdx = 0;
        for(int i=0; i<Day.size() && Day.size()>1; i++) {
            count[dateIdx] = 0;
            for(int j=i; j < Day.size() && Day.get(i)[2] == Day.get(j)[2]; j++) count[dateIdx]++;
            i += (count[dateIdx] - 1);
            dateIdx++;
        }

        if (widgetOptions != null) {    // app widget option이 없을 때
            int minWidthDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
            int minHeightDp = widgetOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT);

            shortMonthName = minWidthDp <= res.getInteger(R.integer.max_width_short_month_label_dp);    // boolean 값 : 240보다 작거나 같을 때
            mini = minHeightDp <= res.getInteger(R.integer.max_height_mini_view_dp);    // dimens에 있음

            if (mini) { numWeeks = minHeightDp <= res.getInteger(R.integer.max_height_mini_view_1_row_dp) ? 1 : 2; }    // true -> mini = 1 , false -> mini = 2
        }

        // 띄울 위젯 xml 선택
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);

        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_YEAR);  // 오늘 날짜
        int todayYear = cal.get(Calendar.YEAR);      // 금년
        int thisMonth;                                // 금월

        if (!mini) {    // (mini = false) == (widgetOptions != null)
            thisMonth = sp.getInt(PREF_MONTH, cal.get(Calendar.MONTH));
            int thisYear = sp.getInt(PREF_YEAR, cal.get(Calendar.YEAR));
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.MONTH, thisMonth);
            cal.set(Calendar.YEAR, thisYear);
        } else {
            thisMonth = cal.get(Calendar.MONTH);
        }

        // RemoteView 위의 TextView 객체의 text를 수정하는 데 사용한다 (달 이름 결정)
        rv.setTextViewText(R.id.month_label, DateFormat.format(shortMonthName ? "MMM yy" : "MMMM yyyy", cal));

        if (!mini) {
            cal.set(Calendar.DAY_OF_MONTH, 1);
            int monthStartDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, 1 - monthStartDayOfWeek);
        } else {
            int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
            cal.add(Calendar.DAY_OF_MONTH, 1 - todayDayOfWeek);
        }

        rv.removeAllViews(R.id.calendar);

        // set view objects of a calendar
        RemoteViews headerRowRv = new RemoteViews(context.getPackageName(), R.layout.row_header);
        DateFormatSymbols dfs = DateFormatSymbols.getInstance();
        String[] weekdays = dfs.getShortWeekdays();

        for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
            RemoteViews dayRv = new RemoteViews(context.getPackageName(), R.layout.cell_header);
            dayRv.setTextViewText(android.R.id.text1, weekdays[day]);
            headerRowRv.addView(R.id.row_container, dayRv);
        }
        rv.addView(R.id.calendar, headerRowRv);

        dateIdx = 0;
        // 각 주에 대한 view를 생성하는 루프
        for (int week = 0; week < numWeeks; week++) {
            RemoteViews rowRv = new RemoteViews(context.getPackageName(), R.layout.row_week);

            //각 날짜에 대한 view를 생성하는 루프
            for (int day = 0; day < 7; day++) {
                boolean inMonth = cal.get(Calendar.MONTH) == thisMonth;
                boolean inYear  = cal.get(Calendar.YEAR) == todayYear;
                boolean isToday = inYear && inMonth && (cal.get(Calendar.DAY_OF_YEAR) == today);
                boolean isFirstOfMonth = cal.get(Calendar.DAY_OF_MONTH) == 1;

                int cellLayoutResId = R.layout.cell_day;

                // 여기서 날짜마다의 색 조정 가능
                if (isToday) {  // 해당 날짜가 오늘이라면 cell_today.xml을 view로 지정 (배경 하얀색)
                    cellLayoutResId = R.layout.cell_today;
                } else if (inMonth) {
                    cellLayoutResId = R.layout.cell_day_this_month;
                }

                if (Day.size() > 1) {
                    Boolean isDate = (cal.get(Calendar.DATE) == Day.get(dateIdx)[2]) && (cal.get(Calendar.MONTH) == Day.get(dateIdx)[1]) && (cal.get(Calendar.YEAR) == Day.get(dateIdx)[0]);
                    if (isDate && count[dateIdx] == 1) {
                        cellLayoutResId = R.layout.cell_take_once;
                    } else if (isDate && count[dateIdx] == 2) {
                        cellLayoutResId = R.layout.cell_take_twice;
                    } else if (isDate && count[dateIdx] == 3) {
                        cellLayoutResId = R.layout.cell_take_three_times;
                    } else if (isDate && count[dateIdx] == 4) {
                        cellLayoutResId = R.layout.cell_take_four_times;
                    }
                }

                RemoteViews cellRv = new RemoteViews(context.getPackageName(), cellLayoutResId);
                cellRv.setTextViewText(android.R.id.text1, Integer.toString(cal.get(Calendar.DAY_OF_MONTH)));

                if (isFirstOfMonth) {
                    cellRv.setTextViewText(R.id.month_label, DateFormat.format("MMM", cal));
                }

                rowRv.addView(R.id.row_container, cellRv);
                cal.add(Calendar.DAY_OF_MONTH, 1);
            }
            rv.addView(R.id.calendar, rowRv);
        }

        // 각 버튼들에 대한 action을 intent에 담아 onReceive() method 호출
        rv.setViewVisibility(R.id.prev_month_button, mini ? View.GONE : View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.prev_month_button, PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_PREVIOUS_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setViewVisibility(R.id.next_month_button, mini ? View.GONE : View.VISIBLE);
        rv.setOnClickPendingIntent(R.id.next_month_button, PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_NEXT_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setOnClickPendingIntent(R.id.month_label, PendingIntent.getBroadcast(context, 0,
                        new Intent(context, MonthCalendarWidget.class).setAction(ACTION_RESET_MONTH),
                        PendingIntent.FLAG_UPDATE_CURRENT));

        rv.setViewVisibility(R.id.month_bar, numWeeks <= 1 ? View.GONE : View.VISIBLE);
        // widget provider class에서 위젯의 상태를 업데이트 하는데 쓰인다
        // 해당 widget의 아이디와 업데이트할 rv를 갱신하는 역할을 한다
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }
}

