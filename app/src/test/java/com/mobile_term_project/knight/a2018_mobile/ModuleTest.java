package com.mobile_term_project.knight.a2018_mobile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

public class ModuleTest extends AndroidTestCase {

    /**
     * @brief it changes visualized month (previous or next)
     */
    @Test
    public void calendarMonthChaning() {

        Context context = new MockContext();
        setContext(context);
        SharedPreferences sp;
        Calendar cal;

        sp = PreferenceManager.getDefaultSharedPreferences(context);
        cal = Calendar.getInstance();

        int thisMonth = cal.get(Calendar.MONTH);

        assertTrue(cal.get(Calendar.MONTH) == thisMonth);
        Module.calendarMonthChaning(sp, cal, -1);   // previous

        assertTrue(thisMonth - 1 == cal.get(Calendar.MONTH));

        Module.calendarMonthChaning(sp, cal, 1);    // next
        assertTrue(thisMonth == cal.get(Calendar.MONTH));

        Module.calendarMonthChaning(sp, cal, 1);    // next
        assertTrue(thisMonth + 1 == cal.get(Calendar.MONTH));

    }

    @Test
    public void genPendingIntentId() {


    }

    /**
     * @brief Testing for inputting graph data
     */
    @Test
    public void lineSetting() {

        /**
         * Testing case
         * 제시해준 x,y 좌표에 그래프를 그릴 수 있는지
         */
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2, 23));
        entries.add(new Entry(1, 22));

        LineDataSet lineDataSet = new LineDataSet(entries, "");
        Module.lineSetting(lineDataSet, Color.rgb(4, 5, 6));

        assertTrue(lineDataSet.isDrawCirclesEnabled());
        assertTrue(lineDataSet.isDrawCircleHoleEnabled());
    }

    @Test
    public void settingAlarm() {
    }



}