package com.mobile_term_project.knight.a2018_mobile;

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
public class showGraphTest {

    private static final String MESSAGE = "This is a test";

    @Rule
    public ActivityTestRule<showGraph> activityTestRule = new ActivityTestRule<showGraph>(showGraph.class);

    @Test
    public void onCreate() throws Exception {

        onView(withText("복용량")).check(matches(isDisplayed()));
        onView(withText("남은 약")).check(matches(isDisplayed()));
        onView(withText("반복")).check(matches(isDisplayed()));
        onView(withText("반복 간격")).check(matches(isDisplayed()));
        onView(withText("알람 종류")).check(matches(isDisplayed()));
        onView(withText("날짜")).check(matches(isDisplayed()));
        onView(withText("요일")).check(matches(isDisplayed()));
        onView(withId(R.id.sendReport)).perform(click());

    }

}