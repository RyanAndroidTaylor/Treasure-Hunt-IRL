package com.dtprogramming.treasurehuntirl.ui.fragments

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.ui.activities.TreasureTabActivity
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.assertion.ViewAssertions
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.Matchers
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/15/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntTabFragmentTest {

    @get:Rule
    val activityTestRule: ActivityTestRule<TreasureTabActivity> = ActivityTestRule(TreasureTabActivity::class.java)

    @Before
    fun setUp() {
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)

        val treasureHuntConnection = TreasureHuntConnectionImpl()
        val waypointConnection = WaypointConnectionImpl()
        val clueConnection = ClueConnectionImpl()

        treasureHuntConnection.insert(TreasureHunt("simple uuid", "simple title"))

        for (i in 1..8)
            waypointConnection.insert(Waypoint("waypoint$i uuid", "waypoint $i", "simple uuid", i * 100.0, i * 200.0))

        for (i in 1..5)
            clueConnection.insert(Clue("clue$i uuid", "simple uuid", "answer uuid", "clue $i"))

        onView(withId(R.id.drawer_layout)).perform(swipeLeft())
        onView(withId(R.id.drawer_layout)).perform(swipeLeft())
    }

    @After
    fun tearDown() {

    }

    @Test
    fun whenFABPressedOpenCreateHuntActivity() {
        onView(withId(R.id.create_hunt_fragment_fab)).perform(click())

        onView(withId(R.id.create_hunt_container_add_clue)).check(matches(isDisplayed()))
        onView(withId(R.id.create_hunt_container_title)).check(ViewAssertions.matches(withText("New Treasure Hunt")))
    }

    @Test
    fun displaySavedHuntInfo() {
        onView(withId(R.id.create_hunt_list)).perform(scrollToHolder(Matchers.withTreasureHuntInfo("simple title", 8, 5)))
    }

    @Test
    fun openSavedHunt() {
        onView(withId(R.id.create_hunt_list)).perform(actionOnHolderItem(Matchers.withTreasureHuntInfo("simple title", 8, 5), click()))

        onView(withId(R.id.create_hunt_container_title)).check(ViewAssertions.matches(withText("simple title")))
        onView(withId(R.id.create_hunt_container_clue_list)).perform(scrollToHolder(Matchers.withClueText("clue 4")))
    }
}