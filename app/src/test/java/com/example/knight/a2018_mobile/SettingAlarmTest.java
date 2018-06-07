package com.example.knight.a2018_mobile;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.util.AttributeSet;

import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

/**
 * Created by leejisung on 2018-06-07.
 */
public class SettingAlarmTest extends AndroidTestCase {

    SettingAlarm alarm = new SettingAlarm();
    @Test
    public void onDateSet() throws Exception {

        Context context = new MockContext();
        setContext(context);

        com.wdullaer.materialdatetimepicker.date.DatePickerDialog pickerDialog = new com.wdullaer.materialdatetimepicker.date.DatePickerDialog();

        Calendar mCalendar = Calendar.getInstance();

        alarm.onDateSet(pickerDialog, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH);
        assertNotNull("date", alarm.dateText.getText());
    }


    @Test
    public void onTimeSet() throws Exception {

        Context context = new MockContext();
        setContext(context);

        Calendar mCalendar = Calendar.getInstance();
        alarm.timeText[1].setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[1].getText());
        alarm.timeText[2].setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[2].getText());
        alarm.timeText[3].setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[3].getText());
        alarm.timeText[4].setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[4].getText());
        alarm.timeText[5].setText(mCalendar.get(Calendar.HOUR_OF_DAY) + ":" + mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[5].getText());

    }

}