package com.dtprogramming.treasurehuntirl.ui.activities

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.Espresso.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/15/16.
 */
@RunWith(AndroidJUnit4::class)
class TreasureTabActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(TreasureTabActivity::class.java, true, true)

    @org.junit.Before
    fun setUp() {

    }

    @org.junit.After
    fun tearDown() {

    }

    @Test
    fun titlesAreDisplayed() {
        onView(withText("HUNTS")).check(matches(isDisplayed()))
        onView(withText("IN PROGRESS")).check(matches(isDisplayed()))
        onView(withText("CREATE")).check(matches(isDisplayed()))
    }

    @Test
    fun canSwipeBetweenTabs() {
        onView(withId(R.id.fragment_hunt_list)).check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout)).perform(swipeLeft())

        onView(withId(R.id.current_hunt_list)).check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout)).perform(swipeLeft())

        onView(withId(R.id.create_hunt_list)).check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout)).perform(swipeRight())

        onView(withId(R.id.current_hunt_list)).check(matches(isDisplayed()))

        onView(withId(R.id.drawer_layout)).perform(swipeRight())

        onView(withId(R.id.fragment_hunt_list)).check(matches(isDisplayed()))
    }
}