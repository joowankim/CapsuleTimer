package com.mobile_term_project.knight.a2018_mobile;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;


/**
 * @brief indicate today in calendar
 * @author Knight
 * @date 2018.05.06
 * @version 1.0.0.1
 */

class oneDayDecorator implements DayViewDecorator {
    private CalendarDay date;

    /**
     * @brief constructor for this
     */
    public oneDayDecorator() {
        date = CalendarDay.today();
    }

    /**
     * @brief judge the date is today
     * @param day a date in calendar
     * @return the day is today(true) or not(false)
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    /**
     * @brief decorate today
     * @param view
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.GREEN));
    }

}
