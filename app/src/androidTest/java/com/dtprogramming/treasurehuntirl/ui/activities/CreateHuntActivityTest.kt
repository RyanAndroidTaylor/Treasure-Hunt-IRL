package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.espresso.Espresso.*
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.Matchers
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/16/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntActivityTest {

    @get:Rule
    val activityTestRule: ActivityTestRule<CreateHuntActivity> = ActivityTestRule(CreateHuntActivity::class.java, true, false)

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra(CreateHuntActivity.CREATE_NEW, true)

        activityTestRule.launchActivity(intent)
    }

    @After
    fun tearDown() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
    }

    @Test
    fun moveToCreateClueScreenWhenAddIsPressed() {
        onView(withId(R.id.create_hunt_container_add_clue)).perform(click())

        onView(withId(R.id.create_clue__container_clue_text)).check(matches(isDisplayed()))
    }

    @Test
    fun showSavedClues() {
        onView(withId(R.id.create_hunt_container_add_clue)).perform(click())

        onView(withId(R.id.create_clue__container_clue_text)).perform(replaceText("This is a good clue"))

        onView(withId(R.id.create_clue_container_save)).perform(click())

        onView(withId(R.id.create_hunt_container_clue_list)).perform(scrollToHolder(Matchers.withClueText("This is a good clue")))

        onView(withId(R.id.create_hunt_container_add_clue)).perform(click())

        onView(withId(R.id.create_clue__container_clue_text)).perform(replaceText("The next best clue"))

        onView(withId(R.id.create_clue_container_save)).perform(click())

        onView(withId(R.id.create_hunt_container_clue_list)).perform(scrollToHolder(Matchers.withClueText("The next best clue")))
    }
}