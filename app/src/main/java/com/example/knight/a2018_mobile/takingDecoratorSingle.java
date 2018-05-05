package com.example.knight.a2018_mobile;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * Created by Kim on 2018-05-06.
 */

class takingDecoratorSingle implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int date;
    private int time;

    private Drawable drawable;

    public takingDecoratorSingle(String dates, int times, Activity cont) {
        if (times >= 0 && times < 2) {
            year = Integer.parseInt(dates.substring(0, 4));
            month = Integer.parseInt(dates.substring(4, 6));
            date = Integer.parseInt(dates.substring(6));
            time = times;
            drawable = cont.getResources().getDrawable(R.drawable.take_all);
        } else {
            Toast.makeText(cont, "먹는 횟수 1회 일때만 가능합니다", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int takingYear = calendar.get(Calendar.YEAR);
        int takingMonth = calendar.get(Calendar.MONTH);
        int takingDay = calendar.get(Calendar.DATE);
        return time != 0 && takingDay == date && takingYear == year && takingMonth == month - 1;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
