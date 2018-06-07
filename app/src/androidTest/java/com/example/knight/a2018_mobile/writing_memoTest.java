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
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Created by leejisung on 2018-06-07.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class writing_memoTest {

    @Rule
    public ActivityTestRule<writing_memo> activityTestRule = new ActivityTestRule<writing_memo>(writing_memo.class);


    @Test
    public void onCreate() throws Exception {
        onView(withId(R.id.memo_content)).perform(typeText("this is test"));
        onView(withId(R.id.memo_upload)).perform(click());
        onView(withId(R.id.medicine_name)).perform(click());
    }

    public void iterateSpinnerItems(){
        String[] myArray = activityTestRule.getActivity().getResources().getStringArray(R.id.medicine_name);
    }

}