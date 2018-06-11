package com.mobile_term_project.knight.a2018_mobile;

import android.graphics.Color;
import android.text.style.ForegroundColorSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

/**
 * @brief decorate sunday grid
 * @author Joo wan Kim
 * @date 2018.05.18
 * @version 1.0.0.1
 */

class SundayDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();

    public SundayDecorator() {
    }

    /**
     * @brief judge that day is sunday
     * @param day  date in calendar
     * @return whether the day is sunday(true) or not(false)
     */
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay == Calendar.SUNDAY;
    }

    /**
     * @brief decorate the day grid
     * @param view day grid
     */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(Color.RED));
    }
}
