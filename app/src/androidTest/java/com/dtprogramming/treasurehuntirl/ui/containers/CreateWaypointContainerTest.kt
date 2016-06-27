package com.dtprogramming.treasurehuntirl.ui.containers

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.THApp
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.Espresso.*
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/26/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateWaypointContainerTest {
    val NUDGE_DISTANCE = "0.00001"

    @get:Rule
    val activityTestRule = ActivityTestRule(CreateHuntActivity::class.java, true, false)

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra(CreateHuntActivity.HUNT_UUID, "some uuid")
        activityTestRule.launchActivity(intent)

        onView(withId(R.id.create_hunt_container_add_waypoint)).perform(click())
    }

    @After
    fun tearDown() {
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
    }

    @Test
    fun testInitialLayout() {
        onView(withId(R.id.create_waypoint_container_title)).check(matches(withText("New Marker")))
        onView(withId(R.id.create_waypoint_container_lat)).check(matches(withText("0.0")))
        onView(withId(R.id.create_waypoint_container_lng)).check(matches(withText("0.0")))
    }

    fun testIncreaseDecreaseLatitudeAndLongitude() {
        onView(withId(R.id.create_waypoint_container_lat)).check(matches(withText("0.0")))
        onView(withId(R.id.create_waypoint_container_lng)).check(matches(withText("0.0")))

        // Latitude
        onView(withId(R.id.create_waypoint_container_inc_lat)).perform(click())
        onView(withId(R.id.create_waypoint_container_lat)).check(matches(withText(NUDGE_DISTANCE)))

        onView(withId(R.id.create_waypoint_container_dec_lat)).perform(click())
        onView(withId(R.id.create_waypoint_container_lat)).check(matches(withText("0.0")))

        // Longitude
        onView(withId(R.id.create_waypoint_container_inc_lng)).perform(click())
        onView(withId(R.id.create_waypoint_container_lng)).check(matches(withText(NUDGE_DISTANCE)))

        onView(withId(R.id.create_waypoint_container_dec_lng)).perform(click())
        onView(withId(R.id.create_waypoint_container_lng)).check(matches(withText("0.0")))
    }
}