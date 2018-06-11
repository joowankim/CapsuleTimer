package com.example.knight.a2018_mobile;

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
     * @brief Testing for which it changes visualized month (previous or next)
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

    /**
     * @brief Testing for setting pending intent ID
     */
    @Test
    public void genPendingIntentId() {

        int id = 22;
        String time;

        time = "13:43";
        String reqId = Module.genPendingIntentId(id, time);
        assertTrue(reqId == "221343");

        time = "6:32";
        reqId = Module.genPendingIntentId(id, time);
        assertTrue(reqId == "220632");
    }

    /**
     * @brief Testing for inputting graph data
     */
    @Test
    public void lineSetting() {

        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(2, 23));
        entries.add(new Entry(1, 22));

        LineDataSet lineDataSet = new LineDataSet(entries, "");
        Module.lineSetting(lineDataSet, Color.rgb(4, 5, 6));

        assertTrue(lineDataSet.isDrawCirclesEnabled());
        assertTrue(lineDataSet.isDrawCircleHoleEnabled());
    }

}