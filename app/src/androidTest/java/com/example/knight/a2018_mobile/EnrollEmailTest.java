package com.example.knight.a2018_mobile;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by leejisung on 2018-06-07.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class EnrollEmailTest {

    private static final String MESSAGE = "Some msg";

    @Rule
    public ActivityTestRule<EnrollEmail>activityTestRule = new ActivityTestRule<EnrollEmail>(EnrollEmail.class);

    @Test
    public void onCreate() throws Exception {

        // error남 ;;
        onView(withText("Email 등록")).check(matches(isDisplayed()));
        //onView(withText("아래에 있는 탭을 올려 수신자를 추가해주세요")).check(matches(isDisplayed()));
        onView(withId(R.id.name_hospital)).perform(typeText(MESSAGE));
        onView(withId(R.id.name_doctor)).perform(typeText(MESSAGE));
        onView(withId(R.id.email)).perform(typeText(MESSAGE));
        onView(withId(R.id.update)).perform(click());
        onView(withId(R.id.delete)).perform(click());
    }

//    @Test
//    public void insert() throws Exception {
//    }
//
//    @Test
//    public void delete() throws Exception {
//    }
//
//    @Test
//    public void select() throws Exception {
//    }
//
//    @Test
//    public void invalidate() throws Exception {
//    }

}