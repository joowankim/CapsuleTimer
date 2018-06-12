package com.mobile_term_project.knight.a2018_mobile;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by leejisung on 2018-06-08.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> activityTestRule = new ActivityTestRule<Register>(Register.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.register_id)).perform(typeText("T1313estId34567"));
        onView(withId(R.id.register_pw)).perform(typeText("1234"));
        onView(withId(R.id.register_re_pw)).perform(typeText("1234"));
        onView(withId(R.id.register)).perform(click());

        onView(withId(R.id.login_layout)).check(ViewAssertions.matches(isDisplayed()));

    }

}