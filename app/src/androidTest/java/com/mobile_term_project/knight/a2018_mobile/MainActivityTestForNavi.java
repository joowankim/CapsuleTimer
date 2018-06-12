package com.mobile_term_project.knight.a2018_mobile;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
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
import static android.support.test.espresso.contrib.DrawerMatchers.isOpen;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by leejisung on 2018-06-12.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTestForNavi {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule1 = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Rule
    public ActivityTestRule<EnrollEmail> activityTestRule2 = new ActivityTestRule<EnrollEmail>(EnrollEmail.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_memo));
        Thread.sleep(1000);
        Espresso.pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_appinfo));
        Thread.sleep(1000);
        Espresso.pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_homepage));
        Thread.sleep(1000);
        Espresso.pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_email));
        Thread.sleep(1000);

        onView(withId(R.id.name_hospital)).perform(typeText("gil_hospital"));
        onView(withId(R.id.name_doctor)).perform(typeText("doctor"));
        Espresso.pressBack();
        onView(withId(R.id.email)).perform(typeText("mobile@gachon.ac.kr"));
        Espresso.pressBack();
        onView(withId(R.id.update)).perform(click());
        Thread.sleep(1000);
        Espresso.pressBack();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()));
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_logout));
        Thread.sleep(1000);
        Espresso.pressBack();
    }
}