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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by leejisung on 2018-06-08.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private static final String MESSAGE = "This is a test";

    @Rule
    public ActivityTestRule<Login> activityTestRule = new ActivityTestRule<Login>(Login.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.login_id)).perform(typeText("test"));
        onView(withId(R.id.login_pw)).perform(typeText("test"));
        onView(withId(R.id.login_submit)).perform(click());
        //onView(withText("No account yet? Create one")).check(matches(isDisplayed()));

        onView(withId(R.id.medicine_name)).perform(typeText("tylenol"));
        onView(withId(R.id.medicine_search_btn)).perform(click());
        onView(withId(R.id.medicine_list)).perform(click());
        Thread.sleep(1000);

    }

}