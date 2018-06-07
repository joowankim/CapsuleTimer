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
        AttributeSet set;


        com.wdullaer.materialdatetimepicker.time.RadialPickerLayout pickerDialog = new com.wdullaer.materialdatetimepicker.time.RadialPickerLayout(context, set);

        Calendar mCalendar = Calendar.getInstance();

        alarm.onTimeSet(pickerDialog, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
        assertNotNull("time", alarm.timeText[1].getText());
        assertNotNull("time", alarm.timeText[2].getText());
        assertNotNull("time", alarm.timeText[3].getText());
        assertNotNull("time", alarm.timeText[4].getText());
        assertNotNull("time", alarm.timeText[5].getText());

    }

    @Test
    public void onSwitchAuto() throws Exception {
    }

    @Test
    public void onSwitchRepeat() throws Exception {
    }

    @Test
    public void setRepeatNo() throws Exception {
    }

    @Test
    public void selectRepeatType() throws Exception {
    }

    @Test
    public void setRemainNo() throws Exception {
    }

}