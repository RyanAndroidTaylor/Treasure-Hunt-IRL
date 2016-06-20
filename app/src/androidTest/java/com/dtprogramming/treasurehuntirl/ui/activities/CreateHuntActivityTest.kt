package com.dtprogramming.treasurehuntirl.ui.activities

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.Espresso.*
import com.dtprogramming.treasurehuntirl.R
import kotlinx.android.synthetic.main.activity_create_hunt.*
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/16/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(CreateHuntActivity::class.java)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {

    }

    @Test
    fun moveToCreateClueScreenWhenAddIsPressed() {
        onView(withId(R.id.create_hunt_add_clue)).perform(click())

        onView(withId(R.id.create_clue_text)).check(matches(isDisplayed()))
    }
}