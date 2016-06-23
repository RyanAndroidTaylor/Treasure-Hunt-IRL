package com.dtprogramming.treasurehuntirl.ui.fragments

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.ui.activities.TreasureTabActivity
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.Espresso.*
import com.dtprogramming.treasurehuntirl.R
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/15/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntTabFragmentTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(TreasureTabActivity::class.java)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun whenFABPressedOpenCreateHuntActivity() {
        onView(withId(R.id.drawer_layout)).perform(swipeLeft())
        onView(withId(R.id.drawer_layout)).perform(swipeLeft())

        onView(withId(R.id.create_hunt_fragment_fab)).perform(click())

        onView(withId(R.id.create_hunt_container_add_clue)).check(matches(isDisplayed()))
    }
}