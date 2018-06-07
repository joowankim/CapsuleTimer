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
public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> activityTestRule = new ActivityTestRule<Register>(Register.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.register_id)).perform(typeText("this is a test"));
        onView(withId(R.id.register_pw)).perform(typeText("this is a test"));
        onView(withId(R.id.register_re_pw)).perform(typeText("this is a test"));
        onView(withId(R.id.register)).perform(click());

        onView(withText("Already a member? Login")).check(matches(isDisplayed()));


    }

}