package com.example.knight.a2018_mobile;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * @brief decorate day when user taked medicine four times
 * @author Joo wan Kim
 * @date 2018.05.18
 * @version 1.0.0.1
 */

class takingDecoratorTriple implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
    private int year;
    private int month;
    private int date;
    private int time;

    private Drawable drawable;

    /**
     * @brief constructor of triple decorator
     * @param year year has day when user taked medicine three times
     * @param month month has day when user taked medicine three times
     * @param day day when user taked medicine three times
     * @param times count of medicine taking
     * @param cont activity context
     */
    public takingDecoratorTriple(int year, int month, int day, int times, Activity cont) {
        if (times >= 0 && times <= 3) {
            this.year = year;
            this.month = month;
            date = day;
            time = times;
            if (time == 1) drawable = cont.getResources().getDrawable(R.drawable.two_more);
            else if (time == 2) drawable = cont.getResources().getDrawable(R.drawable.one_more);
            else if (time == 3) drawable = cont.getResources().getDrawable(R.drawable.take_all);
            else drawable = cont.getResources().getDrawable(R.drawable.none);
        } else {
            Toast.makeText(cont, "먹는 횟수 3회일 때만 가능합니다", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @brief judge that user taked medicine three times
     * @param day date in calendar
     * @return whether user taked medicine three times(true) or not(false)
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int takingYear = calendar.get(Calendar.YEAR);
        int takingMonth = calendar.get(Calendar.MONTH);
        int takingDay = calendar.get(Calendar.DATE);
        return time != 0 && takingDay == date && takingYear == year && takingMonth == month - 1;
    }

    /**
     * @brief decorate the day grid
     * @param view day grid
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.setBackgroundDrawable(drawable);
    }
}
