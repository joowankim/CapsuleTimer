package com.example.knight.a2018_mobile;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by leejisung on 2018-06-08.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class SettingAlarmTest {

    private static final String MESSAGE = "This is a test";

    @Rule
    public ActivityTestRule<SettingAlarm> activityTestRule = new ActivityTestRule<SettingAlarm>(SettingAlarm.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.reminder_title)).perform(typeText(MESSAGE));
        onView(withId(R.id.test)).perform(click());
        onView(withId(R.id.set_repeat)).perform(typeText(MESSAGE));
        onView(withId(R.id.set_repeat_no)).perform(typeText(MESSAGE));

        onView(withId(R.id.tgbtn_sun_repeat)).perform(click());
        onView(withId(R.id.tgbtn_mon_repeat)).perform(click());
        onView(withId(R.id.tgbtn_tue_repeat)).perform(click());
        onView(withId(R.id.tgbtn_wed_repeat)).perform(click());
        onView(withId(R.id.tgbtn_thr_repeat)).perform(click());
        onView(withId(R.id.tgbtn_fri_repeat)).perform(click());
        onView(withId(R.id.tgbtn_sat_repeat)).perform(click());

        onView(withId(R.id.addTime)).perform(click());

        onView(withText("요일 반복")).check(matches(isDisplayed()));

    }

    @Test
    public void setDate() throws Exception {
    }

    @Test
    public void onDateSet() throws Exception {
    }

    @Test
    public void setTime() throws Exception {
    }

    @Test
    public void onTimeSet() throws Exception {
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