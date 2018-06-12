package com.mobile_term_project.knight.a2018_mobile;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by leejisung on 2018-06-08.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static final String MESSAGE = "This is a test";
    private SettingAlarm s;

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.reminder_title)).perform(typeText("Tylenol"));
        onView(withId(R.id.tgbtn_mon_repeat)).perform(click());
        onView(withId(R.id.test)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.list)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.container)).perform(swipeLeft());
        onView(withId(R.id.linear)).perform(swipeLeft());

        onView(withId(R.id.add)).perform(click());
        onView(withId(R.id.memo_content)).perform(typeText("this is test"));
        onView(withId(R.id.memo_upload)).perform(click());


    }
}